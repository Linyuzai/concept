package $PACKAGE$.login;

import org.springframework.core.MethodParameter;

/**
 * 标记了 {@link Login} 的参数
 * <p>
 * 适配不同的参数类型返回对应的值
 */
public interface LoginArgumentAdapter {

    boolean support(MethodParameter parameter);

    Object adapt(MethodParameter parameter);
}
