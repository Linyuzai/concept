package com.github.linyuzai.concept.sample.feizhu;

/**
 * 【建议拷贝到本地环境编写后贴回考试系统，考试结束会自动收卷，请自行定时将代码拷回到系统中】
 * <p>
 * 业务背景：这是一个可以售卖多种类目商品（酒店、门票、签证）的旅游类电商系统。 这个类中主要是接收到订单状态变更消息的逻辑处理。目前有订单支付（这里的支付是直接扣款，有免密协议）和订单完成两部分逻辑。
 * <p>
 * 题目要求：希望你能够将这段代码进行重构，目标：拥有好的可读性（清晰、直观、简洁）、可扩展性（面向未来商品类的扩展、订单的状态扩展、公共逻辑扩展（比如增加入参、出参日志打印和异常控制逻辑加入））
 * 题目判分的优先级： 设计思路 > 代码结构 > 规范性 > 可读性 > 完整度
 * 参考建议：
 * 1. 可读性：
 * 1）代码的注释、命名
 * 2）枚举类
 * 3）重复代码归纳合并
 * 2. 扩展性考虑：
 * 1）设计模式 工厂、模板方法
 * 2）事件驱动方式 比如 SpringEvent 或者 EventBus。没有用过可以上网查哦。（代码中体现出结构和大概逻辑即可）
 */
public class OrderStatusMQListener {
    private OrderService orderService = new OrderService();
    private PayService payService = new PayService();
    private NotifyService notifyService = new NotifyService();

    public void consume(String orderNo) {
        final OrderInfo orderInfo = orderService.getOrderInfo(orderNo);
        // 订单业务类型
        final String orderBizType = orderInfo.getOrderBizType();
        // 订单状态
        final int status = orderInfo.getStatus();

        if (1 == status) { // 创建，待支付执行支付
            // 酒店订单
            if ("hotel".equals(orderBizType)) {
                // 酒店订单支付前的检查 ......
                payPrdOfHotelVerify(orderInfo);
                // 信用支付
                long payId = payService.payOfCredit(orderInfo);
                // 如果支付失败关闭订单
                if (payId < 0) {
                    payService.notPayedToClose(orderInfo);
                }
            }
            // 门票订单
            else if ("tickets".equals(orderBizType)) {
                long payId = -1;
                for (int i = 0; i < 3; i++) { // 如果失败有3次重试
                    // 信用支付
                    payId = payService.payOfCredit(orderInfo);
                    // payId >0 代表支付成功
                    if (payId > 0) {
                        break;
                    }
                }
                // 如果支付失败关闭订单
                if (payId < 0) {
                    payService.notPayedToClose(orderInfo);
                }
            }
            // 签证订单
            else if ("visa".equals(orderBizType)) {
                // 三方支付
                long payId = payService.payOfAliPay(orderInfo);
                // 如果支付失败关闭订单
                if (payId < 0) {
                    payService.notPayedToClose(orderInfo);
                }
                // 如果支付成功
                else {
                    // 签证订单支付后业务上要处理的一些逻辑
                    payAfterOfVisaBusiness(payId, orderInfo);
                }
            }
        } else if (4 == status) { // 订单完成
            // 酒店订单
            if ("hotel".equals(orderBizType)) {
                // 更新订单状态
                orderService.updateOrderStatus(orderNo, 4);
                // 给运营发钉钉通知
                notifyService.dingding("发送到酒店运营群里的内容");
                // 给用户发短信
                notifyService.sms("发送到给酒店用户的内容");
            }
            // 门票订单
            else if ("tickets".equals(orderBizType)) {
                // 更新订单状态
                orderService.updateOrderStatus(orderNo, 4);
                // 给运营发钉钉通知
                notifyService.dingding("发送到门票运营群里的内容");
                // 给用户发短信
                notifyService.sms("发送到给门票用户的内容");
                // 给用户发微信通知
                notifyService.wechat("发送到给门票用户的内容");
            }
            // 签证订单
            else if ("visa".equals(orderBizType)) {
                // 更新订单状态
                orderService.updateOrderStatus(orderNo, 4);
                // 给运营发钉钉通知
                notifyService.dingding("发送到签证运营群里的内容");
                // 给用户发微信通知
                notifyService.wechat("发送到给签证用户的内容");
            }
        }
        // 同意退款 待未来业务扩展......
        else if (9 == status) {

        }

    }

    private void payPrdOfHotelVerify(OrderInfo orderInfo) {
        // 各种酒店订单支付前的检查 ...... 候选人不用关系内部逻辑
    }

    private void payAfterOfVisaBusiness(long payId, OrderInfo orderInfo) {
        // 各种签证订单支付后的逻辑 ...... 候选人不用关系内部逻辑
    }

}
