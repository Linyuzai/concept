package com.github.linyuzai.connection.loadbalance.core.message.sender;

/**
 * 默认的消息发送器工厂。
 * <p>
 * Default factory of message sender.
 */
public class DefaultMessageSenderFactory extends AbstractMessageSenderFactory {

    @Override
    public MessageSender create(String scope) {
        return new DefaultMessageSender();
    }
}
