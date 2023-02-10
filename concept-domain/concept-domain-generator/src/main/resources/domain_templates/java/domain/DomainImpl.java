package _PACKAGE_.domain._DOMAIN_;

import com.github.linyuzai.domain.core.AbstractDomainBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class _UPPER_Impl implements _UPPER_ {

    protected String id;

    public static class Builder extends AbstractDomainBuilder<_UPPER_Impl, Builder> {

        @NotNull
        protected String id;

        @Override
        protected void initDefaultValue() {

        }

        @Override
        protected _UPPER_Impl doBuild() {
            return new _UPPER_Impl();
        }
    }
}
