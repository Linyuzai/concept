package _PACKAGE_.domain._DOMAIN_;

import _PACKAGE_.domain._DOMAIN_.view._UPPER_Query;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_VO;
import com.github.linyuzai.domain.core.page.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class _UPPER_SearcherImpl implements _UPPER_Searcher {

    @Autowired
    protected _UPPER_Repository _LOWER_Repository;

    @Autowired
    protected _UPPER_FacadeAdapter _LOWER_FacadeAdapter;

    @Override
    public Pages<_UPPER_VO> page(_UPPER_Query query, Pages.Args page) {
        return _LOWER_Repository
                .page(_LOWER_FacadeAdapter.toConditions(query), page)
                .map(_LOWER_FacadeAdapter::do2vo);
    }
}
