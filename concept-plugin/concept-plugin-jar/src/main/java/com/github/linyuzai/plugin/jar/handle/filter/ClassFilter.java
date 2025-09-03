package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.PredicateFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 类过滤器
 */
@HandlerDependency(ClassResolver.class)
public class ClassFilter extends PredicateFilter<ClassSupplier> {

    /**
     * 类型过滤
     */
    public static ClassFilter create(Class<?>... classes) {
        return create(Arrays.asList(classes));
    }

    /**
     * 类型过滤
     */
    public static ClassFilter create(Collection<? extends Class<?>> classes) {
        return new ClassFilter(cls -> {
            for (Class<?> c : classes) {
                if (c.isAssignableFrom(cls)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * 类注解过滤
     */
    @SafeVarargs
    public static ClassFilter annotation(Class<? extends Annotation>... annotations) {
        return annotation(Arrays.asList(annotations));
    }

    /**
     * 类注解过滤
     */
    public static ClassFilter annotation(Collection<? extends Class<? extends Annotation>> annotations) {
        return new ClassFilter(cls -> {
            for (Class<? extends Annotation> annotation : annotations) {
                if (cls.isAnnotationPresent(annotation)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * 类修饰过滤
     */
    @SafeVarargs
    public static ClassFilter modifier(Predicate<Integer>... predicates) {
        return modifier(Arrays.asList(predicates));
    }

    /**
     * 类修饰过滤
     */
    public static ClassFilter modifier(Collection<? extends Predicate<Integer>> predicates) {
        return new ClassFilter(cls -> {
            int modifiers = cls.getModifiers();
            for (Predicate<Integer> predicate : predicates) {
                if (predicate.test(modifiers)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * 按是否是枚举过滤
     */
    public static ClassFilter isEnum() {
        return new ClassFilter(Class::isEnum);
    }

    /**
     * 按是否是成员类过滤
     */
    public static ClassFilter isMember() {
        return new ClassFilter(Class::isMemberClass);
    }

    /**
     * 按是否是匿名类过滤
     */
    public static ClassFilter isAnonymous() {
        return new ClassFilter(Class::isAnonymousClass);
    }

    /**
     * 按是否是本地类过滤
     */
    public static ClassFilter isLocal() {
        return new ClassFilter(Class::isLocalClass);
    }

    @SafeVarargs
    public ClassFilter(Predicate<Class<?>>... predicates) {
        this(Arrays.asList(predicates));
    }

    public ClassFilter(Collection<? extends Predicate<Class<?>>> predicates) {
        super(wrap(predicates));
    }

    private static Collection<Predicate<ClassSupplier>> wrap(Collection<? extends Predicate<Class<?>>> predicates) {
        return predicates.stream()
                .map(it -> (Predicate<ClassSupplier>)
                        classSupplier -> it.test(classSupplier.get()))
                .collect(Collectors.toList());
    }

    @Override
    public Object getKey() {
        return ClassSupplier.class;
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
