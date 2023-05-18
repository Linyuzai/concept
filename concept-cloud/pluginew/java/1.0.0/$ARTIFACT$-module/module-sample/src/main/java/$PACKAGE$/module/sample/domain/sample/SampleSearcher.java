package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.module.sample.domain.sample.view.SampleQuery;
import $PACKAGE$.module.sample.domain.sample.view.SampleVO;
import com.github.linyuzai.domain.core.page.Pages;

/**
 * 沸点搜索
 */
public interface SampleSearcher {

    /**
     * 根据 id 获得
     */
    SampleVO get(String id);

    /**
     * 分页查询
     */
    Pages<SampleVO> page(SampleQuery query, Pages.Args page);
}
