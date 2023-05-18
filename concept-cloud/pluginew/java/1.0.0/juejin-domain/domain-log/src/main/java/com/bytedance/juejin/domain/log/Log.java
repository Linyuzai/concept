package com.bytedance.juejin.domain.log;

import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainEntity;

public interface Log extends DomainEntity {

    Class<?> getModule();

    Type getType();

    User getUser();

    String getContent();

    enum Type {

        CREATE, UPDATE, DELETE
    }
}
