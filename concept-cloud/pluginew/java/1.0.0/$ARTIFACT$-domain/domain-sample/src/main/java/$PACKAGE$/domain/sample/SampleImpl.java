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

    public static class Builder extends AbstractDomainBuilder<SampleImpl> {

        @NotEmpty
        protected String id;

        @NotNull
        protected String sample;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder sample(String sample) {
            this.sample = sample;
            return this;
        }

        @Override
        protected void init() {

        }

        @Override
        protected SampleImpl build() {
            return new SampleImpl(id, sample);
        }
    }
}
