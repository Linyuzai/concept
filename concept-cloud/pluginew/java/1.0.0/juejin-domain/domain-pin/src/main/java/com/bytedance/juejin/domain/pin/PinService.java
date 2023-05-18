package com.bytedance.juejin.domain.pin;

import com.bytedance.juejin.domain.pin.event.PinCreatedEvent;
import com.bytedance.juejin.domain.pin.event.PinDeletedEvent;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 沸点领域服务
 */
@Service
public class PinService {

    /**
     * 沸点存储
     */
    @Autowired
    private PinRepository pinRepository;

    /**
     * 领域事件发布器
     */
    @Autowired
    private DomainEventPublisher eventPublisher;

    /**
     * 添加（发布）一条沸点
     */
    public void create(Pin pin, User user) {
        pinRepository.create(pin);
        //发布沸点添加（发布）事件
        eventPublisher.publish(new PinCreatedEvent(pin, user));
    }

    /**
     * 删除一条沸点
     */
    public void delete(Pin pin, User user) {
        //删除沸点
        pinRepository.delete(pin);
        eventPublisher.publish(new PinDeletedEvent(pin, user));
    }
}
