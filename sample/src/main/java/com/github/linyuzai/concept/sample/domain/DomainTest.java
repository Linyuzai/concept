package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import com.github.linyuzai.domain.core.mock.MockDomainContext;

import java.util.Arrays;
import java.util.Collections;

public class DomainTest {

    public void test() {
        UserRepository repository = new UserRepositoryImpl();
        DomainContext context = new MockDomainContext(UserRepository.class, repository);

        Users select = repository.select(Arrays.asList("1", "2"));
        select.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User user = context.create(User.class, "1");
        System.out.println(user.getId());

        Users wrap = context.create(Users.class, Collections.singleton(user));
        wrap.list().stream().map(Identifiable::getId).forEach(System.out::println);

        Users users = context.create(Users.class);
        users.list().stream().map(Identifiable::getId).forEach(System.out::println);

        User schrodingerUser = new SchrodingerUser("4", context);
        System.out.println(schrodingerUser.getId());

        Users schrodingerUsers = new SchrodingerUsers(context);
        schrodingerUsers.list().stream().map(Identifiable::getId).forEach(System.out::println);
    }
}
