package $PACKAGE$.module.user.domain.user;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.module.user.domain.user.view.UserVO;

/**
 * 用户领域模型转换适配器
 */
public interface UserFacadeAdapter {

    /**
     * 用户领域模型转用户视图
     */
    UserVO do2vo(User user);
}
