package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.module.sample.domain.sample.view.SampleCreateCommand;
import com.github.linyuzai.domain.core.DomainIdGenerator;

/**
 * 示例 id 生成器
 */
public interface SampleIdGenerator extends DomainIdGenerator<SampleCreateCommand> {
}
