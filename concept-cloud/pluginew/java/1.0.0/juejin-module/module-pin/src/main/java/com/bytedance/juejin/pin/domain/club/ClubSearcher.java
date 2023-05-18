package com.bytedance.juejin.pin.domain.club;

import com.bytedance.juejin.pin.domain.club.view.ClubFullVO;
import com.bytedance.juejin.pin.domain.club.view.ClubQuery;
import com.bytedance.juejin.pin.domain.club.view.ClubVO;

import java.util.List;

/**
 * 圈子搜索
 */
public interface ClubSearcher {

    /**
     * 根据 id 查询圈子全量视图
     */
    ClubFullVO get(String id);

    /**
     * 条件查询圈子视图列表
     */
    List<ClubVO> list(ClubQuery query);
}
