package $PACKAGE$.basic.boot;

import $PACKAGE$.basic.$CLASS$BasicApplication;
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
@Import($CLASS$BootConfiguration.class)
@@$CLASS$BasicApplication
public @interface $CLASS$BootApplication {
}
