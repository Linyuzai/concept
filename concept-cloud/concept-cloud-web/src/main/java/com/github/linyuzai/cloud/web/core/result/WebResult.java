package com.github.linyuzai.cloud.web.core.result;

import java.io.Serializable;

/**
 * 用于返回值的包装，提供额外的数据信息
 */
public interface WebResult<R> extends Serializable {

    /**
     * 结果描述
     *
     * @return 对于结果的描述，如是否成功，结果码等
     */
    R getResult();

    /**
     * 返回信息
     *
     * @return 成功或失败的信息描述
     */
    String getMessage();

    /**
     * 返回数据
     *
     * @return 响应体数据
     */
    Object getObject();
}
