package $PACKAGE$.domain.sample;

import $PACKAGE$.domain.sample.event.SampleCreatedEvent;
import $PACKAGE$.domain.sample.event.SampleDeletedEvent;
import $PACKAGE$.domain.sample.event.SampleUpdatedEvent;
import $PACKAGE$.domain.user.User;
import com.github.linyuzai.domain.core.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 示例领域服务
 */
@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private DomainEventPublisher eventPublisher;

    /**
     * 创建
     */
    public void create(Sample sample, User user) {
        sampleRepository.create(sample);
        eventPublisher.publish(new SampleCreatedEvent(sample, user));
    }

    /**
     * 更新
     */
    public void update(Sample newSample, Sample oldSample, User user) {
        sampleRepository.update(newSample);
        eventPublisher.publish(new SampleUpdatedEvent(newSample, oldSample, user));
    }

    /**
     * 删除
     */
    public void delete(Sample sample, User user) {
        sampleRepository.delete(sample);
        eventPublisher.publish(new SampleDeletedEvent(sample, user));
    }
}
