package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.ide.starters.local.StarterModuleBuilder
import com.intellij.ide.starters.shared.*
import com.intellij.ide.starters.shared.ValidationFunctions.*
import com.intellij.ide.util.PropertiesComponent
import com.intellij.ide.util.projectWizard.ModuleNameGenerator
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.AbstractWizard
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl.Companion.graphProperty
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.observable.properties.map
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel
import com.intellij.openapi.roots.ui.configuration.validateJavaVersion
import com.intellij.openapi.roots.ui.configuration.validateSdk
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.io.FileUtil
import com.intellij.ui.InplaceButton
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.ActionLink
import com.intellij.ui.layout.*
import com.intellij.util.concurrency.Semaphore
import com.intellij.util.ui.AsyncProcessIcon
import com.intellij.util.ui.UIUtil
import java.awt.event.ActionListener
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.InvalidPathException
import java.nio.file.Paths
import java.util.concurrent.Future
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.SwingUtilities

open class ConceptWebStarterInitialStep(contextProvider: ConceptWebStarterContextProvider) : ModuleWizardStep() {
    protected val moduleBuilder: ConceptWebStarterModuleBuilder = contextProvider.moduleBuilder
    protected val wizardContext: WizardContext = contextProvider.wizardContext
    protected val starterContext: ConceptWebStarterContext = contextProvider.starterContext
    protected val starterSettings: ConceptStarterWizardSettings = contextProvider.settings
    protected val parentDisposable: Disposable = contextProvider.parentDisposable

    private val validatedTextComponents: MutableList<JTextField> = mutableListOf()

    protected val propertyGraph: PropertyGraph = PropertyGraph()
    private val entityNameProperty: GraphProperty<String> = propertyGraph.graphProperty(::suggestName)
    private val locationProperty: GraphProperty<String> = propertyGraph.graphProperty(::suggestLocationByName)
    private val groupIdProperty: GraphProperty<String> = propertyGraph.graphProperty { starterContext.group }
    private val artifactIdProperty: GraphProperty<String> = propertyGraph.graphProperty { entityName }
    private val packageNameProperty: GraphProperty<String> = propertyGraph.graphProperty { starterContext.packageName }
    private val sdkProperty: GraphProperty<Sdk?> = propertyGraph.graphProperty { null }

    private val projectTypeProperty: GraphProperty<ConceptStarterProjectType?> =
        propertyGraph.graphProperty { starterContext.projectType }
    private val languageProperty: GraphProperty<ConceptStarterLanguage> =
        propertyGraph.graphProperty { starterContext.language }
    private val packagingProperty: GraphProperty<ConceptStarterAppPackaging?> =
        propertyGraph.graphProperty { starterContext.packaging }
    private val testFrameworkProperty: GraphProperty<ConceptStarterTestRunner?> =
        propertyGraph.graphProperty { starterContext.testFramework }
    private val languageLevelProperty: GraphProperty<ConceptStarterLanguageLevel?> =
        propertyGraph.graphProperty { starterContext.languageLevel }
    private val applicationTypeProperty: GraphProperty<ConceptStarterAppType?> =
        propertyGraph.graphProperty { starterContext.applicationType }
    private val exampleCodeProperty: GraphProperty<Boolean> =
        propertyGraph.graphProperty { starterContext.includeExamples }

    private var entityName: String by entityNameProperty.map { it.trim() }
    private var location: String by locationProperty
    private var groupId: String by groupIdProperty.map { it.trim() }
    private var artifactId: String by artifactIdProperty.map { it.trim() }
    private var languageLevel: ConceptStarterLanguageLevel? by languageLevelProperty
    private var packageName: String by packageNameProperty.map { it.trim() }

    private val contentPanel: DialogPanel by lazy { createComponent() }
    private val progressIcon: AsyncProcessIcon by lazy { AsyncProcessIcon(moduleBuilder.builderId + "ServerOptions") }
    private val serverUrlLink: ActionLink by lazy { createServerUrlLink() }
    private val retryButton: InplaceButton by lazy { createRetryButton() }

