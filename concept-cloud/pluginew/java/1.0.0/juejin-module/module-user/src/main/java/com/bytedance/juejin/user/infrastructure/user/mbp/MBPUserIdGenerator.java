package com.bytedance.juejin.user.infrastructure.user.mbp;

import com.bytedance.juejin.domain.user.UserIdGenerator;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;

/**
 * 基于 MBP id 生成器的 用户 id 生成器
 */
public class MBPUserIdGenerator extends MBPDomainIdGenerator<Object> implements UserIdGenerator {
}
