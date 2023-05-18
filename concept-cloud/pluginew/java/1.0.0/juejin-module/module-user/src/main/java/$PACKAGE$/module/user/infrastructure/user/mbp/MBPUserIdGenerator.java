package $PACKAGE$.module.user.infrastructure.user.mbp;

import $PACKAGE$.domain.user.UserIdGenerator;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;

/**
 * 基于 MBP id 生成器的 用户 id 生成器
 */
public class MBPUserIdGenerator extends MBPDomainIdGenerator<Object> implements UserIdGenerator {
}
