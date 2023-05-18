package $PACKAGE$.module.sample.infrastructure.sample.mbp;

import $PACKAGE$.module.sample.domain.sample.SampleIdGenerator;
import $PACKAGE$.module.sample.domain.sample.view.SampleCreateCommand;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;
import org.springframework.stereotype.Component;

/**
 * 基于 MBP id 生成器 的 示例 id 生成器
 */
@Component
public class MBPSampleIdGenerator extends MBPDomainIdGenerator<SampleCreateCommand> implements SampleIdGenerator {
}