    private val sdkModel: ProjectSdksModel = ProjectSdksModel()
    private val languageLevelsModel: DefaultComboBoxModel<ConceptStarterLanguageLevel> =
        DefaultComboBoxModel<ConceptStarterLanguageLevel>()
    private val applicationTypesModel: DefaultComboBoxModel<ConceptStarterAppType> =
        DefaultComboBoxModel<ConceptStarterAppType>()

    private lateinit var projectTypesSelector: ConceptButtonSelectorToolbar
    private lateinit var packagingTypesSelector: ConceptButtonSelectorToolbar
    private lateinit var languagesSelector: ConceptButtonSelectorToolbar

    private var languages: List<ConceptStarterLanguage> = starterSettings.languages
    private var applicationTypes: List<ConceptStarterAppType> = starterSettings.applicationTypes
    private var projectTypes: List<ConceptStarterProjectType> = starterSettings.projectTypes
    private var packagingTypes: List<ConceptStarterAppPackaging> = starterSettings.packagingTypes

    @Volatile
    private var serverOptions: ConceptWebStarterServerOptions? = null

    @Volatile
    private var currentRequest: Future<*>? = null

    private val serverOptionsLoadingSemaphore: Semaphore = Semaphore()
    private val serverSettingsButton: InplaceButton = InplaceButton(
        IconButton("Configure", AllIcons.General.Gear, AllIcons.General.GearHover),
        ActionListener {
            configureServer()
        }
    )

    init {
        Disposer.register(parentDisposable, Disposable {
            sdkModel.disposeUIResources()
            currentRequest?.cancel(true)
        })
    }

    override fun getComponent(): JComponent = contentPanel

    override fun getHelpId(): String? = moduleBuilder.getHelpId()

    override fun updateDataModel() {
        starterContext.serverOptions = this.serverOptions!!

        starterContext.projectType = projectTypeProperty.get()
        starterContext.language = languageProperty.get()
        starterContext.group = groupId
        starterContext.artifact = artifactId
        starterContext.name = entityName
        starterContext.packageName = packageName
        starterContext.packaging = packagingProperty.get()
        starterContext.languageLevel = languageLevel
        starterContext.testFramework = testFrameworkProperty.get()
        starterContext.applicationType = applicationTypeProperty.get()
        starterContext.includeExamples = exampleCodeProperty.get()

        wizardContext.projectName = entityName
        wizardContext.setProjectFileDirectory(location)

        val sdk = sdkProperty.get()
        if (wizardContext.project == null) {
            wizardContext.projectJdk = sdk
        } else {
            moduleBuilder.moduleJdk = sdk
        }
    }

    private fun suggestName(): String {
        return suggestName(starterContext.artifact)
    }

    private fun suggestName(prefix: String): String {
        val projectFileDirectory = File(wizardContext.projectFileDirectory)
        return FileUtil.createSequentFileName(projectFileDirectory, prefix, "")
    }

    private fun suggestLocationByName(): String {
        return FileUtil.join(wizardContext.projectFileDirectory, entityName)
    }

    private fun suggestPackageName(): String {
        return StarterModuleBuilder.suggestPackageName(groupId, artifactId)
    }

