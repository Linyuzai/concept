package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.mock.MockDomainContext;
import com.github.linyuzai.domain.core.proxy.ProxyDomainFactory;

import java.util.Arrays;
import java.util.Collections;

public class DomainTest {

    public void test() {
        Class type = new UserRepositoryImpl().getType();
        System.out.println(type);

        UserRepository repository = new UserRepositoryImpl();
        DomainContext context = new MockDomainContext(UserRepository.class, repository);
        DomainFactory factory = new ProxyDomainFactory(context);

        Users select = repository.select(Arrays.asList("1", "2"));
        select.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User user = factory.create(User.class, "1");
        System.out.println(user.getId());

        Users wrap = factory.create(Users.class, Collections.singleton(user));
        wrap.list().stream().map(Identifiable::getId).forEach(System.out::println);

        Users users = factory.create(Users.class);
        users.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User schrodingerUser = new SchrodingerUser("4", context);
        System.out.println(schrodingerUser.getId());

        Users schrodingerUsers = new SchrodingerUsers(context);
        schrodingerUsers.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User u = factory.create(User.class, "3", factory.create(Users.class));
        u.load();
        System.out.println(u.getName());
    }
}
