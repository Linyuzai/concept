package com.bytedance.juejin.pin.domain.pin;

import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.pin.PinRepository;
import com.bytedance.juejin.pin.domain.pin.view.PinQuery;
import com.bytedance.juejin.pin.domain.pin.view.PinVO;
import com.github.linyuzai.domain.core.page.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 沸点查询实现
 */
@Component
public class PinSearcherImpl implements PinSearcher {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private PinFacadeAdapter pinFacadeAdapter;

    @Override
    public PinVO get(String id) {
        Pin pin = pinRepository.get(id);
        if (pin == null) {
            return null;
        }
        return pinFacadeAdapter.do2vo(pin);
    }

    @Override
    public Pages<PinVO> page(PinQuery query, Pages.Args page) {
        return pinRepository
                .page(pinFacadeAdapter.toConditions(query), page)
                .map(pinFacadeAdapter::do2vo);
    }
}
