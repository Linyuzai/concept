package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.icons.AllIcons;
import com.intellij.ide.NewProjectWizardLegacy;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.fileTemplates.impl.CustomFileTemplate;
import com.intellij.ide.projectWizard.ProjectSettingsStep;
import com.intellij.ide.starters.local.*;
import com.intellij.ide.starters.local.wizard.StarterInitialStep;
import com.intellij.ide.starters.shared.*;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.JdkComboBox;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.SdkListItem;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.concurrency.EdtExecutorService;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.io.HttpRequests;
import com.intellij.util.lang.JavaVersion;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

//JavaFxModuleBuilder
@Deprecated
public class ConceptCloudModuleBuilder extends StarterModuleBuilder {

    @NotNull
    @Override
    public String getBuilderId() {
        return "concept-cloud";
    }

    @Nullable
    @Override
    public Icon getNodeIcon() {
        return AllIcons.Nodes.Module;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Concept Cloud";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Generate a project or module to support Spring Cloud and Spring Boot both simultaneous with Gradle";
    }

    @Override
    public int getWeight() {
        return super.getWeight() + 2;
    }

    @Nullable
    @Override
    protected JavaVersion getMinJavaVersion() {
        return super.getMinJavaVersion();
    }

    @NotNull
    @Override
    protected List<StarterProjectType> getProjectTypes() {
        return Collections.singletonList(StarterSettings.getGRADLE_PROJECT());
    }

    @NotNull
    @Override
    protected List<StarterLanguage> getLanguages() {
        return Collections.singletonList(StarterSettings.getJAVA_STARTER_LANGUAGE());
    }

    @NotNull
    @Override
    protected List<StarterTestRunner> getTestFrameworks() {
        return Collections.singletonList(StarterSettings.getJUNIT_TEST_RUNNER());
    }

    @NotNull
    @Override
    protected StarterPack getStarterPack() {
        /*return new StarterPack("concept-cloud", Collections.singletonList(
                new Starter("concept-cloud", "Concept Cloud",
                        getDependencyConfig("/starters/javafx.pom"),
                        Collections.emptyList())));*/
        return new StarterPack("concept-cloud", Collections.emptyList());
    }

    @NotNull
    @Override
    protected List<String> getFilePathsToOpen() {
        List<String> files = new ArrayList<>();
        files.add("build.gradle");
        String packagePath = getPackagePath(getStarterContext().getGroup(), getStarterContext().getArtifact());
        String samplesLanguage = getStarterContext().getLanguage().getId();
        String samplesExt = getSamplesExt(getStarterContext().getLanguage());

        //files.add("src/main/resources/${packagePath}/hello-view.fxml");
        //files.add("src/main/${samplesLanguage}/${packagePath}/HelloController.${samplesExt}");
        //files.add("src/main/${samplesLanguage}/${packagePath}/HelloApplication.${samplesExt}");

        return files;
    }

    @NotNull
    @Override
    protected Map<String, String> getGeneratorContextProperties(@Nullable Sdk sdk, @NotNull DependencyConfig dependencyConfig) {
        return super.getGeneratorContextProperties(sdk, dependencyConfig);
    }

    @Nullable
    @Override
    protected CustomizedMessages getCustomizedMessages() {
        /*CustomizedMessages messages = new CustomizedMessages();
        messages.setProjectTypeLabel("ProjectTypeLabel");
        messages.setServerUrlDialogTitle("ServerUrlDialogTitle");
        messages.setDependenciesLabel("DependenciesLabel");
        messages.setSelectedDependenciesLabel("SelectedDependenciesLabel");
        messages.setNoDependenciesSelectedLabel("NoDependenciesSelectedLabel");
        messages.setFrameworkVersionLabel("FrameworkVersionLabel");*/
        return super.getCustomizedMessages();
    }

