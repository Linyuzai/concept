package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.mock.MockDomainContext;
import com.github.linyuzai.domain.core.proxy.ProxyDomainFactory;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;

import java.util.Arrays;

public class DomainTest {

    public void testIdGenerator() {
        UserIdGenerator idGenerator = MBPDomainIdGenerator.create(UserIdGenerator.class);
        String id = idGenerator.generateId(null);
        System.out.println(id);
        System.out.println(idGenerator.toString());
        System.out.println(idGenerator.equals(idGenerator));
        System.out.println(idGenerator.hashCode());
    }

    public void test() {
        UserRepository repository = new UserRepositoryImpl();
        DomainContext context = new MockDomainContext(UserRepository.class, repository);
        DomainFactory factory = new ProxyDomainFactory(context);

        //Users select = repository.select(Arrays.asList("1", "2"));
        //select.list().stream().map(Identifiable::getId).forEach(System.out::println);

        /*User user = factory.createObject(User.class, "1");
        System.out.println(user.getId());*/

        //Users wrap = factory.createCollection(Users.class, Collections.singleton(user));
        //wrap.list().stream().map(Identifiable::getId).forEach(System.out::println);

        //Users users = factory.createCollection(Users.class);
        //users.list().stream().map(Identifiable::getId).forEach(System.out::println);

        //User schrodingerUser = new SchrodingerUser("4", context);
        //System.out.println(schrodingerUser.getId());

        //Users schrodingerUsers = new SchrodingerUsers(context);
        //schrodingerUsers.list().stream().map(Identifiable::getId).forEach(System.out::println);

        //User u = factory.createObject(User.class, factory.createCollection(Users.class), "doSelect1");
        //System.out.println(u.getName());

        //Users us = factory.createCollection(Users.class, factory.createCollection(Users.class), Arrays.asList("doSelect1", "doSelect2"));
        //us.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User user0 = factory.createObject(User.class, "0");
        System.out.println("user0: " + user0.getId());
        user0.test0();
        System.out.println();

        User user1 = factory.createObject(User.class, factory.createCollection(Users2.class), "0");
        System.out.println("user1: " + user1.getId());
        user1.test0();
        System.out.println();

        Users2 users0 = factory.createCollection(Users2.class, Arrays.asList(new UserImpl("0"), new UserImpl("1")));
        users0.stream().forEach(it -> System.out.println("users0: " + it.getId()));
        users0.test1();
        users0.test2();
        System.out.println();

        Users2 users1 = factory.createCollection(Users2.class);
        users1.stream().forEach(it -> System.out.println("users1: " + it.getId()));
        users1.test1();
        users1.test2();
        System.out.println();

        Users2 users2 = factory.createCollection(Users2.class, factory.createCollection(Users2.class), Arrays.asList("0", "1"));
        users2.stream().forEach(it -> System.out.println("users2: " + it.getId()));
        users2.test1();
        users2.test2();
        System.out.println();

        Users<User> select = repository.select(new Conditions());
        select.test1();
        System.out.println("=============");
        context.aware(select);
        select.test1();
    }
}
