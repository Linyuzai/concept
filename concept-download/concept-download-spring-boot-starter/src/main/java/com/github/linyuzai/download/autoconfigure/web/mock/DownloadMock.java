package com.github.linyuzai.download.autoconfigure.web.mock;

import com.github.linyuzai.download.autoconfigure.properties.DownloadProperties;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringValueResolver;

import java.io.OutputStream;
import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public class DownloadMock implements EmbeddedValueResolverAware {

    private final DownloadConcept concept;

    private final DownloadProperties properties;

    private StringValueResolver resolver;

    @SneakyThrows
    public Object download(Object mock, OutputStream os, Object... args) {
        if (mock == null || os == null) {
            throw new IllegalArgumentException("Mock Object or Output Stream is null");
        }
        if (mock instanceof DownloadOptions) {
            return concept.download((DownloadOptions) mock);
        }
        Method method = getMethod(mock.getClass());
        if (method == null) {
            throw new IllegalArgumentException("No method annotated @Download");
        }
        Object returnValue = method.invoke(mock, args);
        DownloadOptions options = properties.toOptions(
                new MethodParameter(method, -1), returnValue,
                new MockDownloadRequest(), new MockDownloadResponse(os), resolver);
        return concept.download(options);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }

    private static Method getMethod(Class<?> type) {
        Method[] methods = type.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Download.class)) {
                return method;
            }
        }
        Class<?> superclass = type.getSuperclass();
        if (superclass == null) {
            return null;
        }
        return getMethod(superclass);
    }
}
