package com.github.linyuzai.domain.core.link;

import com.github.linyuzai.domain.core.DomainRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainRepositoryLink {

    Class<? extends DomainRepository<?>> value();
}
