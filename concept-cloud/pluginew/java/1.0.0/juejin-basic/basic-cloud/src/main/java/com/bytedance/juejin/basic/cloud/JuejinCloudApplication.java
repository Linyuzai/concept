package com.bytedance.juejin.basic.cloud;

import com.bytedance.juejin.basic.boot.JuejinBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 微服务启动注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JuejinCloudConfiguration.class)
@JuejinBootApplication
@SpringBootApplication
public @interface JuejinCloudApplication {
}
