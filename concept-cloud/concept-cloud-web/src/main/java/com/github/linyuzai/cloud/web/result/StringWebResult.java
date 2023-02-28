package com.github.linyuzai.cloud.web.result;

import lombok.Data;

/**
 * 用于返回值的包装，提供额外的数据信息
 */
@Data
public class StringWebResult implements WebResult<String> {

    private static final long serialVersionUID = 1L;

    private String result;

    private String message;

    private Object object;
}
