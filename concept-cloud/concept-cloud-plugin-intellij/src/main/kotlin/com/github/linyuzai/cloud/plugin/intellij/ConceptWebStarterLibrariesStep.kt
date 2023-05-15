package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ide.starters.shared.*
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.AbstractWizard
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl.Companion.graphProperty
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.*
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.*
import com.intellij.util.ModalityUiUtil
import com.intellij.util.concurrency.EdtExecutorService
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import com.intellij.util.io.HttpRequests
import com.intellij.util.io.HttpRequests.RequestProcessor
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil.DEFAULT_HGAP
import com.intellij.util.ui.UIUtil.DEFAULT_VGAP
import com.intellij.util.ui.components.BorderLayoutPanel
import com.intellij.util.ui.tree.TreeUtil
import com.intellij.util.ui.update.MergingUpdateQueue
import com.intellij.util.ui.update.Update
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.io.IOException
import java.net.URLConnection
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener
import javax.swing.event.TreeSelectionListener
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode
import javax.swing.tree.TreeSelectionModel

open class ConceptWebStarterLibrariesStep(contextProvider: ConceptWebStarterContextProvider) : ModuleWizardStep() {
    protected val moduleBuilder: ConceptWebStarterModuleBuilder = contextProvider.moduleBuilder
    protected val wizardContext: WizardContext = contextProvider.wizardContext
    protected val starterContext: ConceptWebStarterContext = contextProvider.starterContext
    protected val starterSettings: StarterWizardSettings = contextProvider.settings
    protected val parentDisposable: Disposable = contextProvider.parentDisposable

    private val topLevelPanel: JPanel = BorderLayoutPanel()
    private val contentPanel: DialogPanel by lazy { createComponent() }

    private val librariesList: CheckboxTreeBase by lazy { createLibrariesList() }
    private val librariesSearchField: ConceptLibrariesSearchTextField by lazy { createLibrariesFilter() }
    private val libraryDescriptionPanel: ConceptLibraryDescriptionPanel by lazy { ConceptLibraryDescriptionPanel() }
    private val selectedLibrariesPanel: ConceptSelectedLibrariesPanel by lazy { createSelectedLibrariesPanel() }
    private val frameworkVersionsModel: DefaultComboBoxModel<ConceptWebStarterFrameworkVersion> = DefaultComboBoxModel()

    protected val propertyGraph: PropertyGraph = PropertyGraph()
    private val frameworkVersionProperty: GraphProperty<ConceptWebStarterFrameworkVersion?> =
        propertyGraph.graphProperty { null }
    private val selectedDependencies: MutableSet<ConceptWebStarterDependency> = mutableSetOf()

    private var currentSearchString: String = ""
    private val searchMergingUpdateQueue: MergingUpdateQueue by lazy {
        MergingUpdateQueue("SearchLibs_" + moduleBuilder.builderId, 250, true, topLevelPanel, parentDisposable)
    }

    override fun getComponent(): JComponent = topLevelPanel

    override fun getHelpId(): String? = moduleBuilder.getHelpId()

    override fun getPreferredFocusedComponent(): JComponent? = librariesSearchField

    override fun updateDataModel() {
        starterContext.frameworkVersion = frameworkVersionProperty.get()
        starterContext.dependencies.clear()
        starterContext.dependencies.addAll(selectedDependencies)
    }

    override fun onStepLeaving() {
        super.onStepLeaving()

        updateDataModel()
    }

    override fun _init() {
        super._init()

        if (topLevelPanel.componentCount == 0) {
            topLevelPanel.add(contentPanel, BorderLayout.CENTER)
        }

        if (topLevelPanel.isDisplayable && topLevelPanel.isShowing) {
            // called after unsuccessful validation of step
            return
        }

        // libraries list may depend on options specified on the first step
        loadLibrariesList()
        loadFrameworkVersions()

        updateAvailableDependencies()
        getLibrariesRoot()?.let {
            selectFirstDependency(it)
        }
    }

