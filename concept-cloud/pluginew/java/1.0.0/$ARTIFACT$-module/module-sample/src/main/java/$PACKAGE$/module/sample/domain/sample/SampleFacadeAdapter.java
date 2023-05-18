package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.domain.user.User;
import $PACKAGE$.module.sample.domain.sample.view.SampleCreateCommand;
import $PACKAGE$.module.sample.domain.sample.view.SampleQuery;
import $PACKAGE$.module.sample.domain.sample.view.SampleVO;
import com.github.linyuzai.domain.core.condition.Conditions;

/**
 * 领域模型转换适配器
 */
public interface SampleFacadeAdapter {

    /**
     * 创建视图转领域模型
     */
    Sample from(SampleCreateCommand create, User user);

    /**
     * 领域模型转视图
     */
    SampleVO do2vo(Sample sample);

    /**
     * 查询转条件
     */
    Conditions toConditions(SampleQuery query);
}
