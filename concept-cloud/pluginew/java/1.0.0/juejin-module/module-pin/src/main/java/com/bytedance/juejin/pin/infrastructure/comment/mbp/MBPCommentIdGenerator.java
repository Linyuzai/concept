package com.bytedance.juejin.pin.infrastructure.comment.mbp;

import com.bytedance.juejin.pin.domain.comment.CommentIdGenerator;
import com.bytedance.juejin.pin.domain.comment.view.CommentCreateCommand;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;
import org.springframework.stereotype.Component;

/**
 * 基于 MBP id 生成器 的 评论 id 生成器
 */
@Component
public class MBPCommentIdGenerator extends MBPDomainIdGenerator<CommentCreateCommand> implements CommentIdGenerator {
}
