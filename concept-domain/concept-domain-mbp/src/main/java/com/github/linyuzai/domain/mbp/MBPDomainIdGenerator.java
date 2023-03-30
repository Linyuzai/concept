package com.github.linyuzai.domain.mbp;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.linyuzai.domain.core.DomainIdGenerator;

/**
 * 基于 MyBatis-Plus 的 id 生成器
 */
public abstract class MBPDomainIdGenerator implements DomainIdGenerator<Object> {

    @Override
    public String generateId(Object object) {
        return IdWorker.getIdStr();
    }
}
