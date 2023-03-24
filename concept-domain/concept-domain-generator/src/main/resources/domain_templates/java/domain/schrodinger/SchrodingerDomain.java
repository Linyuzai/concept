package _PACKAGE_.domain._DOMAIN_.schrodinger;

import _PACKAGE_.domain._DOMAIN_._UPPER_;
import _PACKAGE_.domain._DOMAIN_._UPPER_Repository;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.proxy.DomainObjectProxy;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schrodinger_UPPER_ extends SchrodingerDomainObject<_UPPER_> {

    protected Schrodinger_UPPER_(String id, DomainContext context) {
        super(id, context);
    }

    @Override
    protected Class<? extends _UPPER_> getDomainType() {
        return _UPPER_.class;
    }

    @Override
    protected Class<? extends DomainRepository<? extends _UPPER_>> getDomainRepositoryType() {
        return _UPPER_Repository.class;
    }

    public static class Builder extends SchrodingerDomainObject.Builder<_UPPER_, Builder> {

        @Override
        protected Class<? extends _UPPER_> getDomainType() {
            return _UPPER_.class;
        }

        @Override
        protected DomainObjectProxy<? extends _UPPER_> getDomainProxy() {
            return new Schrodinger_UPPER_(id, context);
        }
    }
}
