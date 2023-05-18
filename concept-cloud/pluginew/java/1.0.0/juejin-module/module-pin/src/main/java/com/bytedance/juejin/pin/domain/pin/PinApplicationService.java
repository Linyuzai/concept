package com.bytedance.juejin.pin.domain.pin;

import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.pin.PinRepository;
import com.bytedance.juejin.domain.pin.PinService;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.pin.view.PinCreateCommand;
import com.bytedance.juejin.pin.domain.pin.view.PinDeleteCommand;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 沸点应用服务
 */
@Service
public class PinApplicationService {

    @Autowired
    private PinService pinService;

    @Autowired
    private PinFacadeAdapter pinFacadeAdapter;

    @Autowired
    private PinRepository pinRepository;

    /**
     * 创建（发布）沸点
     */
    public void create(PinCreateCommand create, User user) {
        Pin pin = pinFacadeAdapter.from(create, user);
        pinService.create(pin, user);
    }

    /**
     * 删除沸点
     */
    public void delete(PinDeleteCommand delete, User user) {
        Pin pin = pinRepository.get(delete.getId());
        if (pin == null) {
            throw new DomainNotFoundException(Pin.class, delete.getId());
        }
        pinService.delete(pin, user);
    }
}
