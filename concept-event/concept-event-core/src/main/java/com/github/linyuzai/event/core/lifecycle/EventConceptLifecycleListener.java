package com.github.linyuzai.event.core.lifecycle;

import com.github.linyuzai.event.core.concept.EventConcept;

public interface EventConceptLifecycleListener {

    void onInitialize(EventConcept concept);

    void onDestroy(EventConcept concept);
}
