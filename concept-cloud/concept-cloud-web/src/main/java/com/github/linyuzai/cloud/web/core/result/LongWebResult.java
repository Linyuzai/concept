package com.github.linyuzai.cloud.web.core.result;

import lombok.Data;

/**
 * 用于返回值的包装，提供额外的数据信息
 */
@Data
public class LongWebResult implements WebResult<Long> {

    private static final long serialVersionUID = 1L;

    private Long result;

    private String message;

    private Object object;
}