    final override fun validate(): Boolean {
        val unavailable = selectedDependencies.filter { getDependencyState(it) is ConceptDependencyUnavailable }
        if (unavailable.isNotEmpty()) {
            val dependencyInfo = unavailable.joinToString { it.title }
            val version = frameworkVersionProperty.get()?.title ?: ""
            Messages.showErrorDialog(
                "Selected dependencies: $dependencyInfo are not available for version $version.", "Error"
            )
            return false
        }

        if (!validateFields()) {
            return false
        }

        if (starterContext.result == null) {
            // commit selected dependencies to starterContext
            updateDataModel()

            // try to validate and download result
            requestWebService()

            if (starterContext.result == null) {
                return false
            }
        }

        return true
    }

    protected open fun validateFields(): Boolean {
        return true
    }

    @RequiresBackgroundThread
    protected open fun validateWithServer(progressIndicator: ProgressIndicator): Boolean {
        return true
    }

    private fun requestWebService() {
        ProgressManager.getInstance().runProcessWithProgressSynchronously(
            {
                val progressIndicator = ProgressManager.getInstance().progressIndicator

                if (!validateWithServer(progressIndicator)) {
                    return@runProcessWithProgressSynchronously
                }

                progressIndicator.checkCanceled()

                progressIndicator.text =
                    "Downloading ${moduleBuilder.presentableName} Template..."

                val downloadResult: ConceptDownloadResult? = try {
                    downloadResult(progressIndicator)
                } catch (e: Exception) {
                    logger<ConceptWebStarterLibrariesStep>().info(e)

                    EdtExecutorService.getScheduledExecutorInstance().schedule(
                        {
                            var message = "Error: ${e.message}"
                            message =
                                StringUtil.shortenTextWithEllipsis(message, 1024, 0) // exactly 1024 because why not
                            Messages.showErrorDialog(message, moduleBuilder.presentableName)
                        },
                        3, TimeUnit.SECONDS
                    )

                    null
                }

                starterContext.result = downloadResult
            }, "Preparing template...", true, wizardContext.project
        )
    }

    @RequiresBackgroundThread
    private fun downloadResult(progressIndicator: ProgressIndicator): ConceptDownloadResult {
        addStarterNetworkDelay()

        val tempFile = FileUtil.createTempFile(moduleBuilder.builderId, ".tmp", true)
        val log = logger<ConceptWebStarterLibrariesStep>()

        val url = moduleBuilder.getGeneratorUrlInternal(starterContext.serverUrl, starterContext).toExternalForm()
        log.info("Loading project from ${url}")

        return HttpRequests
            .request(url)
            .userAgent(moduleBuilder.getUserAgentInternal())
            .connectTimeout(10000)
            .isReadResponseOnError(true)
            .connect(RequestProcessor { request ->
                val connection: URLConnection = try {
                    request.connection
                } catch (e: IOException) {
                    log.warn(
                        "Can't download project. Message (with headers info): " + HttpRequests.createErrorMessage(
                            e,
                            request,
                            true
                        )
                    )
                    throw IOException(HttpRequests.createErrorMessage(e, request, false), e)
                } catch (he: UnknownHostException) {
                    log.warn("Can't download project: " + he.message)
                    throw IOException(HttpRequests.createErrorMessage(he, request, false), he)
                }

                val contentType = connection.contentType
                val contentDisposition = connection.getHeaderField("Content-Disposition")
                val filename = getFilename(contentDisposition)
                val isZip = StringUtil.isNotEmpty(contentType) && contentType.startsWith("application/zip")
                        || filename.endsWith(".zip")
                // Micronaut has broken content-type (it's "text") but zip-file as attachment
                // (https://github.com/micronaut-projects/micronaut-starter/issues/268)

                request.saveToFile(tempFile, progressIndicator)

                ConceptDownloadResult(isZip, tempFile, filename)
            })
    }

    @NlsSafe
    private fun getFilename(contentDisposition: String?): String {
        val filenameField = "filename="
        if (StringUtil.isEmpty(contentDisposition)) return "unknown"

        val startIdx = contentDisposition!!.indexOf(filenameField)
        val endIdx = contentDisposition.indexOf(';', startIdx)
        var fileName = contentDisposition.substring(
            startIdx + filenameField.length,
            if (endIdx > 0) endIdx else contentDisposition.length
        )
        if (StringUtil.startsWithChar(fileName, '\"') && StringUtil.endsWithChar(fileName, '\"')) {
            fileName = fileName.substring(1, fileName.length - 1)
        }
        return fileName
    }

