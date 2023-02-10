package _PACKAGE_.domain._DOMAIN_;

import _PACKAGE_.domain._DOMAIN_.view._UPPER_CreateCommand;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_Query;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_UpdateCommand;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_VO;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.Conditions;
import org.springframework.beans.factory.annotation.Autowired;

public class _UPPER_FacadeAdapterImpl implements _UPPER_FacadeAdapter {

    @Autowired
    protected _UPPER_Instantiator _LOWER_Instantiator;

    @Autowired
    protected _UPPER_IdGenerator _LOWER_IdGenerator;

    @Autowired
    protected DomainContext context;

    @Autowired
    protected DomainValidator validator;

    @Override
    public _UPPER_ from(_UPPER_CreateCommand create) {

    }

    @Override
    public _UPPER_ from(_UPPER_UpdateCommand update, _UPPER_ _LOWER_) {

    }

    @Override
    public _UPPER_VO do2vo(_UPPER_ _LOWER_) {

    }

    @Override
    public Conditions toConditions(_UPPER_Query query) {

    }
}
