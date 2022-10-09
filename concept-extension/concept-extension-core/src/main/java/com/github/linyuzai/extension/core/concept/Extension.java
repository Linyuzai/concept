package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.lifecycle.Lifecycle;

import java.util.Map;

public interface Extension extends Lifecycle {

    String getId();

    String getName();

    String getType();

    Map<Object, Object> getMetadata();

    ExtensionConcept getConcept();

    void setConcept(ExtensionConcept concept);

    Result extend(Argument argument);

    interface Argument {

        Object getTarget();

        String getKey();

        Object getValue();

        Map<Object, Object> getConfigs();
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