    @NotNull
    @Override
    protected List<GeneratorAsset> getAssets(@NotNull Starter starter) {
        boolean project = getStarterContext().isCreatingNewProject();
        FileTemplateManager ftManager = FileTemplateManager.getInstance(ProjectManager.getInstance().getDefaultProject());
        StandardAssetsProvider standardAssetsProvider = new StandardAssetsProvider();
        final FileTemplate build = new CustomFileTemplate("build", "gradle");
        build.setText("");
        final FileTemplate settings = new CustomFileTemplate("settings", "gradle");
        settings.setText("");

        List<GeneratorAsset> assets = new ArrayList<>();
        assets.add(new GeneratorTemplateFile("build.gradle", build));
        assets.add(new GeneratorTemplateFile("settings.gradle", settings));

        /*assets.add(new GeneratorTemplateFile("build.gradle", ftManager.getJ2eeTemplate(JavaFxModuleTemplateGroup.JAVAFX_BUILD_GRADLE)));
        assets.add(new GeneratorTemplateFile("settings.gradle", ftManager.getJ2eeTemplate(JavaFxModuleTemplateGroup.JAVAFX_SETTINGS_GRADLE)));
        assets.add(new GeneratorTemplateFile(standardAssetsProvider.getGradleWrapperPropertiesLocation(),
                ftManager.getJ2eeTemplate(JavaFxModuleTemplateGroup.JAVAFX_GRADLEW_PROPERTIES)));*/
        assets.addAll(standardAssetsProvider.getGradlewAssets());
        assets.addAll(standardAssetsProvider.getGradleIgnoreAssets());

        String packagePath = getPackagePath(getStarterContext().getGroup(), getStarterContext().getArtifact());
        String samplesLanguage = getStarterContext().getLanguage().getId();
        String samplesExt = getSamplesExt(getStarterContext().getLanguage());

        /*assets.add(new GeneratorTemplateFile("src/main/java/module-info.java",
                ftManager.getJ2eeTemplate(JavaFxModuleTemplateGroup.JAVAFX_MODULE_INFO_JAVA)))

        assets.add(new GeneratorTemplateFile("src/main/${samplesLanguage}/${packagePath}/HelloApplication.${samplesExt}",
                ftManager.getJ2eeTemplate("javafx-HelloApplication-${samplesLanguage}.${samplesExt}")));
        assets.add(new GeneratorTemplateFile("src/main/${samplesLanguage}/${packagePath}/HelloController.${samplesExt}",
                ftManager.getJ2eeTemplate("javafx-HelloController-${samplesLanguage}.${samplesExt}")));
        assets.add(new GeneratorTemplateFile("src/main/resources/${packagePath}/hello-view.fxml",
                ftManager.getJ2eeTemplate(JavaFxModuleTemplateGroup.JAVAFX_HELLO_VIEW_FXML)));*/

        return assets;
    }

    @Override
    protected void setupModule(@NotNull Module module) throws ConfigurationException {
        super.setupModule(module);
    }

    @Override
    public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) {
        super.setupRootModel(modifiableRootModel);
    }

    private void updateConceptVersion(Project project) {
        ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
            ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
            indicator.checkCanceled();
            indicator.setText("ProgressIndicator:Request Version");
            try (InputStream is = download(indicator)) {
                Properties properties = new Properties();
                properties.load(is);
                System.out.println(properties);
            } catch (IOException e) {
                EdtExecutorService.getScheduledExecutorInstance().schedule(() -> {
                    Messages.showErrorDialog("Request Version Error", getPresentableName());
                }, 3, TimeUnit.SECONDS);
                //throw new RuntimeException(e);
            }
        }, "Request Version", true, project);
    }

    @RequiresBackgroundThread
    private InputStream download(ProgressIndicator indicator) throws IOException {
        //File tempFile = FileUtil.createTempFile(getBuilderId(), ".tmp", true);
        String url = "https://raw.githubusercontent.com/Linyuzai/concept/master/concept_version.properties";
        //URLConnection connection = request.getConnection();
        return HttpRequests
                .request(url)
                .connectTimeout(10000)
                .isReadResponseOnError(true)
                .connect(HttpRequests.Request::getInputStream);
        //return request.saveToFile(tempFile, indicator);
    }
}
