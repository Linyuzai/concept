package com.bytedance.juejin.pin.domain.comment;

import com.bytedance.juejin.domain.comment.CommentRepository;
import com.bytedance.juejin.pin.domain.comment.view.CommentQuery;
import com.bytedance.juejin.pin.domain.comment.view.CommentVO;
import com.github.linyuzai.domain.core.page.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 评论搜索实现
 */
@Component
public class CommentSearcherImpl implements CommentSearcher {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentFacadeAdapter commentFacadeAdapter;

    @Override
    public Pages<CommentVO> page(CommentQuery query, Pages.Args page) {
        return commentRepository.page(commentFacadeAdapter.toConditions(query), page)
                .map(commentFacadeAdapter::do2vo);
    }
}
