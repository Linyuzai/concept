package $PACKAGE$.rpc.user;

import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

/**
 * 用户远程对象 remote object
 */
@Data
public class UserRO implements Identifiable {

    protected String id;

    protected String username;

    protected String password;

    protected String nickname;

    protected String avatar;

    protected Boolean enabled;

    protected Long createTime;
}
