package _PACKAGE_.domain._DOMAIN_;

import _PACKAGE_.domain._DOMAIN_.view._UPPER_CreateCommand;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_Query;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_UpdateCommand;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_VO;
import com.github.linyuzai.domain.core.condition.Conditions;

public interface _UPPER_FacadeAdapter {

    _UPPER_ from(_UPPER_CreateCommand create);

    _UPPER_ from(_UPPER_UpdateCommand update, _UPPER_ _LOWER_);

    _UPPER_VO do2vo(_UPPER_ _LOWER_);

    Conditions toConditions(_UPPER_Query query);
}
