package com.github.linyuzai.properties.refresh.core.concept;

import com.github.linyuzai.properties.refresh.core.*;
import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 支持自动刷新的配置属性
 */
public class ReflectPropertiesRefreshConcept implements PropertiesRefreshConcept {

    /**
     * 刷新器管理器
     */
    private final PropertiesRefresherManager refresherManager = new PropertiesRefresherManager();

    /**
     * 获得所有的字段和方法进行处理
     *
     * @param object 被处理的对象
     */
    @Override
    public void register(Object object) {
        //TargetFinder
        List<Field> fields = null;//ReflectUtils.getFields(object);
        List<Method> methods = null;//ReflectUtils.getMethods(object);
        registerFields(fields, object);
        registerMethods(methods, object);
    }

    /**
     * 处理字段
     *
     * @param fields 需要处理的字段
     * @param object 实例对象
     */
    @SneakyThrows
    public void registerFields(List<Field> fields, Object object) {
        for (Field field : fields) {
            //判断是否标注了注解
            RefreshableProperties annotation = field.getAnnotation(RefreshableProperties.class);
            if (annotation == null) {
                Class<?> clazz = field.getType();
                if (clazz.isAnnotationPresent(PropertiesModel.class)) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    //嵌套处理
                    register(field.get(object));
                }
            } else {
                //如果标注了注解则添加一个该字段的刷新器
                refresherManager.addRefresher(
                        new FieldPropertiesRefresher(annotation.value(), object, field));
            }
        }
    }

    /**
     * 处理方法
     *
     * @param methods 需要处理的方法
     * @param object  实例对象
     */
    public void registerMethods(List<Method> methods, Object object) {
        for (Method method : methods) {
            //如果标注了注解则添加一个该方法的回调刷新器
            if (method.isAnnotationPresent(OnPropertiesRefresh.class)) {
                refresherManager.addRefresher(new MethodPropertiesRefresher(object, method));
            }
        }
    }

    /**
     * 使用当前配置属性进行刷新
     */
    //@Override
    public void refresh() {
        refresherManager.refresh(null);
    }

    /**
     * 使用当前配置属性进行条件刷新
     *
     * @param condition 刷新条件
     */
    //@Override
    public void refresh(RefreshPropertiesCondition condition) {
        refresherManager.refresh(null, condition);
    }

    /**
     * 销毁刷新器管理器
     */
    //@Override
    public void destroy() {
        refresherManager.destroy();
    }
}
