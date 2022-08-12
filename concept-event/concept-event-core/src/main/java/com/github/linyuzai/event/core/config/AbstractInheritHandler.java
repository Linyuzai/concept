package com.github.linyuzai.event.core.config;

import com.github.linyuzai.event.core.exception.EventException;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于反射的继承处理器抽象类
 * <p>
 * 由于不同版本的配置略有差异
 * <p>
 * 一个一个适配所有版本的工作量太大
 * <p>
 * 所以通过反射来处理
 *
 * @param <T> 继承类型
 */
public abstract class AbstractInheritHandler<T extends EngineConfig> implements InheritHandler<T> {

    /**
     * 用于将驼峰转为中划线
     */
    protected final Pattern pattern = Pattern.compile("[A-Z]");

    /**
     * 如果端点指定了继承的端点名称
     * <p>
     * 则进行配置继承
     */
    @Override
    public void inherit(T engine) {
        Map<String, ? extends EndpointConfig> endpoints = engine.getEndpoints();
        for (Map.Entry<String, ? extends EndpointConfig> entry : endpoints.entrySet()) {
            String name = entry.getKey();
            EndpointConfig endpoint = entry.getValue();
            String parent = endpoint.getInherit();
            if (parent != null && !parent.isEmpty()) {
                EndpointConfig inherit = endpoints.get(parent);
                if (inherit == null) {
                    throw new EventException("Inherit endpoint not found: " + parent);
                }
                inherit(endpoint, inherit, getRootClass(), getRootPrefix() + "." + formatKey(name));
            }
        }
    }

    /**
     * 获得根类
     */
    public Class<?> getRootClass() {
        return getClass();
    }

    /**
     * 获得根前缀
     */
    public abstract String getRootPrefix();

    /**
     * 处理继承的步骤框架
     */
    @SneakyThrows
    public void inherit(Object child, Object parent, Class<?> clazz, String prefix) {
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            //忽略静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            //非静态内部类会持有外部类的引用，需要过滤
            //如果使用get/set方法则不会有这个问题
            //但是懒得转换方法名称
            //如果有不需要继承的字段可以配合 #needInherit 方法处理
            if (field.getName().equals("this$0")) {
                continue;
            }
            //如果该字段需要继承则执行继承
            if (needInherit(field)) {
                doInherit(field, child, parent, prefix);
            }
        }
    }

    /**
     * 确定是否需要继承
     * <p>
     * 存在一些内部属性或不需要继承的属性
     */
    public boolean needInherit(Field field) {
        return true;
    }

    /**
     * 继承处理
     */
    public void doInherit(Field field, Object child, Object parent, String prefix) {
        Class<?> type = field.getType();
        //这里的Map由于目前不存在Map中包含对象的情况，这里直接特殊处理了
        if (field.getType() == Map.class) {
            inheritMap(getValue(field, child), getValue(field, parent));
        } else if (isValueType(type)) {
            //判断是否时值类型
            //如果已经自定义了就不继承了
            if (hasCustomValue(prefix + "." + formatKey(field.getName()))) {
                return;
            }
            //否则将父配置的值设置给子配置
            setValue(field, child, getValue(field, parent));
        } else {
            //如果是嵌套的配置则递归处理
            inherit(getValue(field, child), getValue(field, parent), type,
                    prefix + "." + formatKey(field.getName()));
        }
    }

    /**
     * 是否自定义了值
     * <p>
     * 如果已经自定义了就不继承
     */
    public abstract boolean hasCustomValue(String key);

    /**
     * 是否是值类型
     * <p>
     * 如果是值类型则直接继承值
     * <p>
     * 否则当作一个嵌套的配置继续处理
     */
    public boolean isValueType(Class<?> clazz) {
        return clazz == String.class ||
                clazz == Class.class ||
                clazz.isPrimitive() ||
                isWrapClass(clazz) ||
                clazz.isEnum();
    }

    /**
     * 将驼峰转为下划线
     */
    public String formatKey(String key) {
        Matcher matcher = pattern.matcher(key);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "-" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰，没有用到
     */
    @Deprecated
    private String x(String str) {
        str = str.toLowerCase();
        Pattern compile = Pattern.compile("_[a-z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(0).toUpperCase().replace("_", ""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获得所有的属性字段
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 获得值
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Field field, Object object) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return (T) field.get(object);
    }

    /**
     * 设置值
     */
    @SneakyThrows
    public static void setValue(Field field, Object object, Object value) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(object, value);
    }

    /**
     * 是否是包装类型
     */
    public static boolean isWrapClass(Class<?> clazz) {
        try {
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Map 继承
     * <p>
     * 将父节点中有但是子节点中没有的放到子节点中
     */
    public static <K, V> void inheritMap(Map<K, V> child, Map<K, V> parent) {
        for (Map.Entry<K, V> entry : parent.entrySet()) {
            K key = entry.getKey();
            if (child.containsKey(key)) {
                continue;
            }
            child.put(key, entry.getValue());
        }
    }

    public static void inherit(PropertiesConfig child, PropertiesConfig parent) {
        inheritMap(child.getMetadata(), parent.getMetadata());
        if (child.getEncoder() == null) {
            child.setEncoder(parent.getEncoder());
        }
        if (child.getDecoder() == null) {
            child.setDecoder(parent.getDecoder());
        }
        if (child.getErrorHandler() == null) {
            child.setErrorHandler(parent.getErrorHandler());
        }
        if (child.getPublisher() == null) {
            child.setPublisher(parent.getPublisher());
        }
        if (child.getSubscriber() == null) {
            child.setSubscriber(parent.getSubscriber());
        }
    }
}
