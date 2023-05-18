package com.bytedance.juejin.pin.domain.comment;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.login.Login;
import com.bytedance.juejin.pin.domain.comment.view.CommentCreateCommand;
import com.bytedance.juejin.pin.domain.comment.view.CommentDeleteCommand;
import com.bytedance.juejin.pin.domain.comment.view.CommentQuery;
import com.bytedance.juejin.pin.domain.comment.view.CommentVO;
import com.github.linyuzai.domain.core.page.Pages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "沸点评论")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentApplicationService commentApplicationService;

    @Autowired
    private CommentSearcher commentSearcher;

    @Operation(summary = "添加评论")
    @PostMapping
    public void create(@RequestBody CommentCreateCommand create, @Login User user) {
        commentApplicationService.create(create, user);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping
    public void delete(@RequestBody CommentDeleteCommand delete, @Login User user) {
        commentApplicationService.delete(delete, user);
    }

    @Operation(summary = "分页查询评论")
    @GetMapping("/page")
    public Pages<CommentVO> page(CommentQuery query, Pages.Args page) {
        return commentSearcher.page(query, page);
    }
}
