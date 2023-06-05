package $PACKAGE$.domain.sample;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.Users;
import com.github.linyuzai.domain.core.AbstractDomainBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 示例实现
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleImpl implements Sample {

    protected String id;

    protected String sample;

    protected User user;

    protected Users users;

    public static class Builder extends AbstractDomainBuilder<SampleImpl> {

        @NotEmpty
        protected String id;

        @NotNull
        protected String sample;

        @NotNull
        protected User user;

        @NotNull
        protected Users users;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder sample(String sample) {
            this.sample = sample;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder users(Users users) {
            this.users = users;
            return this;
        }

        @Override
        protected void init() {

        }

        @Override
        protected SampleImpl build() {
            return new SampleImpl(id, sample, user, users);
        }
    }
}