    private fun createComponent(): DialogPanel {
        entityNameProperty.dependsOn(locationProperty) { File(location).name }
        entityNameProperty.dependsOn(artifactIdProperty) { artifactId }
        locationProperty.dependsOn(entityNameProperty, ::suggestLocationByName)
        artifactIdProperty.dependsOn(entityNameProperty) { entityName }

        packageNameProperty.dependsOn(artifactIdProperty, ::suggestPackageName)
        packageNameProperty.dependsOn(groupIdProperty, ::suggestPackageName)

        progressIcon.toolTipText = "Connecting to web service and retrieving options..."

        return panel {
            row {
                cell(isFullWidth = true) {
                    label("Server URL:")
                    component(serverUrlLink)
                    component(serverSettingsButton)
                    component(retryButton)
                    component(progressIcon)
                }
            }.largeGapAfter()

            row("Name:") {
                textField(entityNameProperty)
                    .growPolicy(ConceptGrowPolicy.SHORT_TEXT)
                    .withSpecialValidation(CHECK_NOT_EMPTY, CHECK_SIMPLE_NAME_FORMAT)
                    .focused()

                for (nameGenerator in ModuleNameGenerator.EP_NAME.extensionList) {
                    val nameGeneratorUi = nameGenerator.getUi(moduleBuilder.builderId) { entityNameProperty.set(it) }
                    if (nameGeneratorUi != null) {
                        component(nameGeneratorUi).constraints(pushX)
                    }
                }
            }.largeGapAfter()

            row("Location:") {
                projectLocationField(locationProperty, wizardContext)
                    .withSpecialValidation(
                        listOf(CHECK_NOT_EMPTY, CHECK_LOCATION_FOR_ERROR),
                        CHECK_LOCATION_FOR_WARNING
                    )
            }.largeGapAfter()

            addFieldsBefore(this)

            if (starterSettings.languages.size > 1) {
                row("Language:") {
                    languagesSelector = buttonSelector(starterSettings.languages, languageProperty) { it.title }
                }.largeGapAfter()
            }

            row("Language:") {
                label("Java")
            }.largeGapAfter()

            if (starterSettings.projectTypes.size > 1) {
                val messages = starterSettings.customizedMessages
                row(messages?.projectTypeLabel ?: "Type:") {
                    projectTypesSelector =
                        buttonSelector(starterSettings.projectTypes, projectTypeProperty) { it?.title ?: "" }
                }.largeGapAfter()
            }

            row("Type:") {
                label("Gradle")
            }.largeGapAfter()

            if (starterSettings.testFrameworks.isNotEmpty()) {
                row("Test framework:") {
                    buttonSelector(starterSettings.testFrameworks, testFrameworkProperty) { it?.title ?: "" }
                }.largeGapAfter()
            }

            row("Group:") {
                textField(groupIdProperty)
                    .growPolicy(ConceptGrowPolicy.SHORT_TEXT)
                    .withSpecialValidation(
                        CHECK_NOT_EMPTY,
                        CHECK_NO_WHITESPACES,
                        CHECK_GROUP_FORMAT,
                        CHECK_NO_RESERVED_WORDS
                    )
            }.largeGapAfter()

            row("Artifact:") {
                textField(artifactIdProperty)
                    .growPolicy(ConceptGrowPolicy.SHORT_TEXT)
                    .withSpecialValidation(
                        CHECK_NOT_EMPTY,
                        CHECK_NO_WHITESPACES,
                        CHECK_ARTIFACT_SIMPLE_FORMAT,
                        CHECK_NO_RESERVED_WORDS
                    )
            }.largeGapAfter()

            if (starterSettings.isPackageNameEditable) {
                row("Package name:") {
                    textField(packageNameProperty)
                        .growPolicy(ConceptGrowPolicy.SHORT_TEXT)
                        .withSpecialValidation(
                            CHECK_NOT_EMPTY,
                            CHECK_NO_WHITESPACES,
                            CHECK_NO_RESERVED_WORDS,
                            CHECK_PACKAGE_NAME
                        )
                }.largeGapAfter()
            }

            if (starterSettings.applicationTypes.isNotEmpty()) {
                row("Application type:") {
                    applicationTypesModel.addAll(starterSettings.applicationTypes)
                    comboBox(
                        applicationTypesModel,
                        applicationTypeProperty,
                        SimpleListCellRenderer.create("") { it?.title ?: "" })
                        .growPolicy(ConceptGrowPolicy.SHORT_TEXT)
                }.largeGapAfter()
            }

            row("Project SDK:") {
                sdkComboBox(sdkModel, sdkProperty, wizardContext.project, moduleBuilder)
                    .growPolicy(ConceptGrowPolicy.SHORT_TEXT)
            }.largeGapAfter()

            if (starterSettings.languageLevels.isNotEmpty()) {
                row("Java:") {
                    languageLevelsModel.addAll(starterSettings.languageLevels)
                    comboBox(
                        languageLevelsModel,
                        languageLevelProperty,
                        SimpleListCellRenderer.create("") { it?.title ?: "" })
                }.largeGapAfter()
            }

            if (starterSettings.packagingTypes.isNotEmpty()) {
                row("Packaging:") {
                    packagingTypesSelector =
                        buttonSelector(starterSettings.packagingTypes, packagingProperty) { it?.title ?: "" }
                }.largeGapAfter()
            }

            if (starterSettings.isExampleCodeProvided) {
                row {
                    checkBox("Example code", exampleCodeProperty)
                }
            }

            addFieldsAfter(this)
        }.withVisualPadding()
    }

