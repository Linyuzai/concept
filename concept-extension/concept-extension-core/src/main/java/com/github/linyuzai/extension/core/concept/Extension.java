package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.dependence.DependenceProvider;
import com.github.linyuzai.extension.core.lifecycle.Lifecycle;

import java.util.Map;

public interface Extension extends Lifecycle {

    String getId();

    String getName();

    String getType();

    Map<Object, Object> getMetadata();

    DependenceProvider getDependenceProvider();

    void setDependenceProvider(DependenceProvider resolver);

    ExtensionConcept getConcept();

    void setConcept(ExtensionConcept concept);

    Result extend(Argument argument);

    interface Argument {

        Object getTarget();

        String getKey();

        Object getValue();

        <T> T getConfig(Object o);

        <T> T getConfig(Object o, T defaultValue);
    }

    interface Result {

        String getCode();

        String getMessage();

        Object getObject();
    }

    interface ArgumentAndResult {

        Argument getArgument();

        Result getResult();
    }

}
