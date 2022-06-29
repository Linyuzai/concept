package com.github.linyuzai.router.core.event;

import com.github.linyuzai.router.core.concept.Router;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 路由定位事件
 */
@Getter
@AllArgsConstructor
public class RouterLocateEvent implements RouterEvent {

    private Router.Location location;

    private Router router;
}
