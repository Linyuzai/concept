package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.mock.MockDomainContext;

import java.util.Arrays;
import java.util.Collections;

public class DomainTest {

    public void test() {
        UserRepository repository = new UserRepositoryImpl();
        DomainContext context = new MockDomainContext(UserRepository.class, repository);

        Users select = repository.select(Arrays.asList("1", "2"));
        System.out.println(select);

        User user = DomainObject.schrodinger(User.class, "1", context);
        System.out.println(user.getName());

        Users wrap = DomainCollection.wrap(Users.class, Collections.singleton(user));
        System.out.println(wrap);

        Users users = DomainCollection.schrodinger(Users.class, context);
        System.out.println(users.list());
    }
}
