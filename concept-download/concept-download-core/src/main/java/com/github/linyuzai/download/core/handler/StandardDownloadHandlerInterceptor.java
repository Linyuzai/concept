package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.handler.impl.CompressSourceHandler;
import com.github.linyuzai.download.core.handler.impl.DestroyContextHandler;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.impl.InitializeContextHandler;
import com.github.linyuzai.download.core.handler.impl.LoadSourceHandler;
import com.github.linyuzai.download.core.handler.impl.WriteResponseHandler;
import com.github.linyuzai.download.core.handler.impl.CreateSourceHandler;

/**
 * 标准的下载处理器拦截器 / Standard interceptor of handler
 * 每个标准流程结束都会回调 / A callback occurs at the end of each standard process
 */
public interface StandardDownloadHandlerInterceptor extends DownloadHandlerInterceptor {

    /**
     * 确定步骤并向下传递 / Identify steps and pass them down
     *
     * @param handler 下载处理器 / Handler of download
     * @param context 下载上下文 / Context of download
     */
    @Override
    default void intercept(DownloadHandler handler, DownloadContext context) {
        if (handler instanceof InitializeContextHandler) {
            beforeContextInitialized(context);
        } else if (handler instanceof CreateSourceHandler) {
            beforeSourceCreated(context);
        } else if (handler instanceof LoadSourceHandler) {
            beforeSourceLoaded(context);
        } else if (handler instanceof CompressSourceHandler) {
            beforeSourceCompressed(context);
        } else if (handler instanceof WriteResponseHandler) {
            beforeResponseWritten(context);
        } else if (handler instanceof DestroyContextHandler) {
            beforeContextDestroyed(context);
        } else {
            beforeOtherHandled(handler, context);
        }
    }

    /**
     * 下载上下文初始化后回调 / Callback after initialization of download context
     *
     * @param context 下载上下文 / Context of download
     */
    default void beforeContextInitialized(DownloadContext context) {

    }

    /**
     * 下载源创建之后回调 / Callback after download source creation
     *
     * @param context 下载上下文 / Context of download
     */
    default void beforeSourceCreated(DownloadContext context) {

    }

    /**
     * 下载源加载之后回调 / Callback after download source loaded
     *
     * @param context 下载上下文 / Context of download
     */
    default void beforeSourceLoaded(DownloadContext context) {

    }

    /**
     * 下载源压缩之后回调 / Callback after download source compression
     *
     * @param context 下载上下文 / Context of download
     */
    default void beforeSourceCompressed(DownloadContext context) {

    }

    /**
     * 响应写入之后回调 / Callback after response written
     *
     * @param context 下载上下文 / Context of download
     */
    default void beforeResponseWritten(DownloadContext context) {

    }

    /**
     * 下载上下文销毁之后回调 / Callback after destruction of download context
     *
     * @param context 下载上下文 / Context of download
     */
    default void beforeContextDestroyed(DownloadContext context) {

    }

    /**
     * 没有匹配到的处理器的回调 / Callback after no matching handler handled
     *
     * @param handler 没有匹配到的处理器 / No matching handler
     * @param context 下载上下文 / Context of download
     */
    default void beforeOtherHandled(DownloadHandler handler, DownloadContext context) {

    }
}
