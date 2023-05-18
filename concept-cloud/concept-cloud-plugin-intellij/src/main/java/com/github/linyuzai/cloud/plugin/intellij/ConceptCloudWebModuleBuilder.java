package com.github.linyuzai.cloud.plugin.intellij;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.Url;
import com.intellij.util.Urls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressWarnings("all")
public class ConceptCloudWebModuleBuilder extends ConceptWebStarterModuleBuilder {

    private final Key<Map<String, ConceptFrameworkVersion>> CONCEPT_CLOUD_CFV_KEY = new Key<>("CONCEPT_CLOUD_CFV_KEY");

    @NotNull
    @Override
    protected Url composeGeneratorUrl(@NotNull String s, @NotNull ConceptWebStarterContext webStarterContext) {
        ConceptWebStarterFrameworkVersion frameworkVersion = webStarterContext.getFrameworkVersion();
        String version = frameworkVersion.getId();
        String location = "java/" + version + "/template.zip";
        String url;
        if (s.endsWith("/")) {
            url = s + location;
        } else {
            url = s + "/" + location;
        }
        return Urls.newUnparsable(url);
    }

    @Override
    protected void extractGeneratorResult(@NotNull File download, @NotNull File parent) {
        //Decompressor.Zip
        try (ZipFile zf = new ZipFile(download)) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            List<ZipEntry> files = new ArrayList<>();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    String dirName = transform(entry.getName(), true);
                    createDir(dirName, parent);
                } else {
                    files.add(entry);
                }
            }
            for (ZipEntry file : files) {
                String fileName = transform(file.getName(), false);
                File create = createFile(fileName, parent);
                if (intact(file.getName())) {
                    try (InputStream is = zf.getInputStream(file);
                         OutputStream os = new FileOutputStream(create)) {
                        FileUtil.copy(is, os);
                    }
                    continue;
                }
                if ("concept.gradle".equals(file.getName())) {
                    handleConceptGradle(create);
                    continue;
                }
                try (BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(file), StandardCharsets.UTF_8))) {
                    String collect = br.lines().collect(Collectors.joining("\n"));
                    String transform = transform(collect, false);
                    String content;
                    if ("gradle/wrapper/gradle-wrapper.properties".equals(file.getName())
                            || "build.gradle".equals(file.getName())) {
                        content = handleVersions(transform);
                    } else {
                        content = transform;
                    }
                    FileUtil.writeToFile(create, content);
                }
            }
        } catch (Throwable e) {
            Messages.showErrorDialog("Unzip error: " + e.getMessage(), getPresentableName());
        }
    }

    private boolean intact(String name) {
        ConceptFrameworkVersion conceptFrameworkVersion = getConceptFrameworkVersion();
        if (conceptFrameworkVersion == null) {
            return false;
        }
        return conceptFrameworkVersion.intact.contains(name);
    }

    private void handleConceptGradle(File file) throws IOException {
        ConceptFrameworkVersion conceptFrameworkVersion = getConceptFrameworkVersion();
        if (conceptFrameworkVersion == null) {
            return;
        }
        Set<ConceptWebStarterDependency> dependencies = getStarterContext().getDependencies();
        StringBuilder builder = new StringBuilder();
        builder.append("dependencies {\n");
        for (ConceptWebStarterDependency dependency : dependencies) {
            Collection<String> collection = conceptFrameworkVersion.getConceptDependency(dependency.getId());
            if (collection != null) {
                for (String s : collection) {
                    builder.append("\t").append(s).append("\n");
                }
            }
        }
        builder.append("}");
        FileUtil.writeToFile(file, builder.toString());
    }

    private String handleVersions(String content) {
        Versions versions = getVersions();
        return content
                .replaceAll("\\$V_GRADLE\\$", versions.gradle)
                .replaceAll("\\$V_SPRING_BOOT\\$", versions.springBoot)
                .replaceAll("\\$V_SPRING_DM\\$", versions.springDependencyManagement)
                .replaceAll("\\$V_SPRING_CLOUD_DM\\$", versions.springCloudDependencyManagement)
                .replaceAll("\\$V_CONCEPT_DOMAIN\\$", versions.conceptDomain)
                .replaceAll("\\$V_CONCEPT_CLOUD_WEB\\$", versions.conceptCloudWeb)
                .replaceAll("\\$V_MBP\\$", versions.myBatisPlus);
    }

    private ConceptFrameworkVersion getConceptFrameworkVersion() {
        Map<String, ConceptFrameworkVersion> data = getStarterContext().getUserData(CONCEPT_CLOUD_CFV_KEY);
        if (data == null) {
            return null;
        }
        ConceptWebStarterFrameworkVersion frameworkVersion = getStarterContext().getFrameworkVersion();
        if (frameworkVersion == null) {
            return null;
        }
        String frameworkVersionId = frameworkVersion.getId();
        return data.get(frameworkVersionId);
    }

    private void createDir(String name, File parent) {
        File file = new File(parent, name);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private File createFile(String name, File parent) throws IOException {
        File file = new File(parent, name);
        File parentFile = new File(transform(file.getParent(), true));
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File create = new File(parentFile, file.getName());
        if (!create.exists()) {
            create.createNewFile();
        }
        return create;
    }

    private String transform(String content, boolean replaceDot) {
        String group = getStarterContext().getGroup();
        String artifact = getStarterContext().getArtifact();
        String version = getStarterContext().getVersion();
        String cls = handleClass(artifact);
        String pkg = group.toLowerCase() + "." + cls.toLowerCase();
        String replaced = content.replaceAll("\\$GROUP\\$", group)
                .replaceAll("\\$ARTIFACT\\$", artifact)
                .replaceAll("\\$VERSION\\$", version)
                .replaceAll("\\$PACKAGE\\$", pkg)
                .replaceAll("\\$CLASS\\$", cls);
        if (replaceDot) {
            return replaced.replaceAll("\\.", "/");
        } else {
            return replaced;
        }
    }

    private String handleClass(String s) {
        return Arrays.stream(Arrays.stream(s.split("-"))
                        .map(this::upperFirst)
                        .collect(Collectors.joining())
                        .split("_"))
                .map(this::upperFirst)
                .collect(Collectors.joining());
    }

    private String upperFirst(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private Versions getVersions() {
        ConceptFrameworkVersion frameworkVersion = getConceptFrameworkVersion();
        if (frameworkVersion == null) {
            return null;
        }
        return frameworkVersion.versions;
    }

    @NotNull
    @Override
    public String getBuilderId() {
        return "concept-cloud";
    }

    @NotNull
    @Override
    public String getDefaultServerUrl() {
        return "https://raw.githubusercontent.com/Linyuzai/concept/master/concept-cloud/pluginew";
        //return "https://cdn.jsdelivr.net/gh/Linyuzai/concept/concept-cloud/pluginew";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Generate a project based on Gradle that supports both Spring Cloud and Spring Boot";
    }

    @NotNull
    @Override
    protected List<ConceptStarterLanguage> getLanguages() {
        return Collections.singletonList(ConceptStarterSettings.getJAVA_STARTER_LANGUAGE());
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
    protected List<ConceptStarterProjectType> getProjectTypes() {
        return Collections.singletonList(ConceptStarterSettings.getGRADLE_PROJECT());
    }

    @NotNull
    @Override
    protected List<String> getFilePathsToOpen() {
        return Collections.singletonList("HELP.md");
    }

    @NotNull
    @Override
    protected ConceptWebStarterServerOptions loadServerOptions(@NotNull String s) {
        String name = "starter_new.json";
        String url;
        if (s.endsWith("/")) {
            url = s + name;
        } else {
            url = s + "/" + name;
        }
        JsonObject json = loadJsonData(url, null).getAsJsonObject();
        List<ConceptWebStarterFrameworkVersion> frameworkVersions = new ArrayList<>();
        JsonArray frameworkVersionArray = json.get("frameworkVersions").getAsJsonArray();
        Map<String, ConceptFrameworkVersion> value = new LinkedHashMap<>();
        for (JsonElement frameworkVersionElement : frameworkVersionArray) {
            ConceptFrameworkVersion frameworkVersion = new ConceptFrameworkVersion();
            JsonObject frameworkVersionObject = frameworkVersionElement.getAsJsonObject();
            String id = frameworkVersionObject.get("id").getAsString();
            String title = frameworkVersionObject.get("title").getAsString();
            boolean isDefault = frameworkVersionObject.get("default").getAsBoolean();
            frameworkVersions.add(new ConceptWebStarterFrameworkVersion(id, title, isDefault));

            JsonArray intactArray = frameworkVersionObject.get("intact").getAsJsonArray();
            for (JsonElement intactElement : intactArray) {
                String intact = intactElement.getAsString();
                frameworkVersion.addIntact(intact);
            }

            JsonObject versionsObject = frameworkVersionObject.get("versions").getAsJsonObject();

            String gradle = versionsObject.get("gradle").getAsString();
            String springBoot = versionsObject.get("springBoot").getAsString();
            String springDependencyManagement = versionsObject.get("springDependencyManagement").getAsString();
            String springCloudDependencyManagement = versionsObject.get("springCloudDependencyManagement").getAsString();
            String conceptDomain = versionsObject.get("conceptDomain").getAsString();
            String conceptCloudWeb = versionsObject.get("conceptCloudWeb").getAsString();
            String myBatisPlus = versionsObject.get("myBatisPlus").getAsString();

            frameworkVersion.setVersions(gradle, springBoot, springDependencyManagement,
                    springCloudDependencyManagement, conceptDomain, conceptCloudWeb, myBatisPlus);

            JsonObject dependencies = frameworkVersionObject.get("dependencies").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : dependencies.entrySet()) {
                JsonArray array = entry.getValue().getAsJsonArray();
                Set<String> set = new LinkedHashSet<>();
                for (JsonElement element : array) {
                    set.add(element.getAsString());
                }
                frameworkVersion.addConceptDependency(entry.getKey(), set);
            }
            value.put(id, frameworkVersion);
        }
        getStarterContext().putUserData(CONCEPT_CLOUD_CFV_KEY, value);
        List<ConceptWebStarterDependencyCategory> dependencyCategories = new ArrayList<>();
        JsonArray dependencyCategoryArray = json.get("dependencyCategories").getAsJsonArray();
        for (JsonElement dependencyCategoryElement : dependencyCategoryArray) {
            JsonObject dependencyCategoryObject = dependencyCategoryElement.getAsJsonObject();
            String categoryTitle = dependencyCategoryObject.get("title").getAsString();
            JsonArray dependencyArray = dependencyCategoryObject.get("dependencies").getAsJsonArray();
            List<ConceptWebStarterDependency> dependencies = new ArrayList<>();
            for (JsonElement dependencyElement : dependencyArray) {
                JsonObject dependencyObject = dependencyElement.getAsJsonObject();
                String id = dependencyObject.get("id").getAsString();
                String dependencyTitle = dependencyObject.get("title").getAsString();
                String description = dependencyObject.get("description").getAsString();
                JsonArray linkArray = dependencyObject.get("links").getAsJsonArray();
                List<ConceptLibraryLink> links = new ArrayList<>();
                for (JsonElement linkElement : linkArray) {
                    JsonObject linkObject = linkElement.getAsJsonObject();
                    String linkUrl = linkObject.get("url").getAsString();
                    String linkTitle = linkObject.get("title").getAsString();
                    links.add(new ConceptLibraryLink(linkUrl, linkTitle));
                }
                boolean isDefault = dependencyObject.get("default").getAsBoolean();
                boolean isRequired = dependencyObject.get("required").getAsBoolean();
                dependencies.add(new ConceptWebStarterDependency(id, dependencyTitle, description, links, isDefault, isRequired));
            }
            dependencyCategories.add(new ConceptWebStarterDependencyCategory(categoryTitle, dependencies));
        }
        return new ConceptWebStarterServerOptions(frameworkVersions, dependencyCategories);
    }

    public static class ConceptFrameworkVersion {

        Set<String> intact = new HashSet<>();

        Versions versions = new Versions();

        ConceptDependencies conceptDependencies = new ConceptDependencies();

        void addIntact(String intact) {
            this.intact.add(intact);
        }

        void setVersions(String gradle,
                         String springBoot,
                         String springDependencyManagement,
                         String springCloudDependencyManagement,
                         String conceptDomain,
                         String conceptCloudWeb,
                         String myBatisPlus) {
            versions.gradle = gradle;
            versions.springBoot = springBoot;
            versions.springDependencyManagement = springDependencyManagement;
            versions.springCloudDependencyManagement = springCloudDependencyManagement;
            versions.conceptDomain = conceptDomain;
            versions.conceptCloudWeb = conceptCloudWeb;
            versions.myBatisPlus = myBatisPlus;
        }

        void addConceptDependency(String id, Collection<String> dependencies) {
            conceptDependencies.mapping.put(id, dependencies);
        }

        Collection<String> getConceptDependency(String id) {
            return conceptDependencies.mapping.get(id);
        }
    }

    public static class Versions {

        String gradle;

        String springBoot;

        String springDependencyManagement;

        String springCloudDependencyManagement;

        String conceptDomain;

        String conceptCloudWeb;

        String myBatisPlus;
    }

    public static class ConceptDependencies {

        Map<String, Collection<String>> mapping = new LinkedHashMap<>();
    }
}
