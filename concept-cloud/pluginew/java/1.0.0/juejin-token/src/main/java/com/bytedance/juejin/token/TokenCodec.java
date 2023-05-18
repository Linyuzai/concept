package com.bytedance.juejin.token;

import com.bytedance.juejin.domain.user.User;

/**
 * Token 编解码
 */
public interface TokenCodec {

    /**
     * 生成 Token
     */
    String encode(User user);

    /**
     * 解析 Token
     */
    User decode(String token);
}
