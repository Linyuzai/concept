package com.bytedance.juejin.domain.like;

import com.github.linyuzai.domain.core.DomainRepository;

/**
 * 点赞存储
 */
public interface LikeRepository extends DomainRepository<Like, Likes<Like>> {
}
