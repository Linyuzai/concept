package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.sample.SampleImpl;
import $PACKAGE$.domain.user.User;
import $PACKAGE$.module.pin.domain.sample.view.SampleCreateCommand;
import $PACKAGE$.module.pin.domain.sample.view.SampleQuery;
import $PACKAGE$.module.pin.domain.sample.view.SampleUserVO;
import $PACKAGE$.module.pin.domain.sample.view.SampleVO;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 沸点领域模型转换适配器实现
 */
@Component
public class SampleFacadeAdapterImpl implements SampleFacadeAdapter {

    @Autowired
    private SampleIdGenerator sampleIdGenerator;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public Sample from(SampleCreateCommand create, User user) {
        String id = sampleIdGenerator.generateId(create);
        return new SampleImpl.Builder()
                .id(id)
                .build(validator);
    }

    @Override
    public SampleVO do2vo(Sample sample) {
        SampleVO vo = new SampleVO();
        vo.setId(sample.getId());
        return vo;
    }

    @Override
    public Conditions toConditions(SampleQuery query) {
        return new LambdaConditions().equal(Sample::getSample, query.getSample());
    }

    private SampleUserVO getUser(User user) {
        SampleUserVO vo = new SampleUserVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return vo;
    }
}
