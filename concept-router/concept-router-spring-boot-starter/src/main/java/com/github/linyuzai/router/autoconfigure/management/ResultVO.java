package com.github.linyuzai.router.autoconfigure.management;

import lombok.Builder;
import lombok.Data;

/**
 * 结果返回值视图
 */
@Data
@Builder
public class ResultVO {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private Object object;
}
