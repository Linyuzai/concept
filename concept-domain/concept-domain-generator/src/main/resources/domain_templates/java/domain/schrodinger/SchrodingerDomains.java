package _PACKAGE_.domain._DOMAIN_.schrodinger;

import _PACKAGE_.domain._DOMAIN_._UPPER_;
import _PACKAGE_.domain._DOMAIN_._UPPER_Repository;
import _PACKAGE_.domain._DOMAIN_._UPPER_s;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainCollection;

public abstract class Schrodinger_UPPER_s extends SchrodingerDomainCollection<_UPPER_> implements _UPPER_s {

    @Override
    protected Class<? extends _UPPER_> getDomainType() {
        return _UPPER_.class;
    }

    @Override
    protected Class<? extends DomainRepository<? extends _UPPER_>> getDomainRepositoryType() {
        return _UPPER_Repository.class;
    }
}
