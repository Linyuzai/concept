package com.github.linyuzai.cloud.plugin.intellij;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import com.intellij.ide.starters.remote.*;
import com.intellij.ide.starters.shared.CustomizedMessages;
import com.intellij.ide.starters.shared.StarterLanguage;
import com.intellij.ide.starters.shared.StarterProjectType;
import com.intellij.ide.starters.shared.StarterSettings;
import com.intellij.util.Url;
import com.intellij.util.Urls;
import com.intellij.util.io.HttpRequests;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConceptCloudWebModuleBuilder extends WebStarterModuleBuilder {

    @NotNull
    @Override
    protected Url composeGeneratorUrl(@NotNull String s, @NotNull WebStarterContext webStarterContext) {
        return Urls.newUrl("https", "", "cdn.jsdelivr.net/gh/Linyuzai/concept/concept_version.properties");
    }

    @Override
    protected void extractGeneratorResult(@NotNull File file, @NotNull File file1) {

        System.out.println("extractGeneratorResult");
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
    protected WebStarterServerOptions loadServerOptions(@NotNull String s) {
        String url;
        if (s.endsWith("/")) {
            url = s + "starter.json";
        } else {
            url = s + "/starter.json";
        }
        JsonElement json = loadJsonData(url, null);
        JsonObject object = json.getAsJsonObject();
        List<WebStarterFrameworkVersion> frameworkVersions = new ArrayList<>();
        JsonArray cloudArray = object.get("cloud").getAsJsonArray();
        for (JsonElement e : cloudArray) {
            JsonObject cloudObject = e.getAsJsonObject();
            String cloudVersion = cloudObject.get("version").getAsString();
            frameworkVersions.add(new WebStarterFrameworkVersion(cloudVersion, "Concept Cloud v" + cloudVersion, false));
        }
        List<WebStarterDependencyCategory> dependencyCategories = new ArrayList<>();
        JsonObject dependencyObject = object.get("dependency").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : dependencyObject.entrySet()) {
            JsonArray dependencyArray = entry.getValue().getAsJsonArray();
            List<WebStarterDependency> dependencies = new ArrayList<>();
            for (JsonElement dependency : dependencyArray) {
                String string = dependency.getAsString();
                dependencies.add(new WebStarterDependency(string, string, null, Collections.emptyList(), false, false));
            }
            dependencyCategories.add(new WebStarterDependencyCategory(entry.getKey(), dependencies));
        }
        return new WebStarterServerOptions(frameworkVersions, dependencyCategories);
    }
}
