package com.github.linyuzai.download.autoconfigure.web.servlet;

import com.github.linyuzai.download.autoconfigure.properties.DownloadProperties;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.web.DownloadRequest;
import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Getter
@RequiredArgsConstructor
@RestControllerAdvice
public class ServletDownloadAdvice implements ResponseBodyAdvice<Object>, EmbeddedValueResolverAware {

    private final DownloadConcept concept;

    private final DownloadProperties properties;

    private StringValueResolver resolver;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(Download.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        DownloadOptions options = properties.toOptions(returnType, body,
                getRequest(request), getResponse(response), resolver);
        return concept.download(options);
    }

    protected DownloadRequest getRequest(ServerHttpRequest request) {
        return new ServletDownloadRequest(request);
    }

    protected DownloadResponse getResponse(ServerHttpResponse response) {
        return new ServletDownloadResponse(response);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }
}