    private fun loadFrameworkVersions() {
        val availableFrameworkVersions = getAvailableFrameworkVersions()
        frameworkVersionsModel.removeAllElements()
        frameworkVersionsModel.addAll(availableFrameworkVersions)
        val defaultVersion = starterContext.frameworkVersion ?: availableFrameworkVersions.firstOrNull()
        if (availableFrameworkVersions.contains(defaultVersion)) {
            frameworkVersionProperty.set(defaultVersion)
        } else {
            frameworkVersionProperty.set(availableFrameworkVersions.firstOrNull())
        }
    }

    protected open fun addFieldsAfter(layout: ConceptLayoutBuilder) {}

    private fun createComponent(): DialogPanel {
        val messages = starterSettings.customizedMessages
        selectedLibrariesPanel.emptyText.text = messages?.noDependenciesSelectedLabel
            ?: "No dependencies added"

        return panel(LCFlags.fillX, LCFlags.fillY) {
            val frameworkVersions = getAvailableFrameworkVersions()
            if (frameworkVersions.isNotEmpty()) {
                row {
                    cell(isFullWidth = true) {
                        label(
                            messages?.frameworkVersionLabel ?: "Version:"
                        )

                        if (frameworkVersions.size == 1) {
                            label(frameworkVersions[0].title)
                        } else {
                            frameworkVersionsModel.addListDataListener(object : ListDataListener {
                                override fun intervalAdded(e: ListDataEvent?) = updateAvailableDependencies()
                                override fun intervalRemoved(e: ListDataEvent?) = updateAvailableDependencies()
                                override fun contentsChanged(e: ListDataEvent?) = updateAvailableDependencies()
                            })
                            comboBox(
                                frameworkVersionsModel,
                                frameworkVersionProperty,
                                SimpleListCellRenderer.create("") { it?.title ?: "" })
                        }
                    }
                }.largeGapAfter()
            }

            addFieldsAfter(this)

            row {
                label(messages?.dependenciesLabel ?: "Dependencies:")
            }

            row {
                component(JPanel(GridBagLayout()).apply {
                    add(BorderLayoutPanel().apply {
                        preferredSize = Dimension(0, 0)

                        addToTop(librariesSearchField)
                        addToCenter(ScrollPaneFactory.createScrollPane(librariesList))
                    }, gridConstraint(0, 0))

                    add(JPanel(GridBagLayout()).apply {
                        border = JBUI.Borders.emptyLeft(DEFAULT_HGAP * 2)
                        preferredSize = Dimension(0, 0)

                        add(libraryDescriptionPanel.apply {
                            preferredSize = Dimension(0, 0)
                        }, gridConstraint(0, 0))
                        add(BorderLayoutPanel().apply {
                            preferredSize = Dimension(0, 0)

                            addToTop(JBLabel(
                                messages?.selectedDependenciesLabel ?: "Added dependencies:"
                            ).apply {
                                border = JBUI.Borders.empty(0, 0, DEFAULT_VGAP * 2, 0)
                            })
                            addToCenter(selectedLibrariesPanel)
                        }, gridConstraint(0, 1))
                    }, gridConstraint(1, 0))
                }).constraints(push, grow)
            }
        }.withVisualPadding()
    }

    internal fun gridConstraint(col: Int, row: Int): GridBagConstraints {
        return GridBagConstraints().apply {
            fill = GridBagConstraints.BOTH
            gridx = col
            gridy = row
            weightx = 1.0
            weighty = 1.0
        }
    }

    private fun getAvailableFrameworkVersions(): List<ConceptWebStarterFrameworkVersion> {
        return starterContext.serverOptions.frameworkVersions.filter {
            moduleBuilder.isVersionAvailableInternal(it)
        }
    }

