package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.message.Message;

public interface ConnectionSelectorAdapter {

    ConnectionSelector getConnectionSelector(Message message);
}
