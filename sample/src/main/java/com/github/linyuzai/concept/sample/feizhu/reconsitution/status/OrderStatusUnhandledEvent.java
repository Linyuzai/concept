package com.github.linyuzai.concept.sample.feizhu.reconsitution.status;

import com.github.linyuzai.concept.sample.feizhu.OrderInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderStatusUnhandledEvent {

    private final OrderInfo orderInfo;
}
