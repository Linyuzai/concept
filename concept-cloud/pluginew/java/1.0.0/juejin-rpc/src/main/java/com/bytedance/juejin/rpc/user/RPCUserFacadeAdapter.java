package com.bytedance.juejin.rpc.user;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.domain.user.UserImpl;
import com.bytedance.juejin.rpc.RemoteObjectFacadeAdapter;
import com.github.linyuzai.domain.core.DomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用户领域模型和用户远程对象转换适配器
 */
public interface RPCUserFacadeAdapter extends RemoteObjectFacadeAdapter<User, UserRO> {
}
