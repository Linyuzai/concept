package _PACKAGE_.domain._DOMAIN_;

import _PACKAGE_.domain._DOMAIN_.schrodinger.Schrodinger_UPPER_;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_VO;
import org.springframework.stereotype.Component;

@Component
public class _UPPER_InstantiatorImpl implements _UPPER_Instantiator {

    @Override
    public _UPPER_Impl.Builder newBuilder() {
        return new _UPPER_Impl.Builder();
    }

    @Override
    public Schrodinger_UPPER_.Builder newSchrodingerBuilder() {
        return new Schrodinger_UPPER_.Builder();
    }

    @Override
    public _UPPER_VO newView() {
        return new _UPPER_VO();
    }
}
