package com.github.linyuzai.download.core.source.proxy;

import com.github.linyuzai.download.core.exception.DownloadException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReflectionTemplate {

    protected final Map<Class<? extends Annotation>, Method> methodMap = new HashMap<>();

    protected final Map<Class<? extends Annotation>, Field> fieldMap = new HashMap<>();

    public ReflectionTemplate(Class<?> clazz) {
        this(clazz, isReflectSuper(clazz));
    }

    public ReflectionTemplate(Class<?> clazz, boolean reflectSuper) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(SourceProxy.class)) {
                    int count = method.getParameterCount();
                    if (count == 0) {
                        Method exist = methodMap.get(annotationType);
                        if (exist == null) {
                            methodMap.put(annotationType, method);
                        } else {
                            throw new DownloadException(annotationType.getName()
                                    + " is already defined on " + exist);
                        }
                    } else {
                        throw new DownloadException("Method cannot have parameters: " + method);
                    }
                }
            }
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(SourceProxy.class)) {
                    if (methodMap.containsKey(annotationType)) {
                        //ignore
                        continue;
                    }
                    Field exist = fieldMap.get(annotationType);
                    if (exist == null) {
                        fieldMap.put(annotationType, field);
                    } else {
                        throw new DownloadException(annotationType.getName()
                                + " is already defined on " + exist);
                    }
                }
            }
        }

        if (reflectSuper) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                ReflectionTemplate superTemplate = new ReflectionTemplate(superclass, true);
                mergeSuper(superTemplate);
            }
        }
    }

    public static boolean isReflectSuper(Class<?> clazz) {
        SourceModel annotation = clazz.getAnnotation(SourceModel.class);
        if (annotation == null) {
            throw new DownloadException("@SourceModel not found");
        }
        return annotation.superclass();
    }

    public Object reflect(Class<? extends Annotation> clazz, Object target) {
        try {
            Method method = methodMap.get(clazz);
            if (method == null) {
                Field field = fieldMap.get(clazz);
                if (field == null) {
                    return null;
                } else {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    return field.get(target);
                }
            } else {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method.invoke(target);
            }
        } catch (Throwable e) {
            throw new DownloadException(e);
        }
    }

    protected void mergeSuper(ReflectionTemplate template) {
        for (Map.Entry<Class<? extends Annotation>, Method> entry : template.methodMap.entrySet()) {
            if (!methodMap.containsKey(entry.getKey()) && !fieldMap.containsKey(entry.getKey())) {
                methodMap.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<Class<? extends Annotation>, Field> entry : template.fieldMap.entrySet()) {
            if (!methodMap.containsKey(entry.getKey()) && !fieldMap.containsKey(entry.getKey())) {
                fieldMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
