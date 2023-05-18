package com.bytedance.juejin.pin.domain.comment;

import com.bytedance.juejin.pin.domain.comment.view.CommentQuery;
import com.bytedance.juejin.pin.domain.comment.view.CommentVO;
import com.github.linyuzai.domain.core.page.Pages;

/**
 * 评论搜索
 */
public interface CommentSearcher {

    /**
     * 分页查询评论视图
     */
    Pages<CommentVO> page(CommentQuery query, Pages.Args page);
}
