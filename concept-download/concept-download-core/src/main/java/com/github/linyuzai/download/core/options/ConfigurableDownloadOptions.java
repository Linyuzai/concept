package com.github.linyuzai.download.core.options;

import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.web.DownloadRequest;
import com.github.linyuzai.download.core.web.DownloadResponse;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;

public interface ConfigurableDownloadOptions extends DownloadOptions {

    void setSource(Object source);

    void setSourceCacheEnabled(boolean enabled);

    void setSourceCachePath(String path);

    void setSourceCacheDelete(boolean delete);

    void setFilename(String filename);

    void setInline(boolean inline);

    void setContentType(String contentType);

    void setCompressFormat(String format);

    void setForceCompress(boolean force);

    void setCompressCacheEnabled(boolean enabled);

    void setCompressCachePath(String path);

    void setCompressCacheName(String name);

    void setCompressCacheDelete(boolean delete);

    void setCharset(Charset charset);

    void setHeaders(Map<String, String> headers);

    void setRequest(DownloadRequest request);

    void setResponse(DownloadResponse response);

    void setExtra(Object extra);

    void setMethod(Method method);

    void setReturnValue(Object returnValue);

    void setEventListener(DownloadEventListener listener);
}
