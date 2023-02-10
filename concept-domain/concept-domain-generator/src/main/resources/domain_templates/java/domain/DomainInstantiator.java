package _PACKAGE_.domain._DOMAIN_;

import _PACKAGE_.domain._DOMAIN_.schrodinger.Schrodinger_UPPER_;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_VO;

public interface _UPPER_Instantiator {

    _UPPER_Impl.Builder newBuilder();

    Schrodinger_UPPER_.Builder newSchrodingerBuilder();

    _UPPER_VO newView();
}
