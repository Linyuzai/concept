package $PACKAGE$.module.user.infrastructure.user.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import $PACKAGE$.basic.boot.mbp.MBPBaseRepository;
import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.UserImpl;
import $PACKAGE$.domain.user.UserRepository;
import $PACKAGE$.domain.user.Users;
import com.github.linyuzai.domain.core.DomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 基于 MBP 的用户存储实现
 */
@Repository
public class MBPUserRepository extends MBPBaseRepository<User, Users, UserPO> implements UserRepository {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DomainValidator validator;

    @Override
    public UserPO do2po(User user) {
        UserPO po = new UserPO();
        po.setId(user.getId());
        po.setUsername(user.getUsername());
        po.setPassword(user.getPassword());
        po.setNickname(user.getNickname());
        po.setAvatar(user.getAvatar());
        po.setEnabled(true);
        po.setCreateTime(user.getCreateTime());
        return po;
    }

    @Override
    public User po2do(UserPO po) {
        return new UserImpl.Builder()
                .id(po.getId())
                .username(po.getUsername())
                .password(po.getPassword())
                .nickname(po.getNickname())
                .avatar(po.getAvatar())
                .enabled(po.getEnabled())
                .createTime(po.getCreateTime())
                .build(validator);
    }

    @Override
    public BaseMapper<UserPO> getBaseMapper() {
        return userMapper;
    }
}
