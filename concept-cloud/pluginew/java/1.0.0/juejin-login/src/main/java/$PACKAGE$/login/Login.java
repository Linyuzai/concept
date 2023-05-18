package $PACKAGE$.login;

import io.swagger.v3.oas.annotations.Hidden;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记参数在登录状态下获得
 */
@Hidden
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

    boolean required() default true;
}
