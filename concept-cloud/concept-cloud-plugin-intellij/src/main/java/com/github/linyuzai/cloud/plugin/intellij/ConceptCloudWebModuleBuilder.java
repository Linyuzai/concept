package com.github.linyuzai.cloud.plugin.intellij;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import com.intellij.ide.starters.shared.*;
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

    private final Key<Map<String, ConceptFrameworkVersion>> CONCEPT_CLOUD_FVE_KEY = new Key<>("CONCEPT_CLOUD_FVE_KEY");

    @NotNull
    @Override
    protected Url composeGeneratorUrl(@NotNull String s, @NotNull ConceptWebStarterContext webStarterContext) {
        String version;
        ConceptWebStarterFrameworkVersion frameworkVersion = webStarterContext.getFrameworkVersion();
        if (frameworkVersion == null) {
            version = "";
        } else {
            version = "_" + frameworkVersion.getId();
        }
        String url;
        if (s.endsWith("/")) {
            url = s + "java" + version + ".zip";
        } else {
            url = s + "/java" + version + ".zip";
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
                if ("concept.gradle".equals(file.getName())) {
                    handleConceptGradle(create);
                }
                try (BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(file), StandardCharsets.UTF_8))) {
                    String collect = br.lines().collect(Collectors.joining("\n"));
                    String transform = transform(collect, false);
                    String content;
                    if ("build.gradle".equals(file.getName())) {
                        content = handleSpringVersion(transform);
                    } else {
                        content = transform;
                    }
                    FileUtil.writeToFile(create, content);
                }
            }
        } catch (Throwable e) {
            Messages.showErrorDialog("Unzip error: " + e.getMessage(), getPresentableName());
        }
        //ZipUtil.extract();
    }

    private void handleConceptGradle(File file) throws IOException {
        ConceptFrameworkVersion conceptFrameworkVersion = getFrameworkVersionEx();
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

    private String handleSpringVersion(String content) {
        String springBoot;
        String springDependencyManagement;
        String springCloudDependencyManagement;
        SpringVersion springVersion = getSpringVersion();
        if (springVersion == null) {
            springBoot = "2.7.6";
            springDependencyManagement = "1.1.0";
            springCloudDependencyManagement = "2021.0.4";
        } else {
            springBoot = springVersion.springBoot;
            springDependencyManagement = springVersion.springDependencyManagement;
            springCloudDependencyManagement = springVersion.springCloudDependencyManagement;
        }
        return content.replaceAll("\\$V_SPRING_BOOT\\$", springBoot)
                .replaceAll("\\$V_SPRING_DM\\$", springDependencyManagement)
                .replaceAll("\\$V_SPRING_CLOUD_DM\\$", springCloudDependencyManagement);
    }

    private ConceptFrameworkVersion getFrameworkVersionEx() {
        Map<String, ConceptFrameworkVersion> data = getStarterContext().getUserData(CONCEPT_CLOUD_FVE_KEY);
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

    private SpringVersion getSpringVersion() {
        ConceptFrameworkVersion frameworkVersion = getFrameworkVersionEx();
        if (frameworkVersion == null) {
            return null;
        }
        return frameworkVersion.springVersion;
    }

    @NotNull
    @Override
    public String getBuilderId() {
        return "concept-cloud";
    }

    @NotNull
    @Override
    public String getDefaultServerUrl() {
        //return "https://raw.githubusercontent.com/Linyuzai/concept/master/concept-cloud/plugin";
        return "https://cdn.jsdelivr.net/gh/Linyuzai/concept/concept-cloud/plugin";
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
        String url;
        if (s.endsWith("/")) {
            url = s + "starter_v2.json";
        } else {
            url = s + "/starter_v2.json";
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

            if (frameworkVersionObject.has("springVersions")) {
                JsonObject springVersionsObject = frameworkVersionObject.get("springVersions").getAsJsonObject();
                String springBoot = springVersionsObject.get("springBoot").getAsString();
                String springDependencyManagement = springVersionsObject.get("springDependencyManagement").getAsString();
                String springCloudDependencyManagement = springVersionsObject.get("springCloudDependencyManagement").getAsString();
                frameworkVersion.setSpringVersion(springBoot, springDependencyManagement, springCloudDependencyManagement);
            }

            Map<String, Set<String>> map = new LinkedHashMap<>();
            JsonObject dependencies = frameworkVersionObject.get("dependencies").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : dependencies.entrySet()) {
                JsonArray array = entry.getValue().getAsJsonArray();
                Set<String> set = new LinkedHashSet<>();
                for (JsonElement element : array) {
                    set.add(element.getAsString());
                }
                map.put(entry.getKey(), set);
                frameworkVersion.addConceptDependency(id, set);
            }
            value.put(id, frameworkVersion);
        }
        getStarterContext().putUserData(CONCEPT_CLOUD_FVE_KEY, value);
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
                    String type = linkObject.get("type").getAsString();
                    String linkUrl = linkObject.get("url").getAsString();
                    String linkTitle = linkObject.get("title").getAsString();
                    links.add(new ConceptLibraryLink(LibraryLinkType.valueOf(type), linkUrl, linkTitle));
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

        SpringVersion springVersion = new SpringVersion();

        ConceptDependency conceptDependency = new ConceptDependency();

        void setSpringVersion(String springBoot, String springDependencyManagement, String springCloudDependencyManagement) {
            springVersion.springBoot = springBoot;
            springVersion.springDependencyManagement = springDependencyManagement;
            springVersion.springCloudDependencyManagement = springCloudDependencyManagement;
        }

        void addConceptDependency(String id, Collection<String> dependencies) {
            conceptDependency.mapping.put(id, dependencies);
        }

        Collection<String> getConceptDependency(String id) {
            return conceptDependency.mapping.get(id);
        }
    }

    public static class SpringVersion {

        String springBoot;

        String springDependencyManagement;

        String springCloudDependencyManagement;

    }

    public static class ConceptDependency {

        Map<String, Collection<String>> mapping = new LinkedHashMap<>();
    }
}
