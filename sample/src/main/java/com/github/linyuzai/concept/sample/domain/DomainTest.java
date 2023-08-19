package com.github.linyuzai.concept.sample.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import com.github.linyuzai.domain.core.mock.MockDomainContext;
import com.github.linyuzai.domain.core.proxy.ProxyDomainFactory;
import com.github.linyuzai.domain.core.recycler.DomainRecycler;
import com.github.linyuzai.domain.core.recycler.LinkedDomainRecycler;
import com.github.linyuzai.domain.core.recycler.ThreadLocalDomainRecycler;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;
import lombok.SneakyThrows;

import java.util.*;

public class DomainTest {

    public DomainFactory getDomainFactory() {
        UserRepository repository = new UserRepositoryImpl();
        DomainContext context = new MockDomainContext(UserRepository.class, repository);
        DomainRecycler recycler = new ThreadLocalDomainRecycler(new LinkedDomainRecycler());
        return new ProxyDomainFactory(context, recycler);
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
        testRecycler();
        //testFactory10(getDomainFactory());
    }

    public void doSomething(Object o) {

    }

    public void testRecycler() {
        DomainFactory factory = getDomainFactory();
        ThreadLocalDomainRecycler recycler = (ThreadLocalDomainRecycler) ((ProxyDomainFactory) factory).getRecycler();
        long start = System.currentTimeMillis();
        Set<Users2> users2s = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            Map<String, Users2> map = factory.createCollection(Users2.class, Arrays.asList("1", "2", "3"), ids -> {
                System.out.println("mapping");
                Map<String, List<String>> idsMapping = new LinkedHashMap<>();
                idsMapping.put("1", Arrays.asList("1","2"));
                idsMapping.put("2", Arrays.asList("2","3"));
                idsMapping.put("3", Arrays.asList("3","1"));
                return idsMapping;
            });
            users2s.addAll(map.values());
            recycler.recycle();
        }
        long span = System.currentTimeMillis() - start;
        System.out.println("!!!!!!!!!!!!!!!!");
        System.out.println(span);
        System.out.println(users2s.size());
        System.out.println("!!!!!!!!!!!!!!!!");
    }

    @SneakyThrows
    public void testConditions() {
        ObjectMapper mapper = new ObjectMapper();
        Conditions conditions = new Conditions()
                .equal("a", "1")
                .isNull("b")
                .in("c", Arrays.asList("2", "3"))
                .like("d", "4")
                .orderBy("e")
                .limit(2);
        System.out.println(conditions);
        String s = mapper.writeValueAsString(conditions);
        System.out.println(s);
        Conditions value = mapper.readValue(s, LambdaConditions.class);
        System.out.println(value);
    }

    public void testFactory1(DomainFactory factory) {
        User user = factory.createObject(User.class, "1");
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory2(DomainFactory factory) {
        User user = factory.createObject(User.class, new Conditions().equal("id", "2"));
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory3(DomainFactory factory) {
        Users2 users = factory.createCollection(Users2.class, Arrays.asList("3", "_3"));
        User user = factory.createObject(User.class, users, "3");
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory4(DomainFactory factory) {
        Users2 users = factory.createCollection(Users2.class, Arrays.asList("4", "_4"));
        User user = factory.createObject(User.class, users, u -> u.getId().equals("4"));
        System.out.println(user.getId());
        user.test0();
    }

    public void testFactory5(DomainFactory factory) {
        Map<String, User> map = factory.createObject(Users2.class, Arrays.asList("5", "_5"), ids -> {
            System.out.println("mapping");
            Map<String, String> idMapping = new LinkedHashMap<>();
            idMapping.put("5", "5");
            idMapping.put("_5", "_5");
            return idMapping;
        });
        for (Map.Entry<String, User> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            User user = entry.getValue();
            System.out.println(user.getId());
            user.test0();
            System.out.println("===============");
        }
    }

    public void testFactory6(DomainFactory factory) {
        Users2 users = factory.createCollection(Users2.class, Arrays.asList("6", "_6"));
        for (User user : users.list()) {
            System.out.println(user.getId());
            user.test0();
            System.out.println("===============");
        }
        users.test1();
        users.test2();
    }

    public void testFactory7(DomainFactory factory) {
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

    public void testFactory8(DomainFactory factory) {
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

    public void testFactory9(DomainFactory factory) {
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

    public void testFactory10(DomainFactory factory) {
        Map<String, Users2> map = factory.createCollection(Users2.class, Arrays.asList("10", "_10", "_"), ids -> {
            System.out.println("mapping");
            Map<String, List<String>> idsMapping = new LinkedHashMap<>();
            idsMapping.put("10", Arrays.asList("10"));
            idsMapping.put("_10", Arrays.asList("_10"));
            idsMapping.put("_", Collections.emptyList());
            return idsMapping;
        });
        for (Map.Entry<String, Users2> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            Users2 users = entry.getValue();
            List<User> list = users.list();
            for (User user : list) {
                System.out.println(user.getId());
                //user.test0();
                System.out.println("===============");
            }
            /*users.test1();
            users.test2();*/
            System.out.println("===============");
        }
    }
}
