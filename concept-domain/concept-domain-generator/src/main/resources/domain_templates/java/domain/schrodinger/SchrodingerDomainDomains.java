package _PACKAGE_.domain._DOMAIN_.schrodinger;

import _PACKAGE_.domain._DOMAIN_._UPPER_;
import _PACKAGE_.domain._DOMAIN_._UPPER_Repository;
import _PACKAGE_.domain._DOMAIN_._UPPER_s;
import com.github.linyuzai.domain.core.ContextDomainBuilder;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schrodinger_UPPER__UPPER_s extends Schrodinger_UPPER_s implements _UPPER_s {

    protected Schrodinger_UPPER__UPPER_s(String _LOWER_Id, DomainContext context) {
        this.ownerId = _LOWER_Id;
        this.context = context;
    }

    @Override
    protected Conditions onConditionsObtain(Conditions conditions, String id) {
        return conditions.lambda().equal(_UPPER_::getId, id);
    }

    @Override
    protected Class<?> getOwnerType() {
        return _UPPER_.class;
    }

    @Override
    protected Class<? extends DomainRepository<?>> getOwnerRepositoryType() {
        return _UPPER_Repository.class;
    }

    public static class Builder extends ContextDomainBuilder<Schrodinger_UPPER__UPPER_s, Builder> {

        @NotNull
        protected String _LOWER_Id;

        public Builder _LOWER_Id(String _LOWER_Id) {
            this._LOWER_Id = _LOWER_Id;
            return this;
        }

        @Override
        protected Schrodinger_UPPER__UPPER_s doBuild() {
            return new Schrodinger_UPPER__UPPER_s(_LOWER_Id, context);
        }
    }
}
