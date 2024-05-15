package com.github.linyuzai.connection.loadbalance.core.message.sender;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;

/**
 * 消息发送器工厂抽象类。
 * 支持连接域。
 * <p>
 * Abstract class of factory of message sender.
 * Support scoped.
 */
public abstract class AbstractMessageSenderFactory extends AbstractScopedFactory<MessageSender>
        implements MessageSenderFactory {
}
