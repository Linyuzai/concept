package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.module.sample.domain.sample.view.SampleQuery;
import $PACKAGE$.module.sample.domain.sample.view.SampleVO;
import com.github.linyuzai.domain.core.page.Pages;

import java.util.List;

/**
 * 搜索
 */
public interface SampleSearcher {

    /**
     * 根据 id 获得
     */
    SampleVO get(String id);

    /**
     * 列表查询
     */
    List<SampleVO> list(SampleQuery query);

    /**
     * 分页查询
     */
    Pages<SampleVO> page(SampleQuery query, Pages.Args page);
}
