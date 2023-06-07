package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.mock.MockDomainContext;
import com.github.linyuzai.domain.core.proxy.ProxyDomainFactory;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;

import java.util.*;

public class DomainTest {

    public DomainFactory getDomainFactory() {
        UserRepository repository = new UserRepositoryImpl();
        DomainContext context = new MockDomainContext(UserRepository.class, repository);
        return new ProxyDomainFactory(context);
    }

    public void testIdGenerator() {
        UserIdGenerator idGenerator = MBPDomainIdGenerator.create(UserIdGenerator.class);
        String id = idGenerator.generateId(null);
        System.out.println(id);
        System.out.println(idGenerator.toString());
        System.out.println(idGenerator.equals(idGenerator));
        System.out.println(idGenerator.hashCode());
    }

    public void test() {
        testFactory10();
    }

    public void testFactory1() {
        DomainFactory factory = getDomainFactory();
        User user = factory.createObject(User.class, "1");
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory2() {
        DomainFactory factory = getDomainFactory();
        User user = factory.createObject(User.class, new Conditions().equal("id", "2"));
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory3() {
        DomainFactory factory = getDomainFactory();
        Users2 users = factory.createCollection(Users2.class, Arrays.asList("3", "_3"));
        User user = factory.createObject(User.class, users, "3");
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory4() {
        DomainFactory factory = getDomainFactory();
        Users2 users = factory.createCollection(Users2.class, Arrays.asList("4", "_4"));
        User user = factory.createObject(User.class, users, u -> u.getId().equals("4"));
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory5() {
        DomainFactory factory = getDomainFactory();
        Map<String, String> idMapping = new LinkedHashMap<>();
        idMapping.put("5", "5");
        idMapping.put("_5", "_5");
        Map<String, User> map = factory.createObject(User.class, Users2.class,
                Arrays.asList("5", "_5"), idMapping);
        for (Map.Entry<String, User> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            User user = entry.getValue();
            System.out.println(user.getId());
            user.test0();
            System.out.println("===============");
        }
    }

    public void testFactory6() {
        DomainFactory factory = getDomainFactory();
        Users2 users = factory.createCollection(Users2.class, Arrays.asList("6", "_6"));
        for (User user : users.list()) {
            System.out.println(user.getId());
            user.test0();
            System.out.println("===============");
        }
        users.test1();
        users.test2();
    }

    public void testFactory7() {
        DomainFactory factory = getDomainFactory();
        Users2 users = factory.createCollection(Users2.class, new Conditions()
                .in("id", Arrays.asList("7", "_7")));
        for (User user : users.list()) {
            System.out.println(user.getId());
            user.test0();
            System.out.println("===============");
        }
        users.test1();
        users.test2();
    }

    public void testFactory8() {
        DomainFactory factory = getDomainFactory();
        Users2 us = factory.createCollection(Users2.class, Arrays.asList("8", "_8"));
        Users2 users = factory.createCollection(Users2.class, us, Arrays.asList("8"));
        for (User user : users.list()) {
            System.out.println(user.getId());
            user.test0();
            System.out.println("===============");
        }
        users.test1();
        users.test2();
    }

    public void testFactory9() {
        DomainFactory factory = getDomainFactory();
        Users2 us = factory.createCollection(Users2.class, Arrays.asList("9", "_9"));
        Users2 users = factory.createCollection(Users2.class, us, u -> u.getId().equals("9"));
        for (User user : users.list()) {
            System.out.println(user.getId());
            user.test0();
            System.out.println("===============");
        }
        users.test1();
        users.test2();
    }

    public void testFactory10() {
        DomainFactory factory = getDomainFactory();
        Map<String, List<String>> idsMapping = new LinkedHashMap<>();
        idsMapping.put("10", Arrays.asList("10"));
        idsMapping.put("_10", Arrays.asList("_10"));
        Map<String, Users2> map = factory.createCollection(User.class, Users2.class,
                Arrays.asList("10", "_10"), idsMapping);
        for (Map.Entry<String, Users2> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            Users2 users = entry.getValue();
            for (User user : users.list()) {
                System.out.println(user.getId());
                user.test0();
                System.out.println("===============");
            }
            users.test1();
            users.test2();
            System.out.println("===============");
        }
    }
}
