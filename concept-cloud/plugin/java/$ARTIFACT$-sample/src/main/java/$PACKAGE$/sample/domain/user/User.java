package $PACKAGE$.sample.domain.user;

import $PACKAGE$.basic.rpc.api.user.UserApi;
import $PACKAGE$.basic.rpc.api.user.UserRO;

/**
 * 在 sample 模块中定义 user 模型
 * <p>
 * 模块内的功能都使用此模型
 * <p>
 * 通过 {@link UserApi} 来获得 {@link UserRO} 再转为 自身模块使用的 user 模型
 */
public interface User {

    String getId();

    String getName();
}
