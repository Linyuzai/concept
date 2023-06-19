package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

/**
 * 基于 {@link WebSocketContainer} 的连接订阅者
 */
@NoArgsConstructor
public abstract class ContainerWebSocketConnectionSubscriber<T extends WebSocketConnection>
        extends WebSocketConnectionSubscriber<T> implements ServletContextAware {

    private volatile WebSocketContainer container;

    public WebSocketContainer getContainer() {
        if (container == null) {
            synchronized (this) {
                if (container == null) {
                    container = ContainerProvider.getWebSocketContainer();
                }
            }
        }
        return container;
    }

    @Override
    public void setServletContext(@NonNull ServletContext servletContext) {
        if (container == null) {
            container = (WebSocketContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer");
        }
    }
}
