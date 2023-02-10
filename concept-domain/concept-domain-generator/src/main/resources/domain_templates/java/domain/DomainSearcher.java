package _PACKAGE_.domain._DOMAIN_;

import _PACKAGE_.domain._DOMAIN_.view._UPPER_Query;
import _PACKAGE_.domain._DOMAIN_.view._UPPER_VO;
import com.github.linyuzai.domain.core.page.Pages;

public interface _UPPER_Searcher {

    Pages<_UPPER_VO> page(_UPPER_Query query, Pages.Args page);
}
