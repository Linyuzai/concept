package $PACKAGE$.gateway.swagger;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CloudSpringDocAggregation {

    private final DiscoveryClient discoveryClient;

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    @EventListener
    public void updateSpringDoc(HeartbeatEvent event) {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        //获得服务名
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            //获得服务实例
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            if (instances.isEmpty()) {
                continue;
            }
            //这里直接拿第一个
            ServiceInstance instance = instances.get(0);
            //获得 metadata 中的 swagger
            String swaggerMetadata = instance.getMetadata()
                    .getOrDefault("swagger", "");
            String[] groupAndNames = swaggerMetadata.split(",");
            for (String groupAndName : groupAndNames) {
                if (groupAndName.isEmpty()) {
                    continue;
                }
                String[] split = groupAndName.split(":");
                String group = split[0];
                String name = split[1];
                String url = group + "/v3/api-docs/" + group;
                urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(group, url, name));
            }
        }

        swaggerUiConfigProperties.setUrls(urls);
    }
}
