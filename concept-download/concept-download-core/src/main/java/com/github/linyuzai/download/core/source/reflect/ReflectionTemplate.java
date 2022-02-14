package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.reflect.conversion.ValueConversion;
import com.github.linyuzai.download.core.source.reflect.conversion.ValueConvertor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 每个类对应一个反射模版，包含该类的反射信息。
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReflectionTemplate {

    protected final Map<Class<? extends Annotation>, Reflector> reflectorMap = new HashMap<>();

    public ReflectionTemplate(Class<?> clazz) {
        this(clazz, isReflectSuper(clazz));
    }

    /**
     * 处理标记了 {@link SourceReflection} 注解的方法或字段
     *
     * @param clazz        Class
     * @param reflectSuper 是否反射父类
     */
    public ReflectionTemplate(Class<?> clazz, boolean reflectSuper) {
        Method[] methods = clazz.getDeclaredMethods();
        //处理所有方法
        for (Method method : methods) {
            //获得方法上的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //如果注解上标注了特定的注解
                if (annotationType.isAnnotationPresent(SourceReflection.class)) {
                    //将注解和方法反射器缓存起来
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
        //处理所有的属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //获得属性上的注解
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //如果注解上标注了特定的注解
                if (annotationType.isAnnotationPresent(SourceReflection.class)) {
                    //将注解和属性反射器缓存起来
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

        //处理父类
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

    /**
     * 通过注解获得值。
     *
     * @param annotation Annotation
     * @param model      模型
     * @return 值
     */
    public Object value(Class<? extends Annotation> annotation, Object model) {
        return reflectorMap.get(annotation).reflect(model);
    }

    /**
     * 将模型的值通过反射设置到 {@link Source}，
     * 如果值类型不匹配则尝试使用 {@link ValueConvertor} 进行值转换。
     *
     * @param model  模型
     * @param source {@link Source}
     */
    @SneakyThrows
    public void reflect(Object model, Source source) {
        for (Map.Entry<Class<? extends Annotation>, Reflector> entry : reflectorMap.entrySet()) {
            Object reflect = entry.getValue().reflect(model);
            /*if (reflect == null) {
                continue;
            }*/
            SourceReflection reflection = entry.getKey().getAnnotation(SourceReflection.class);
            //优先使用方法设置
            if (!reflectMethod(reflection, source, reflect)) {
                reflectField(reflection, source, reflect);
            }
        }
    }

    @SneakyThrows
    protected boolean reflectField(SourceReflection reflection, Source source, Object reflect) {
        String fieldName = reflection.fieldName();
        if (fieldName.isEmpty()) {
            return false;
        }
        Field field = getReflectField(source.getClass(), fieldName);
        if (field == null) {
            return false;
        }
        Class<?> type = field.getType();
        Object value;
        if (reflect == null || type.isInstance(reflect)) {
            value = reflect;
        } else {
            value = convertValue(reflect, type);
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(source, value);
        return true;
    }

    @SneakyThrows
    protected boolean reflectMethod(SourceReflection reflection, Source source, Object reflect) {
        String methodName = reflection.methodName();
        if (methodName.isEmpty()) {
            return false;
        }
        Class<?> parameterType = reflection.methodParameterType();
        Method method = getReflectMethod(source.getClass(), methodName, parameterType);
        if (method == null) {
            return false;
        }
        Object value;
        if (reflect == null || parameterType.isInstance(reflect)) {
            value = reflect;
        } else {
            value = convertValue(reflect, parameterType);
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        method.invoke(source, value);
        return true;
    }

    protected Field getReflectField(Class<?> clazz, String name) {
        Class<?> c = clazz;
        while (c != null) {
            try {
                return c.getDeclaredField(name);
            } catch (Throwable ignore) {
            }
            c = c.getSuperclass();
        }
        return null;
    }

    protected Method getReflectMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        Class<?> c = clazz;
        while (c != null) {
            try {
                return c.getDeclaredMethod(name, parameterTypes);
            } catch (Throwable ignore) {
            }
            c = c.getSuperclass();
        }
        return null;
    }

    protected Object convertValue(Object value, Class<?> type) {
        return ValueConversion.getInstance().convert(value, type);
    }

    /**
     * 合并父类模版的反射信息，
     * 即支持父类上的注解。
     *
     * @param template 父类模版
     */
    protected void mergeSuper(ReflectionTemplate template) {
        for (Map.Entry<Class<? extends Annotation>, Reflector> entry : template.reflectorMap.entrySet()) {
            if (!reflectorMap.containsKey(entry.getKey())) {
                reflectorMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
