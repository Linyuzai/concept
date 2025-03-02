package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PluginConceptInitializedEvent {

    private final PluginConcept concept;
}
