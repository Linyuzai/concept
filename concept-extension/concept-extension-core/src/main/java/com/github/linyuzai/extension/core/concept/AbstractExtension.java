package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.lifecycle.AbstractLifecycle;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class AbstractExtension extends AbstractLifecycle implements Extension {

    private String id;

    private String name;

    private String type;

    private Map<String, String> metadata;

    private boolean autoInitialize = true;

    private ExtensionConcept concept;
}
