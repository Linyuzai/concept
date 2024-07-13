package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.convert.*;
import com.github.linyuzai.plugin.core.handle.extract.format.ObjectFormatter;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.ContentMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginText;
import com.github.linyuzai.plugin.core.type.NestedType;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 内容提取器
 * <p>
 * 支持 byte[] {@link String} {@link InputStream} {@link ByteBuffer}
 */
public abstract class ContentExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        //如果是指定类型则匹配内容
        if (String.class == cls || InputStream.class == cls || ByteBuffer.class == cls || byte[].class == cls) {
            return new ContentMatcher(annotations);
        }
        return null;
    }

    @Override
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        if (String.class == cls) {
            Charset charset = getCharset(annotations);//获得编码
            return new ContentToStringConvertor(charset);//转String
        } else if (InputStream.class == cls) {
            return new ContentToInputStreamConvertor();//转InputStream
        } else if (ByteBuffer.class == cls) {
            return new ContentToByteBufferConvertor();//转ByteBuffer
        } else if (byte[].class == cls) {
            return new ContentToByteArrayConvertor();//转byte[]
        } else {
            return null;
        }
    }

    /**
     * 获得编码
     */
    protected Charset getCharset(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginText.class) {
                String charset = ((PluginText) annotation).charset();
                return Charset.forName(charset);
            }
        }
        return null;
    }

    @Override
    public PluginFormatter getFormatter(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        //如果是字节数组返回单个对象的格式化器
        if (cls.isArray() && cls.getComponentType() == byte.class) {
            return new ObjectFormatter();
        }
        return super.getFormatter(type, annotations);
    }

    /**
     * 内容提取执行器工厂
     */
    public static class InvokerFactory extends AbstractPluginExtractor.InvokerFactory {

        @Override
        protected AbstractPluginExtractor<?> createExtractor() {
            return new ContentExtractor<Object>() {
                @Override
                public void onExtract(Object plugin, PluginContext context) {
                }
            };
        }
    }
}
