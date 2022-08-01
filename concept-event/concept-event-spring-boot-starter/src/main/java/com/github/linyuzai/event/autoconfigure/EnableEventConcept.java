package com.github.linyuzai.event.autoconfigure;

import com.github.linyuzai.event.autoconfigure.bus.EventBusConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EventEnabled.class, EventConfiguration.class, EventBusConfiguration.class})
public @interface EnableEventConcept {
}
