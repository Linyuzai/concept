package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DefaultConnectionSelectorAdapter implements ConnectionSelectorAdapter {

    private List<ConnectionSelector> selectors;

    @Override
    public ConnectionSelector getConnectionSelector(Message message) {
        for (ConnectionSelector selector : selectors) {
            if (selector.support(message)) {
                return selector;
            }
        }
        return null;
    }
}
