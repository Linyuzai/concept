package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeadMessageEvent implements MessageEvent {

    private Message message;
}
