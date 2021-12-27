package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.reflect.conversion.ValueConversion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReflectionTemplate {

    protected final Map<Class<? extends Annotation>, Reflector> reflectorMap = new HashMap<>();

    public ReflectionTemplate(Class<?> clazz) {
        this(clazz, isReflectSuper(clazz));
    }

    public ReflectionTemplate(Class<?> clazz, boolean reflectSuper) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(SourceReflection.class)) {
                    int count = method.getParameterCount();
                    if (count == 0) {
                        Reflector exist = reflectorMap.get(annotationType);
                        if (exist == null) {
                            reflectorMap.put(annotationType, new MethodReflector(method));
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
                if (annotationType.isAnnotationPresent(SourceReflection.class)) {
                    Reflector exist = reflectorMap.get(annotationType);
                    if (exist == null) {
                        reflectorMap.put(annotationType, new FieldReflector(field));
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
                ReflectionTemplate template = new ReflectionTemplate(superclass, true);
                mergeSuper(template);
            }
        }
    }

    @Deprecated
    public static boolean hasAnnotation(Class<? extends Annotation> annotation) {
        if (annotation == SourceReflection.class) {
            return true;
        }
        Annotation[] annotations = annotation.getAnnotations();
        for (Annotation a : annotations) {
            boolean hasAnnotation = hasAnnotation(a.annotationType());
            if (hasAnnotation) {
                return true;
            }
        }
        return false;
    }

    public static boolean isReflectSuper(Class<?> clazz) {
        SourceModel annotation = clazz.getAnnotation(SourceModel.class);
        if (annotation == null) {
            throw new DownloadException("@SourceModel not found");
        }
        return annotation.superclass();
    }

    public Object value(Class<? extends Annotation> annotation, Object model) {
        try {
            return reflectorMap.get(annotation).reflect(model);
        } catch (ReflectiveOperationException e) {
            throw new DownloadException(e);
        }
    }

    public void reflect(Object model, Source source) {
        try {
            for (Map.Entry<Class<? extends Annotation>, Reflector> entry : reflectorMap.entrySet()) {
                SourceReflection reflection = entry.getKey().getAnnotation(SourceReflection.class);
                String methodName = reflection.methodName();
                if (methodName.isEmpty()) {
                    String fieldName = reflection.fieldName();
                    if (fieldName.isEmpty()) {
                        //Ignore SourceObject
                    } else {
                        Object reflect = entry.getValue().reflect(model);
                        if (reflect == null) {
                            continue;
                        }
                        Field field = source.getClass().getDeclaredField(fieldName);
                        Class<?> type = field.getType();
                        Object value;
                        if (type.isInstance(reflect)) {
                            value = reflect;
                        } else {
                            value = convertValue(reflect, type);
                        }
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        field.set(source, value);
                    }
                } else {
                    Object reflect = entry.getValue().reflect(model);
                    if (reflect == null) {
                        continue;
                    }
                    Class<?> parameterType = reflection.methodParameterType();
                    Object value;
                    if (parameterType.isInstance(reflect)) {
                        value = reflect;
                    } else {
                        value = convertValue(reflect, parameterType);
                    }
                    Method method = source.getClass().getDeclaredMethod(methodName, parameterType);
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(source, value);
                }
            }
        } catch (Throwable e) {
            throw new DownloadException(e);
        }
    }

    protected Object convertValue(Object value, Class<?> type) {
        return ValueConversion.helper().convert(value, type);
    }

    protected void mergeSuper(ReflectionTemplate template) {
        for (Map.Entry<Class<? extends Annotation>, Reflector> entry : template.reflectorMap.entrySet()) {
            if (!reflectorMap.containsKey(entry.getKey())) {
                reflectorMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
