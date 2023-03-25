package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainCollection;
import lombok.NonNull;

public class SchrodingerUsers extends SchrodingerDomainCollection<User> implements Users {

    public SchrodingerUsers(@NonNull DomainContext context) {
        super(context);
    }
}
