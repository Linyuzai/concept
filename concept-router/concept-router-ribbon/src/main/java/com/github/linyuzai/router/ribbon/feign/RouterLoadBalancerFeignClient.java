/*
package com.github.linyuzai.router.ribbon.feign;

import com.github.linyuzai.router.core.utils.Reflector;
import com.netflix.client.ClientException;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import feign.Client;
import feign.Request;
import feign.Response;
import lombok.SneakyThrows;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;

@Deprecated
public class RouterLoadBalancerFeignClient extends LoadBalancerFeignClient {

    static final Request.Options DEFAULT_OPTIONS = new Request.Options();

    private final Client delegate;

    private final CachingSpringLoadBalancerFactory lbClientFactory;

    private final SpringClientFactory clientFactory;

    private final Field responseField;

    @SneakyThrows
    public RouterLoadBalancerFeignClient(LoadBalancerFeignClient client) {
        super(null, null, null);
        Reflector reflector = new Reflector(LoadBalancerFeignClient.class);
        this.delegate = (Client) reflector.field(Client.class).get(client);
        this.lbClientFactory = (CachingSpringLoadBalancerFactory) reflector.field(CachingSpringLoadBalancerFactory.class).get(client);
        this.clientFactory = (SpringClientFactory) reflector.field(SpringClientFactory.class).get(client);
        responseField = new Reflector("org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer.RibbonResponse")
                .field(Response.class);
    }

    static URI cleanUrl(String originalUrl, String host) {
        String newUrl = originalUrl;
        if (originalUrl.startsWith("https://")) {
            newUrl = originalUrl.substring(0, 8)
                    + originalUrl.substring(8 + host.length());
        } else if (originalUrl.startsWith("http")) {
            newUrl = originalUrl.substring(0, 7)
                    + originalUrl.substring(7 + host.length());
        }
        StringBuffer buffer = new StringBuffer(newUrl);
        if ((newUrl.startsWith("https://") && newUrl.length() == 8)
                || (newUrl.startsWith("http://") && newUrl.length() == 7)) {
            buffer.append("/");
        }
        return URI.create(buffer.toString());
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        try {
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            URI uriWithoutHost = cleanUrl(request.url(), clientName);
            RouterFeignLoadBalancer.RouterRibbonRequest ribbonRequest = new RouterFeignLoadBalancer.RouterRibbonRequest(
                    this.delegate, request, uriWithoutHost);

            IClientConfig requestConfig = getClientConfig(options, clientName);
            return getResponse(lbClient(clientName)
                    .executeWithLoadBalancer(ribbonRequest, requestConfig));
        } catch (ClientException e) {
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public Response getResponse(Object object) {
        return (Response) responseField.get(object);
    }

    IClientConfig getClientConfig(Request.Options options, String clientName) {
        IClientConfig requestConfig;
        if (options == DEFAULT_OPTIONS) {
            requestConfig = this.clientFactory.getClientConfig(clientName);
        } else {
            requestConfig = new FeignOptionsClientConfig(options);
        }
        return requestConfig;
    }

    protected IOException findIOException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof IOException) {
            return (IOException) t;
        }
        return findIOException(t.getCause());
    }

    public Client getDelegate() {
        return this.delegate;
    }

    private FeignLoadBalancer lbClient(String clientName) {
        return this.lbClientFactory.create(clientName);
    }

    static class FeignOptionsClientConfig extends DefaultClientConfigImpl {

        FeignOptionsClientConfig(Request.Options options) {
            setProperty(CommonClientConfigKey.ConnectTimeout,
                    options.connectTimeoutMillis());
            setProperty(CommonClientConfigKey.ReadTimeout, options.readTimeoutMillis());
        }

        @Override
        public void loadProperties(String clientName) {

        }

        @Override
        public void loadDefaultValues() {

        }

    }
}
*/
