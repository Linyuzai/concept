package com.bytedance.juejin.pin.infrastructure.pin.mbp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 沸点数据模型
 */
@Data
@TableName("t_pin")
class PinPO implements Identifiable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String content;

    private String clubId;

    private String userId;

    private Date createTime;

    @TableLogic
    private Boolean deleted;
}
