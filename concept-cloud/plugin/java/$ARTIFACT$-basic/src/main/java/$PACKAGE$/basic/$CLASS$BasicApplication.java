package $PACKAGE$.basic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单体应用和微服务通用组件的基础注解
 * <p>
 * 默认情况下，basic 模块中的配置通过 spring.factories 注入
 * <p>
 * 各个模块指定配置扫描路径为 config 包，并使用 @Bean 手动注入
 * <p>
 * 这样可以通过对应的配置类和 {@link ConditionalOnMissingBean} 方便在 application 模块中重写扩展
 * <p>
 * 如果不想要这样配置可以自行修改扫描路径
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import($CLASS$BasicConfiguration.class)
@ComponentScan(basePackages = "$PACKAGE$.*.config",
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@SpringBootApplication
public @interface $CLASS$BasicApplication {
}
