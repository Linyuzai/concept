package com.github.linyuzai.domain.mbp;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.linyuzai.domain.core.DomainIdGenerator;

/**
 * 基于 MyBatis-Plus 的 id 生成器
 */
public class MBPDomainIdGenerator<T> implements DomainIdGenerator<T> {

    @Override
    public String generateId(T object) {
        return IdWorker.getIdStr();
    }
}
