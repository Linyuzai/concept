package com.github.linyuzai.router.core.event;

import com.github.linyuzai.router.core.concept.Router;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouterMatchEvent implements RouterEvent {

    private Router.Source source;

    private Router router;
}
