package com.github.linyuzai.cloud.web.result;

import java.io.Serializable;

/**
 * 用于返回值的包装，提供额外的数据信息
 */
public interface WebResult<R> extends Serializable {

    R getResult();

    String getMessage();

    Object getObject();
}
