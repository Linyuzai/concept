package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ConceptCloudModuleType extends ModuleType<ConceptCloudModuleBuilder> {

    public static final ConceptCloudModuleType INSTANCE = new ConceptCloudModuleType();

    protected ConceptCloudModuleType() {
        super("concept.cloud.module.type");
    }

    @NotNull
    @Override
    public ConceptCloudModuleBuilder createModuleBuilder() {
        return new ConceptCloudModuleBuilder();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @NotNull
    @Override
    public String getName() {
        return "Concept Cloud";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getDescription() {
        return "Generate a project or module to support Spring Cloud and Spring Boot both";
    }

    @NotNull
    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return AllIcons.General.Balloon;
    }
}
