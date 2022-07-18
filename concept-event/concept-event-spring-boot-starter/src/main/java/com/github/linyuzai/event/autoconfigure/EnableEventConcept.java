package com.github.linyuzai.event.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EventEnabled.class, EventConfiguration.class})
public @interface EnableEventConcept {
}
