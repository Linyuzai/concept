package com.bytedance.juejin.pin.domain.like;

import com.bytedance.juejin.domain.like.Like;
import com.bytedance.juejin.domain.like.LikeRepository;
import com.bytedance.juejin.domain.like.LikeService;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.like.view.LikeCreateCommand;
import com.bytedance.juejin.pin.domain.like.view.LikeDeleteCommand;
import com.github.linyuzai.domain.core.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 点赞应用服务
 */
@Service
public class LikeApplicationService {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeFacadeAdapter likeFacadeAdapter;

    @Autowired
    private LikeRepository likeRepository;

    /**
     * 点赞
     */
    public void create(LikeCreateCommand create, User user) {
        Like like = likeFacadeAdapter.from(create, user);
        Like exist = likeRepository.get(like.getId());
        if (exist == null) {
            likeService.create(like, user);
        } else {
            throw new DomainException("已经点赞");
        }
    }

    /**
     * 取消点赞
     */
    public void delete(LikeDeleteCommand delete, User user) {
        Like like = likeRepository.get(delete.getId());
        if (like == null) {
            return;
        }
        likeService.delete(like, user);
    }
}
