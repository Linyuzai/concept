package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.jetbrains.annotations.NotNull;

public class ConceptCloudGroup extends DefaultActionGroup {

    public ConceptCloudGroup() {
        addSeparator();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(ConceptCloudUtils.isSupport(e));
    }
}
