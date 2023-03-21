package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;

/**
 * 返回值包装工厂抽象实现
 */
public abstract class AbstractWebResultFactory implements WebResultFactory {

    /**
     * 创建返回值
     * <p>
     * 如果上下文中存在 {@link Throwable} 创建失败结果
     * <p>
     * 否则创建成功结果
     *
     * @param context 上下文
     * @return 返回值
     */
    @Override
    public WebResult<?> create(WebContext context) {
        Throwable e = context.get(Throwable.class);
        if (e == null) {
            Object body = context.get(WebContext.Response.BODY);
            return createSuccessWebResult(getSuccessMessage(context), body, context);
        } else {
            return createFailureWebResult(getFailureMessage(e, context), e, context);
        }
    }

    /**
     * 获得成功信息
     *
     * @param context 上下文
     * @return 成功信息
     */
    protected String getSuccessMessage(WebContext context) {
        return null;
    }

    /**
     * 获得失败信息
     * <p>
     * 异常对应的信息
     *
     * @param e       异常
     * @param context 上下文
     * @return 失败信息
     */
    protected String getFailureMessage(Throwable e, WebContext context) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

    /**
     * 创建成功的返回值
     *
     * @param message 成功信息
     * @param body    响应体
     * @param context 上下文
     * @return 返回值
     */
    protected abstract WebResult<?> createSuccessWebResult(String message, Object body, WebContext context);

    /**
     * 创建失败的返回值
     *
     * @param message 失败信息
     * @param e       异常
     * @param context 上下文
     * @return 返回值
     */
    protected abstract WebResult<?> createFailureWebResult(String message, Throwable e, WebContext context);
}
