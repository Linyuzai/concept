@file:JvmName("ConceptWebStarterSettings")

package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ide.starters.shared.*
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.util.io.Decompressor
import org.jetbrains.annotations.Nls
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.zip.ZipFile

open class ConceptWebStarterServerOptions(
    val frameworkVersions: List<ConceptWebStarterFrameworkVersion>,
    val dependencyCategories: List<ConceptWebStarterDependencyCategory>,
) : UserDataHolderBase() {

    fun <T> extractOption(key: Key<T>, handler: (T) -> Unit) {
        val value = getUserData(key)
        if (value != null) {
            handler.invoke(value)
        }
    }
}

val SERVER_NAME_KEY: Key<String> = Key.create("name")
val SERVER_GROUP_KEY: Key<String> = Key.create("group")
val SERVER_ARTIFACT_KEY: Key<String> = Key.create("artifact")
val SERVER_VERSION_KEY: Key<String> = Key.create("version")
val SERVER_PACKAGE_NAME_KEY: Key<String> = Key.create("packageName")
val SERVER_LANGUAGES: Key<List<ConceptStarterLanguage>> = Key.create("languages")
val SERVER_LANGUAGE_LEVEL_KEY: Key<ConceptStarterLanguageLevel> = Key.create("languageLevel")
val SERVER_LANGUAGE_LEVELS_KEY: Key<List<ConceptStarterLanguageLevel>> = Key.create("languageLevels")
val SERVER_PROJECT_TYPES: Key<List<ConceptStarterProjectType>> = Key.create("projectTypes")
val SERVER_APPLICATION_TYPES: Key<List<ConceptStarterAppType>> = Key.create("appTypes")
val SERVER_PACKAGING_TYPES: Key<List<ConceptStarterAppPackaging>> = Key.create("packagingTypes")

open class ConceptWebStarterFrameworkVersion(
    val id: String,
    val title: String,
    val isDefault: Boolean
) {
    override fun toString(): String {
        return "WebStarterFrameworkVersion(version='$id', isDefault=$isDefault)"
    }
}

open class ConceptWebStarterDependencyCategory(

    val title: String,
    val dependencies: List<ConceptWebStarterDependency>
) {
    open fun isAvailable(starterContext: ConceptWebStarterContext): Boolean = true

    override fun toString(): String {
        return "WebStarterDependencyCategory(title='$title')"
    }
}

open class ConceptWebStarterDependency(
    val id: String,

    override val title: String,
    override val description: String? = null,
    override val links: List<ConceptLibraryLink> = emptyList(),
    override val isDefault: Boolean = false,
    override val isRequired: Boolean = false
) : ConceptLibraryInfo {
    override fun toString(): String {
        return "WebStarterDependency(id='$id', title='$title')"
    }
}

class ConceptWebStarterContextProvider(
    val moduleBuilder: ConceptWebStarterModuleBuilder,
    val wizardContext: WizardContext,
    val starterContext: ConceptWebStarterContext,
    val settings: ConceptStarterWizardSettings,
    val parentDisposable: Disposable
)

sealed class ConceptDependencyState

class ConceptDependencyUnavailable(
    @Nls(capitalization = Nls.Capitalization.Sentence)
    val message: String?,

    val hint: String? = null
) : ConceptDependencyState()

object ConceptDependencyAvailable : ConceptDependencyState()

fun addStarterNetworkDelay() {
    // todo remove
}

@Throws(IOException::class)
fun unzipSubfolder(tempZipFile: File, contentEntryDir: File) {
    var rootFolderName: String

    ZipFile(tempZipFile).use { jar ->
        val entries = jar.entries()
        if (!entries.hasMoreElements()) {
            throw ConceptUnexpectedArchiveStructureException("The archive is empty")
        }
        val rootFolders = HashSet<String>()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val path = Paths.get(entry.name)
            if (path.nameCount > 0) {
                rootFolders.add(path.normalize().getName(0).toString())
            }
        }
        if (rootFolders.size != 1) {
            throw ConceptUnexpectedArchiveStructureException(
                "The archive should have 1 subdirectory, but has: " + rootFolders.joinToString(",")
            )
        }
        rootFolderName = rootFolders.iterator().next()
    }

    Decompressor.Zip(tempZipFile)
        .removePrefixPath(rootFolderName)
        .extract(contentEntryDir)
}

class ConceptDownloadResult(
    val isZip: Boolean,
    val tempFile: File,
    val filename: String
)

internal class ConceptUnexpectedArchiveStructureException(message: String) : IOException(message)