    private fun createLibrariesList(): CheckboxTreeBase {
        val list = CheckboxTreeBase(object : CheckboxTree.CheckboxTreeCellRenderer() {
            override fun customizeRenderer(
                tree: JTree?,
                value: Any?,
                selected: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
            ) {
                if (value !is DefaultMutableTreeNode) return

                this.border = JBUI.Borders.empty(2, 0)
                when (val item = value.userObject) {
                    is ConceptWebStarterDependencyCategory -> textRenderer.append(item.title)
                    is ConceptWebStarterDependency -> {
                        val enabled = (value as CheckedTreeNode).isEnabled
                        val attributes =
                            if (enabled) SimpleTextAttributes.REGULAR_ATTRIBUTES else SimpleTextAttributes.GRAYED_ATTRIBUTES
                        textRenderer.append(item.title, attributes)
                    }
                }
            }
        }, null)
        list.emptyText.text = "Nothing found"

        enableEnterKeyHandling(list)

        list.rowHeight = 0
        list.isRootVisible = false
        list.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION

        list.addCheckboxTreeListener(object : CheckboxTreeListener {
            override fun nodeStateChanged(node: CheckedTreeNode) {
                val dependency = node.userObject as? ConceptWebStarterDependency ?: return
                if (node.isChecked) {
                    selectedDependencies.add(dependency)
                } else {
                    selectedDependencies.remove(dependency)
                }
                librariesList.repaint()
                selectedLibrariesPanel.update(selectedDependencies)
            }
        })
        list.selectionModel.addTreeSelectionListener(TreeSelectionListener { e ->
            val path = e.path
            if (path != null && e.isAddedPath) {
                when (val item = (path.lastPathComponent as? DefaultMutableTreeNode)?.userObject) {
                    is ConceptWebStarterDependency -> {
                        updateSelectedLibraryInfo(item)
                    }

                    is ConceptWebStarterDependencyCategory -> libraryDescriptionPanel.update(item.title, null)
                }
            } else {
                libraryDescriptionPanel.reset()
            }
        })
        librariesSearchField.list = list

        return list
    }

