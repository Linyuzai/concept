package $PACKAGE$.module.sample.infrastructure.sample.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import $PACKAGE$.basic.boot.mbp.MBPBaseRepository;
import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleImpl;
import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.domain.sample.Samples;
import $PACKAGE$.domain.user.User;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 基于 MBP 的示例存储实现
 */
@Repository
public class MBPSampleRepository extends MBPBaseRepository<Sample, Samples, SamplePO> implements SampleRepository {

    @Autowired
    private SampleMapper pinMapper;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public SamplePO do2po(Sample pin) {
        SamplePO po = new SamplePO();
        po.setId(pin.getId());
        return po;
    }

    @Override
    public Sample po2do(SamplePO po) {
        return new SampleImpl.Builder()
                .id(po.getId())
                .build(validator);
    }

    @Override
    public BaseMapper<SamplePO> getBaseMapper() {
        return sampleMapper;
    }
}
