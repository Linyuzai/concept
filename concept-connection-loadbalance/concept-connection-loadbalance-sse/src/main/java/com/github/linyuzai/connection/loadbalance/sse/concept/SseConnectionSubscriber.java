package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ProtocolConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer.LB_HOST_PORT;

/**
 * SSE 连接订阅者。
 * <p>
 * SSE connection subscriber.
 */
@Getter
@Setter
public abstract class SseConnectionSubscriber<T extends SseConnection>
        extends ProtocolConnectionSubscriber<T> {

    private String protocol = "http";

    private String endpoint = SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT;

    //private Map<String, Boolean> connectingServers = new ConcurrentHashMap<>();

    /*@Override
    public boolean interceptSubscribe(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        if (server == null) {
            return false;
        }
        String url = ConnectionServer.url(server);
        if (connectingServers.getOrDefault(url, Boolean.FALSE)) {
            return true;
        } else {
            connectingServers.put(url, Boolean.TRUE);
            concept.getEventPublisher().register(new ConnectingListener(url));
            return false;
        }
    }*/

    @Override
    public Map<String, String> getParams(ConnectionLoadBalanceConcept concept) {
        ConnectionServer local = concept.getConnectionServerManager().getLocal();
        String params = ConnectionServer.url(local);
        Map<String, String> map = new LinkedHashMap<>();
        map.put(LB_HOST_PORT, params);
        return map;
    }

    public abstract String getType();

    /*@Deprecated
    @Getter
    @RequiredArgsConstructor
    public class ConnectingListener implements SseEventListener {

        private final String connectingServer;

        @Override
        public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
            if (event instanceof ConnectionEstablishEvent) {
                Connection connection = ((ConnectionEstablishEvent) event).getConnection();
                if (connection.isSubscriberType()) {
                    ConnectionServer server = (ConnectionServer) connection.getMetadata()
                            .get(ConnectionServer.class);
                    String url = ConnectionServer.url(server);
                    connectingServers.remove(url);
                    concept.getEventPublisher().unregister(this);
                }
            }
        }
    }*/
}
