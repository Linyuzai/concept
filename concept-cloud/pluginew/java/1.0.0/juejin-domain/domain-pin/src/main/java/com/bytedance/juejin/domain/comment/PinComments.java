package com.bytedance.juejin.domain.comment;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.proxy.DomainProxy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 沸点的评论集合
 */
public interface PinComments extends Comments<PinComment>,
        DomainProxy.RepositoryAccess<PinComment>,
        DomainProxy.ConditionsAccess,
        DomainProxy.ExtraAccess<List<PinComment>> {

    /**
     * 获得最新的 n 条评论
     */
    default List<PinComment> getNewestList(int count) {
        List<PinComment> extra = getExtra();//获得缓存
        if (extra == null) {
            //生成条件
            Conditions conditions = Conditions.from(getConditions()).lambda()
                    .orderBy(PinComment::getCreateTime, true)
                    .limit(count);
            //查询数据
            List<PinComment> comments = getRepository().select(conditions)
                    .stream()
                    .collect(Collectors.toList());
            //设置缓存
            setExtra(comments);
            return comments;
        } else {
            return extra;
        }
    }
}