    val CHECK_LOCATION_FOR_WARNING = TextValidationFunction { fieldText: String? ->
        try {
            val file =
                Paths.get(FileUtil.expandUserHome(fieldText!!)).toFile()
            if (file.exists()) {
                val children = file.list()
                if (children != null && children.size > 0) {
                    return@TextValidationFunction "Directory is not empty"
                }
            }
        } catch (ipe: InvalidPathException) {
            return@TextValidationFunction null
        }
        null
    }

    private fun createServerUrlLink(): ActionLink {
        val result = ActionLink(urlPreview(starterContext.serverUrl)) {
            BrowserUtil.browse(starterContext.serverUrl)
        }
        UIUtil.applyStyle(UIUtil.ComponentStyle.REGULAR, result)
        return result
    }

    private fun createRetryButton(): InplaceButton {
        return InplaceButton(IconButton(
            "Retry",
            AllIcons.Nodes.ErrorIntroduction, AllIcons.Actions.ForceRefresh
        ), ActionListener {
            requestServerOptions()
        }).apply {
            isVisible = false
        }
    }

    @NlsSafe
    private fun urlPreview(serverUrl: String): String {
        val url = serverUrl
            .removePrefix("https://")
            .removeSuffix("/")

        if (url.length > 35) {
            return url.take(30) + "..."
        }
        return url
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return contentPanel.preferredFocusedComponent
    }

    override fun validate(): Boolean {
        if (!validateFormFields(component, contentPanel, validatedTextComponents)) {
            return false
        }
        if (!validateSdk(sdkProperty, sdkModel)) {
            return false
        }
        if (!validateJavaVersion(sdkProperty, languageLevel?.javaVersion)) {
            return false
        }

        return checkServerOptionsLoaded()
    }

    private fun checkServerOptionsLoaded(): Boolean {
        val request = currentRequest
        if (serverOptions != null && request == null) {
            return true
        }

        if (request == null) {
            // failure? retry server options loading
            requestServerOptions()
        }

        ProgressManager.getInstance().runProcessWithProgressSynchronously(Runnable {
            val progressIndicator = ProgressManager.getInstance().progressIndicator
            progressIndicator.isIndeterminate = true

            for (i in 0 until 30) {
                progressIndicator.checkCanceled()
                if (serverOptionsLoadingSemaphore.waitFor(500)) {
                    serverOptions?.let { updatePropertiesWithServerOptions(it) }
                    return@Runnable
                }
            }
        }, "Connecting to web service and retrieving options...", true, wizardContext.project)

        return serverOptions != null
    }

    protected open fun addFieldsBefore(layout: ConceptLayoutBuilder) {}

    protected open fun addFieldsAfter(layout: ConceptLayoutBuilder) {}

    override fun _init() {
        super._init()

        if (serverOptions == null && currentRequest == null) {
            @Suppress("HardCodedStringLiteral")
            val serverUrlFromSettings = PropertiesComponent.getInstance().getValue(getServerUrlPropertyName())
            if (serverUrlFromSettings != null) {
                setServerUrl(serverUrlFromSettings)
            }

            // required on dialog opening to get correct modality state
            SwingUtilities.invokeLater(this::requestServerOptions)
        }
    }

    private fun setServerUrl(@NlsSafe url: String) {
        starterContext.serverUrl = url
        serverUrlLink.text = urlPreview(url)
        serverUrlLink.toolTipText = url
    }

