package com.github.linyuzai.concept.sample.feizhu;

/**
 * 订单服务类 (方便候选人本地阅读代码, 不需要动)
 */
public class OrderService {
    public OrderInfo getOrderInfo(String orderNo) {
        return new OrderInfo();
    }

    public void updateOrderStatus(String orderNo, int status) {
        // 更新订单逻辑
    }

}
