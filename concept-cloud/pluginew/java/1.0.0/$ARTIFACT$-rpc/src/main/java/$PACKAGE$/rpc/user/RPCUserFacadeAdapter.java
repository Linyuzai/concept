package $PACKAGE$.rpc.user;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.UserImpl;
import $PACKAGE$.rpc.RemoteObjectFacadeAdapter;
import com.github.linyuzai.domain.core.DomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用户领域模型和用户远程对象转换适配器
 */
public interface RPCUserFacadeAdapter extends RemoteObjectFacadeAdapter<User, UserRO> {
}