    private fun requestServerOptions() {
        progressIcon.isVisible = true
        retryButton.isVisible = false
        progressIcon.resume()

        serverOptionsLoadingSemaphore.down()

        currentRequest = ApplicationManager.getApplication().executeOnPooledThread {
            addStarterNetworkDelay()

            val readyServerOptions = try {
                moduleBuilder.getServerOptions(starterContext.serverUrl)
            } catch (e: Exception) {
                if (e is IOException || e is IllegalStateException) {
                    logger<ConceptWebStarterInitialStep>().info(
                        "Unable to get server options for " + moduleBuilder.builderId,
                        e
                    )
                } else {
                    logger<ConceptWebStarterInitialStep>().error(
                        "Unable to get server options for " + moduleBuilder.builderId,
                        e
                    )
                }

                ApplicationManager.getApplication().invokeLater(
                    {
                        if (component.isShowing) {
                            // only if the wizard is visible
                            Messages.showErrorDialog(
                                "Initialization failed for '${starterContext.serverUrl}'\nPlease check URL, network and proxy settings.\n\nError message:\n${e.message}",
                                "Error"
                            )
                        }
                    }, getModalityState()
                )

                null
            }

            setServerOptions(readyServerOptions)
        }
    }

    private fun setServerOptions(serverOptions: ConceptWebStarterServerOptions?) {
        this.serverOptions = serverOptions
        this.currentRequest = null
        this.serverOptionsLoadingSemaphore.up()

        ApplicationManager.getApplication().invokeLater(Runnable {
            progressIcon.suspend()
            progressIcon.isVisible = false
            retryButton.isVisible = serverOptions == null

            if (serverOptions != null) {
                updatePropertiesWithServerOptions(serverOptions)
            }
        }, getModalityState(), getDisposed())
    }

    private fun getModalityState(): ModalityState {
        return ModalityState.stateForComponent(wizardContext.getUserData(AbstractWizard.KEY)!!.contentComponent)
    }

    private fun getDisposed(): Condition<Any> = Condition<Any> { Disposer.isDisposed(parentDisposable) }

    private fun configureServer() {
        val currentServerUrl = starterContext.serverUrl
        val serverUrlTitle = starterSettings.customizedMessages?.serverUrlDialogTitle
            ?: "Server URL"

        val newUrl =
            Messages.showInputDialog(component, null, serverUrlTitle, null, currentServerUrl, object : InputValidator {
                override fun canClose(inputString: String?): Boolean = checkInput(inputString)

                override fun checkInput(inputString: String?): Boolean {
                    try {
                        URL(inputString)
                    } catch (e: MalformedURLException) {
                        return false
                    }
                    return true
                }
            })

        // update
        if (newUrl != null && starterContext.serverUrl != newUrl) {
            setServerUrl(newUrl)

            PropertiesComponent.getInstance().setValue(getServerUrlPropertyName(), newUrl)

            requestServerOptions()
        }
    }

    private fun getServerUrlPropertyName(): String {
        return moduleBuilder.builderId + ".service.url.last"
    }

