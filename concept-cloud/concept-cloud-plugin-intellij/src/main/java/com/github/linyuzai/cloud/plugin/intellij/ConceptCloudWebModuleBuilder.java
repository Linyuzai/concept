package com.github.linyuzai.cloud.plugin.intellij;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import com.intellij.ide.starters.remote.*;
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
public class ConceptCloudWebModuleBuilder extends WebStarterModuleBuilder {

    private final Key<Map<String, Map<String, Set<String>>>> CONECPT_DEPENDENCY_KEY = new Key<>("CONECPT_DEPENDENCY_KEY");

    @NotNull
    @Override
    protected Url composeGeneratorUrl(@NotNull String s, @NotNull WebStarterContext webStarterContext) {
        String url;
        String path = s.replace("https://", "").replace("http://", "");
        if (path.endsWith("/")) {
            url = path + "java.zip";
        } else {
            url = path + "/java.zip";
        }
        return Urls.newUrl("https", "", url);
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
                if (needHandleAfterSpecialCheck(create)) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(file), StandardCharsets.UTF_8))) {
                        String collect = br.lines().collect(Collectors.joining("\n"));
                        FileUtil.writeToFile(create, transform(collect, false));
                    }
                }
            }
        } catch (Throwable e) {
            Messages.showErrorDialog("Unzip error: " + e.getMessage(), getPresentableName());
        }
        //ZipUtil.extract();
    }

    private boolean needHandleAfterSpecialCheck(File file) throws IOException {
        if (!"concept.gradle".equals(file.getName())) {
            return true;
        }
        Map<String, Map<String, Set<String>>> value = getStarterContext().getUserData(CONECPT_DEPENDENCY_KEY);
        if (value == null) {
            return false;
        }
        WebStarterFrameworkVersion frameworkVersion = getStarterContext().getFrameworkVersion();
        if (frameworkVersion == null) {
            return false;
        }
        String frameworkVersionId = frameworkVersion.getId();
        Map<String, Set<String>> map = value.get(frameworkVersionId);
        if (map == null) {
            return false;
        }
        Set<WebStarterDependency> dependencies = getStarterContext().getDependencies();
        StringBuilder builder = new StringBuilder();
        builder.append("dependencies {\n");
        for (WebStarterDependency dependency : dependencies) {
            Set<String> set = map.get(dependency.getId());
            if (set != null) {
                for (String s : set) {
                    builder.append("\t").append(s).append("\n");
                }
            }
        }
        builder.append("}");
        FileUtil.writeToFile(file, builder.toString());
        return false;
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
        String pkg = group.toLowerCase() + "." + artifact.toLowerCase();
        String cls = artifact.substring(0, 1).toUpperCase() + artifact.substring(1);
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

    @NotNull
    @Override
    public String getBuilderId() {
        return "concept-cloud";
    }

    @NotNull
    @Override
    public String getDefaultServerUrl() {
        return "https://raw.githubusercontent.com/Linyuzai/concept/master/concept-cloud/plugin";
        //return "https://cdn.jsdelivr.net/gh/Linyuzai/concept/concept-cloud/plugin";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Generate a project based on Gradle that supports both Spring Cloud and Spring Boot";
    }

    @NotNull
    @Override
    protected List<StarterLanguage> getLanguages() {
        return Collections.singletonList(StarterSettings.getJAVA_STARTER_LANGUAGE());
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
    protected List<StarterProjectType> getProjectTypes() {
        return Collections.singletonList(StarterSettings.getGRADLE_PROJECT());
    }

    @NotNull
    @Override
    protected List<String> getFilePathsToOpen() {
        return Collections.singletonList("README.md");
    }

    @NotNull
    @Override
    protected WebStarterServerOptions loadServerOptions(@NotNull String s) {
        String url;
        if (s.endsWith("/")) {
            url = s + "starter.json";
        } else {
            url = s + "/starter.json";
        }
        JsonObject json = loadJsonData(url, null).getAsJsonObject();
        List<WebStarterFrameworkVersion> frameworkVersions = new ArrayList<>();
        JsonArray frameworkVersionArray = json.get("frameworkVersions").getAsJsonArray();
        Map<String, Map<String, Set<String>>> value = new LinkedHashMap<>();
        for (JsonElement frameworkVersionElement : frameworkVersionArray) {
            JsonObject frameworkVersionObject = frameworkVersionElement.getAsJsonObject();
            String id = frameworkVersionObject.get("id").getAsString();
            String title = frameworkVersionObject.get("title").getAsString();
            boolean isDefault = frameworkVersionObject.get("default").getAsBoolean();
            frameworkVersions.add(new WebStarterFrameworkVersion(id, title, isDefault));

            Map<String, Set<String>> map = new LinkedHashMap<>();
            JsonObject dependencies = frameworkVersionObject.get("dependencies").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : dependencies.entrySet()) {
                JsonArray array = entry.getValue().getAsJsonArray();
                Set<String> set = new LinkedHashSet<>();
                for (JsonElement element : array) {
                    set.add(element.getAsString());
                }
                map.put(entry.getKey(), set);
            }
            value.put(id, map);
        }
        getStarterContext().putUserData(CONECPT_DEPENDENCY_KEY, value);
        List<WebStarterDependencyCategory> dependencyCategories = new ArrayList<>();
        JsonArray dependencyCategoryArray = json.get("dependencyCategories").getAsJsonArray();
        for (JsonElement dependencyCategoryElement : dependencyCategoryArray) {
            JsonObject dependencyCategoryObject = dependencyCategoryElement.getAsJsonObject();
            String categoryTitle = dependencyCategoryObject.get("title").getAsString();
            JsonArray dependencyArray = dependencyCategoryObject.get("dependencies").getAsJsonArray();
            List<WebStarterDependency> dependencies = new ArrayList<>();
            for (JsonElement dependencyElement : dependencyArray) {
                JsonObject dependencyObject = dependencyElement.getAsJsonObject();
                String id = dependencyObject.get("id").getAsString();
                String dependencyTitle = dependencyObject.get("title").getAsString();
                String description = dependencyObject.get("description").getAsString();
                JsonArray linkArray = dependencyObject.get("links").getAsJsonArray();
                List<LibraryLink> links = new ArrayList<>();
                for (JsonElement linkElement : linkArray) {
                    JsonObject linkObject = linkElement.getAsJsonObject();
                    String type = linkObject.get("type").getAsString();
                    String linkUrl = linkObject.get("url").getAsString();
                    String linkTitle = linkObject.get("title").getAsString();
                    links.add(new LibraryLink(LibraryLinkType.valueOf(type), linkUrl, linkTitle));
                }
                boolean isDefault = dependencyObject.get("default").getAsBoolean();
                boolean isRequired = dependencyObject.get("required").getAsBoolean();
                dependencies.add(new WebStarterDependency(id, dependencyTitle, description, links, isDefault, isRequired));
            }
            dependencyCategories.add(new WebStarterDependencyCategory(categoryTitle, dependencies));
        }
        return new WebStarterServerOptions(frameworkVersions, dependencyCategories);
    }
}
