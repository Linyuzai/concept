package $PACKAGE$.module.user.infrastructure.user.mbp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 用户数据模型
 */
@Data
@TableName("t_user")
class UserPO implements Identifiable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private Boolean enabled;

    private Date createTime;
}
