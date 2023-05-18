package com.bytedance.juejin.pin.infrastructure.club.mbp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

/**
 * 圈子用户数据模型
 */
@Data
@TableName("t_club_user")
public class ClubUserPO implements Identifiable {

    @TableId(type = IdType.AUTO)
    private String id;

    private String clubId;

    private String userId;
}
