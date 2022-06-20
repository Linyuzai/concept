package com.github.linyuzai.router.core.event;

import com.github.linyuzai.router.core.concept.Router;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouterMatchedEvent implements RouterEvent {

    private Router router;
}
