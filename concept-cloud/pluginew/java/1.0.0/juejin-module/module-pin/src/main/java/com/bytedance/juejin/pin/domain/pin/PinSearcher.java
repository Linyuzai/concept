package com.bytedance.juejin.pin.domain.pin;

import com.bytedance.juejin.pin.domain.pin.view.PinQuery;
import com.bytedance.juejin.pin.domain.pin.view.PinVO;
import com.github.linyuzai.domain.core.page.Pages;

/**
 * 沸点搜索
 */
public interface PinSearcher {

    /**
     * 根据 id 获得沸点视图
     */
    PinVO get(String id);

    /**
     * 分页查询沸点视图
     */
    Pages<PinVO> page(PinQuery query, Pages.Args page);
}
