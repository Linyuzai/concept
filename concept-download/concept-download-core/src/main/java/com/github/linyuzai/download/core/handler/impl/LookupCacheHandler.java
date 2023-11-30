package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;

/**
 * 对于某些请求是否可以直接获得缓存而不重复走前面的逻辑。
 * 下载内容不可变（不好判断）+ 启用并存在缓存。
 */
@Deprecated
public class LookupCacheHandler implements DownloadHandler {

    @Override
    public Object handle(DownloadContext context, DownloadHandlerChain chain) {
        return chain.next(context);
    }
}
