package com.bytedance.juejin.basic.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单体应用启动注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JuejinBootConfiguration.class)
@ComponentScan(basePackages = "com.bytedance.juejin.*.config",
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@SpringBootApplication
public @interface JuejinBootApplication {
}
