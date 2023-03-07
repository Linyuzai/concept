package com.github.linyuzai.concept.sample.feizhu;

/**
 * 支付服务类 (方便候选人本地阅读代码, 不需要动)
 */
public class PayService {

    /**
     * 信用支付
     * @param orderInfo
     */
    public long payOfCredit(OrderInfo orderInfo) {
        //  信用支付逻辑，候选人不用理会支付内部逻辑 ...
        return 1;
    }

    /**
     * 支付宝支付
     * @param orderInfo
     */
    public long payOfAliPay(OrderInfo orderInfo) {
        //  支付宝支付逻辑，候选人不用理会支付内部逻辑 ...
        return 1;
    }

    /**
     * 未支付成功后关闭
     * @param orderInfo
     */
    public void notPayedToClose(OrderInfo orderInfo) {
        //  支付宝支付逻辑，候选人不用理会支付内部逻辑 ...
    }
}