    internal fun enableEnterKeyHandling(list: CheckboxTreeBase) {
        list.inputMap.put(KeyStroke.getKeyStroke("ENTER"), "pick-node")
        list.actionMap.put("pick-node", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                val selection = list.selectionPath
                if (selection != null) {
                    if (selection.lastPathComponent is CheckedTreeNode) {
                        val node = selection.lastPathComponent as CheckedTreeNode
                        list.setNodeState(node, !node.isChecked)
                    } else if (selection.lastPathComponent is DefaultMutableTreeNode) {
                        if (list.isExpanded(selection)) {
                            list.collapsePath(selection)
                        } else {
                            list.expandPath(selection)
                        }
                    }
                }
            }
        })
    }

    private fun isDependencyMatched(item: ConceptWebStarterDependency, search: String): Boolean {
        return item.title.contains(search, true)
                || (item.description ?: "").contains(search, true)
                || item.id.contains(search, true)
    }

    private fun createLibrariesFilter(): ConceptLibrariesSearchTextField {
        val textField = ConceptLibrariesSearchTextField()
        textField.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                searchMergingUpdateQueue.queue(Update.create("", Runnable {
                    ModalityUiUtil.invokeLaterIfNeeded(getModalityState(), Runnable {
                        currentSearchString = textField.text
                        loadLibrariesList()
                        librariesList.repaint()
                    })
                }))
            }
        })
        return textField
    }

    protected fun getModalityState(): ModalityState {
        return ModalityState.stateForComponent(wizardContext.getUserData(AbstractWizard.KEY)!!.contentComponent)
    }

    protected fun getDisposed(): Condition<Any> = Condition<Any> { Disposer.isDisposed(parentDisposable) }

    private fun createSelectedLibrariesPanel(): ConceptSelectedLibrariesPanel {
        val panel = ConceptSelectedLibrariesPanel()
        val messages = starterSettings.customizedMessages
        panel.emptyText.text = messages?.noDependenciesSelectedLabel ?: "No dependencies added"
        panel.libraryRemoveListener = { libraryInfo ->
            selectedDependencies.remove(libraryInfo)
            walkCheckedTree(getLibrariesRoot()) {
                if (it.userObject == libraryInfo) {
                    librariesList.setNodeState(it, false)
                }
            }
            selectedLibrariesPanel.update(selectedDependencies)
        }
        if (starterContext.frameworkVersion != null) {
            panel.dependencyStateFunction = { libraryInfo ->
                getDependencyState(libraryInfo)
            }
        }

        return panel
    }

    internal fun walkCheckedTree(root: CheckedTreeNode?, visitor: (CheckedTreeNode) -> Unit) {
        if (root == null) return

        fun walkTreeNode(root: TreeNode, visitor: (CheckedTreeNode) -> Unit) {
            if (root is CheckedTreeNode) {
                visitor.invoke(root)
            }

            for (child in root.children()) {
                walkTreeNode(child, visitor)
            }
        }

        walkTreeNode(root, visitor)
    }

    private fun getLibrariesRoot(): CheckedTreeNode? {
        return librariesList.model.root as? CheckedTreeNode
    }

    private fun getDependencyState(libraryInfo: LibraryInfo): ConceptDependencyState {
        val frameworkVersion = frameworkVersionProperty.get() ?: return ConceptDependencyAvailable
        return moduleBuilder.getDependencyStateInternal(frameworkVersion, libraryInfo as ConceptWebStarterDependency)
    }

    private fun loadLibrariesList() {
        val librariesRoot = CheckedTreeNode()
        val search = currentSearchString.trim()

        val dependencyCategories = starterContext.serverOptions.dependencyCategories
        for (category in dependencyCategories) {
            if (!category.isAvailable(starterContext)) continue

            val categoryNode = DefaultMutableTreeNode(category, true)

            for (dependency in category.dependencies) {
                if (search.isBlank() || isDependencyMatched(dependency, search)) {
                    val libraryNode = CheckedTreeNode(dependency)
                    if (dependency.isDefault) {
                        selectedDependencies.add(dependency)
                    }

                    libraryNode.isChecked = selectedDependencies.contains(dependency)

                    if (dependency.isDefault) {
                        libraryNode.isEnabled = false
                    } else {
                        val state = getDependencyState(dependency)
                        libraryNode.isEnabled = state is ConceptDependencyAvailable
                    }

                    if (dependencyCategories.size > 1) {
                        categoryNode.add(libraryNode)
                    } else {
                        librariesRoot.add(libraryNode)
                    }
                }
            }

            if (dependencyCategories.size > 1) {
                if (categoryNode.childCount > 0) {
                    librariesRoot.add(categoryNode)
                }
            }
        }
        librariesList.model = DefaultTreeModel(librariesRoot)

        if (search.isNotBlank()) {
            for (category in librariesRoot.children()) {
                librariesList.expandPath(TreeUtil.getPath(librariesRoot, category))
            }
            selectFirstDependency(librariesRoot)
        }
    }

    private fun selectFirstDependency(librariesRoot: CheckedTreeNode) {
        if (librariesRoot.childCount > 0) {
            val firstNode = librariesRoot.getChildAt(0)
            if (firstNode is CheckedTreeNode) {
                librariesList.selectionModel.addSelectionPath(TreeUtil.getPath(librariesRoot, firstNode))
            } else {
                librariesList.expandPath(TreeUtil.getPath(librariesRoot, firstNode))
                if (firstNode.childCount > 0) {
                    librariesList.selectionModel.addSelectionPath(
                        TreeUtil.getPath(
                            librariesRoot,
                            firstNode.getChildAt(0)
                        )
                    )
                }
            }
        }
    }

    private fun updateSelectedLibraryInfo(item: ConceptWebStarterDependency) {
        val dependencyState = getDependencyState(item)
        val versionInfo = if (dependencyState is ConceptDependencyUnavailable) dependencyState.hint else null
        libraryDescriptionPanel.update(item, versionInfo)
    }

    private fun updateAvailableDependencies() {
        selectedLibrariesPanel.update(selectedDependencies)

        val root = getLibrariesRoot() ?: return

        walkCheckedTree(root) {
            val dependency = it.userObject as? ConceptWebStarterDependency
            if (dependency != null) {
                val state = getDependencyState(dependency)
                it.isEnabled = state is ConceptDependencyAvailable
            }
        }
        librariesList.repaint()

        val selectedDependency = (librariesList.selectionPath?.lastPathComponent as? CheckedTreeNode)?.userObject
        if (selectedDependency is ConceptWebStarterDependency) {
            updateSelectedLibraryInfo(selectedDependency)
        }
    }
}