    protected open fun updatePropertiesWithServerOptions(serverOptions: ConceptWebStarterServerOptions) {
        starterContext.frameworkVersion = serverOptions.frameworkVersions.find { it.isDefault }
            ?: serverOptions.frameworkVersions.firstOrNull()

        serverOptions.extractOption(SERVER_NAME_KEY) {
            if (entityName == suggestName(DEFAULT_MODULE_NAME)) {
                val newName = suggestName(it)
                if (entityName != newName) {
                    entityNameProperty.set(newName)
                }
            }
        }
        serverOptions.extractOption(SERVER_GROUP_KEY) {
            if (groupId == DEFAULT_MODULE_GROUP && groupId != it) {
                groupIdProperty.set(it)
            }
        }
        serverOptions.extractOption(SERVER_ARTIFACT_KEY) {
            if (artifactId == DEFAULT_MODULE_ARTIFACT && artifactId != it) {
                artifactIdProperty.set(it)
            }
        }
        serverOptions.extractOption(SERVER_PACKAGE_NAME_KEY) {
            if (packageName == DEFAULT_PACKAGE_NAME && packageName != it) {
                packageNameProperty.set(it)
            }
        }
        serverOptions.extractOption(SERVER_VERSION_KEY) {
            starterContext.version = it
        }
        serverOptions.extractOption(SERVER_LANGUAGE_LEVELS_KEY) { levels ->
            val selectedItem = languageLevelsModel.selectedItem
            languageLevelsModel.removeAllElements()
            languageLevelsModel.addAll(levels)

            if (levels.contains(selectedItem)) {
                languageLevelsModel.selectedItem = selectedItem
            } else {
                languageLevel = levels.firstOrNull()
                languageLevelsModel.selectedItem = languageLevel
            }
        }
        serverOptions.extractOption(SERVER_LANGUAGE_LEVEL_KEY) { level ->
            if (languageLevel == starterSettings.defaultLanguageLevel && languageLevel != level) {
                languageLevelProperty.set(level)
            }
        }
        serverOptions.extractOption(SERVER_PROJECT_TYPES) { types ->
            if (types.isNotEmpty() && types != this.projectTypes && ::projectTypesSelector.isInitialized) {
                val correspondingOption = types.find { it.id == projectTypeProperty.get()?.id }
                projectTypeProperty.set(correspondingOption ?: types.first())
                val actionGroup = projectTypesSelector.actionGroup as DefaultActionGroup
                actionGroup.removeAll()
                actionGroup.addAll(types.map {
                    ButtonSelectorAction(
                        it,
                        projectTypeProperty,
                        it.title,
                        it.description
                    )
                })
                this.projectTypes = types
            }
        }
        serverOptions.extractOption(SERVER_APPLICATION_TYPES) { types ->
            if (types.isNotEmpty() && types != applicationTypes) {
                applicationTypesModel.removeAllElements()
                applicationTypesModel.addAll(types)
                applicationTypesModel.selectedItem = types.firstOrNull()
                this.applicationTypes = types
            }
        }
        serverOptions.extractOption(SERVER_PACKAGING_TYPES) { types ->
            if (types.isNotEmpty() && types != this.packagingTypes && ::packagingTypesSelector.isInitialized) {
                val correspondingOption = types.find { it.id == packagingProperty.get()?.id }
                packagingProperty.set(correspondingOption ?: types.first())
                val actionGroup = packagingTypesSelector.actionGroup as DefaultActionGroup
                actionGroup.removeAll()
                actionGroup.addAll(types.map { ButtonSelectorAction(it, packagingProperty, it.title, it.description) })
                this.packagingTypes = types
            }
        }
        serverOptions.extractOption(SERVER_LANGUAGES) { languages ->
            if (languages.isNotEmpty() && languages != this.languages && ::languagesSelector.isInitialized) {
                val correspondingOption = languages.find { it.id == languageProperty.get().id }
                languageProperty.set(correspondingOption ?: languages.first())
                val actionGroup = languagesSelector.actionGroup as DefaultActionGroup
                actionGroup.removeAll()
                actionGroup.addAll(languages.map {
                    ButtonSelectorAction(
                        it,
                        languageProperty,
                        it.title,
                        it.description
                    )
                })
                this.languages = languages
            }
        }

        contentPanel.revalidate()
    }

    @Suppress("SameParameterValue")
    private fun <T : JComponent> ConceptCellBuilder<T>.withSpecialValidation(vararg errorValidations: TextValidationFunction): ConceptCellBuilder<T> {
        return this.withSpecialValidation(errorValidations.asList(), null)
    }

    private fun <T : JComponent> ConceptCellBuilder<T>.withSpecialValidation(
        errorValidations: List<TextValidationFunction>,
        warningValidation: TextValidationFunction?
    ): ConceptCellBuilder<T> {
        return withValidation(this, errorValidations, warningValidation, validatedTextComponents, parentDisposable)
    }
}