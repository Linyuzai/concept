package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainEntity;
import com.github.linyuzai.domain.core.link.DomainRepositoryLink;

//@DomainRepositoryLink(UserRepository.class)
public interface User extends DomainEntity {

    String getName();
}
