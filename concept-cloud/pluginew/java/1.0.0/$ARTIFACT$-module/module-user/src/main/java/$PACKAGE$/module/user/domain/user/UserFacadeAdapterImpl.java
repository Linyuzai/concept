package $PACKAGE$.module.user.domain.user;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.module.user.domain.user.view.UserVO;
import org.springframework.stereotype.Component;

/**
 * 用户领域模型转换适配器实现
 */
@Component
public class UserFacadeAdapterImpl implements UserFacadeAdapter {

    @Override
    public UserVO do2vo(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return vo;
    }
}
