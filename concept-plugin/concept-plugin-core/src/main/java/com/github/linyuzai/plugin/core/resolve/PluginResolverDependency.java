package com.github.linyuzai.plugin.core.resolve;

/**
 * 插件解析器 {@link PluginResolver} 的依赖支持。
 * 插件进行解析时是一步一步进行的，
 * 解析器解析时可能需要依赖其他解析器的解析结果，
 * 将这种关系定义为解析器的依赖关系，类似于我们的包依赖
 * 这样就可以通过一定方式自动设置额外的解析器，
 * 不需要手动添加大量的解析器。
 */
public interface PluginResolverDependency {

    /**
     * 获得依赖的所有解析器类。
     * 默认情况下，会尝试通过类上标记的注解 {@link DependOnResolvers} 来获得。
     *
     * @return 依赖的所有解析器类
     */
    @SuppressWarnings("unchecked")
    default Class<? extends PluginResolver>[] dependencies() {
        Class<?> clazz = getClass();
        //当前类有就用当前类的配置，否则查找其父类上的配置
        while (clazz != null) {
            DependOnResolvers annotation = clazz.getAnnotation(DependOnResolvers.class);
            if (annotation != null) {
                return annotation.value();
            }
            clazz = clazz.getSuperclass();
        }
        return new Class[0];
    }
}
