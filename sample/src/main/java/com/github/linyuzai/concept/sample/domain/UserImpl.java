package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserImpl implements User {

    private String id;

    @Override
    public DomainContext getContext() {
        return null;
    }

    @Override
    public Conditions getConditions() {
        return null;
    }

    @Override
    public DomainRepository<User, ?> getRepository() {
        return null;
    }

    @Override
    public Object getExtra() {
        return null;
    }

    @Override
    public void setExtra(Object extra) {

    }
}
