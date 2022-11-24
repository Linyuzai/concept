package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.lifecycle.Lifecycle;

import java.util.Map;

public interface Extension extends Lifecycle {

    String getId();

    String getName();

    String getType();

    Map<String, String> getMetadata();

    boolean isAutoInitialize();

    void setAutoInitialize(boolean autoInitialize);

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

        boolean isSuccess();

        String getCode();

        String getMessage();

        Object getObject();
    }

    interface ArgumentAndResult {

        Extension getExtension();

        Argument getArgument();

        Result getResult();
    }

}
