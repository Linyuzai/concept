package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.dependence.DependenceProvider;
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

    private Map<Object, Object> metadata;

    private DependenceProvider dependenceProvider;

    private ExtensionConcept concept;
}
