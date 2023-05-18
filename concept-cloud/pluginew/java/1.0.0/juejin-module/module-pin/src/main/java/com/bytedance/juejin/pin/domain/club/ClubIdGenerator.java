package com.bytedance.juejin.pin.domain.club;

import com.bytedance.juejin.pin.domain.club.view.ClubCreateCommand;
import com.github.linyuzai.domain.core.DomainIdGenerator;

/**
 * 圈子 id 生成器
 */
public interface ClubIdGenerator extends DomainIdGenerator<ClubCreateCommand> {
}
