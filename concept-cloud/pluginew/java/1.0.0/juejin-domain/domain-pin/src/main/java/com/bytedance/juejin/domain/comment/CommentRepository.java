package com.bytedance.juejin.domain.comment;

import com.github.linyuzai.domain.core.DomainRepository;

/**
 * 评论存储
 */
public interface CommentRepository extends DomainRepository<Comment, Comments<Comment>> {

}
