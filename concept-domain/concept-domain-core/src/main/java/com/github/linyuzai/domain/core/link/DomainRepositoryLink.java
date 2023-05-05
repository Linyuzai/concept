package com.github.linyuzai.domain.core.link;

import com.github.linyuzai.domain.core.DomainRepository;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainRepositoryLink {

    Class<? extends DomainRepository<?, ?>> value();
}
