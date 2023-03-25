package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainObject;
import lombok.NonNull;

public class SchrodingerUser extends SchrodingerDomainObject<User> implements User{

    public SchrodingerUser(@NonNull String id, @NonNull DomainContext context) {
        super(id, context);
    }

    @Override
    public String getName() {
        return getTarget().getName();
    }
}
