package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.annotation.AnnotationWebResultFactory;

/**
 * 使用 {@link Boolean} 作为 result 的结果包装工厂
 */
public class BooleanWebResultFactory extends AnnotationWebResultFactory {

    /**
     * 创建成功的返回值
     *
     * @param message 成功信息
     * @param body    响应体
     * @param context 上下文
     * @return 返回值
     */
    @Override
    protected WebResult<?> createSuccessWebResult(String message, Object body, WebContext context) {
        return new BooleanWebResult(true, message, body);
    }

    /**
     * 创建失败的返回值
     *
     * @param message 失败信息
     * @param e       异常
     * @param context 上下文
     * @return 返回值
     */
    @Override
    protected WebResult<?> createFailureWebResult(String message, Throwable e, WebContext context) {
        return new BooleanWebResult(false, message, null);
    }
}
