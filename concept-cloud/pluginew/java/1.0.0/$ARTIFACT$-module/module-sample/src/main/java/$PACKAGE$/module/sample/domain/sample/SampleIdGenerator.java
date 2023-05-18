package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.module.sample.domain.sample.view.PinCreateCommand;
import com.github.linyuzai.domain.core.DomainIdGenerator;

/**
 * 示例 id 生成器
 */
public interface SampleIdGenerator extends DomainIdGenerator<SampleCreateCommand> {
}
