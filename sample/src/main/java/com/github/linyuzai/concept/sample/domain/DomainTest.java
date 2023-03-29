package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import com.github.linyuzai.domain.core.mock.MockDomainContext;
import com.github.linyuzai.domain.core.proxy.ProxyDomainFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

public class DomainTest {

    public void test() {
        Class type = new UserRepositoryImpl().getType();
        System.out.println(type);

        UserRepository repository = new UserRepositoryImpl();
        DomainContext context = new MockDomainContext(UserRepository.class, repository);
        DomainFactory factory = new ProxyDomainFactory(context);

        /*Users select = repository.select(Arrays.asList("1", "2"));
        select.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User user = factory.createObject(User.class, "1");
        System.out.println(user.getId());

        Users wrap = factory.createCollection(Users.class, Collections.singleton(user));
        wrap.list().stream().map(Identifiable::getId).forEach(System.out::println);

        Users users = factory.createCollection(Users.class);
        users.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User schrodingerUser = new SchrodingerUser("4", context);
        System.out.println(schrodingerUser.getId());

        Users schrodingerUsers = new SchrodingerUsers(context);
        schrodingerUsers.list().stream().map(Identifiable::getId).forEach(System.out::println);
*/
        User u = factory.createObject(User.class, factory.createCollection(Users.class), "doSelect1");
        System.out.println(u.getName());

        Users us = factory.createCollection(Users.class, factory.createCollection(Users.class), Arrays.asList("doSelect1","doSelect2"));
        us.list().stream().map(Identifiable::getId).forEach(System.out::println);
    }
}
