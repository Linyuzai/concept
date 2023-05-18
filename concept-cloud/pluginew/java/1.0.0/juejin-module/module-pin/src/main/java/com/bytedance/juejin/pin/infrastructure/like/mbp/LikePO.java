package com.bytedance.juejin.pin.infrastructure.like.mbp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 点赞数据模型
 */
@Data
@TableName("t_like")
class LikePO implements Identifiable {

    /**
     * 点赞 id
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 给沸点点赞则存在沸点 id
     */
    private String pinId;

    /**
     * 给评论点赞则存在评论 id
     */
    private String commentId;

    /**
     * 点赞用户
     */
    private String userId;

    /**
     * 点赞时间
     */
    private Date createTime;
}
