package com.bytedance.juejin.basic.cloud.register;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.cloud.client.discovery.event.InstancePreRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
public class MetadataRegister {

    private final List<GroupedOpenApi> groupedOpenApis;

    /**
     * 监听服务注册前置事件
     */
    @EventListener
    public void register(InstancePreRegisteredEvent event) throws Exception {
        //读取 router.properties 资源文件
        ClassPathResource resource = new ClassPathResource("router.properties");
        //加载到 Properties 中
        Properties properties = new Properties();
        try (InputStream is = resource.getInputStream()) {
            properties.load(is);
        }
        //获得 routers 值
        String routers = properties.getProperty("routers");
        //写入 metadata 中
        Map<String, String> metadata = event.getRegistration().getMetadata();
        metadata.put("routers", routers);

        //swagger的group和name的对应关系
        List<String> groupAndNameList = new ArrayList<>();
        for (GroupedOpenApi api : groupedOpenApis) {
            String name = api.getDisplayName() == null ? api.getGroup() : api.getDisplayName();
            groupAndNameList.add(api.getGroup() + ":" + name);
        }

        metadata.put("swagger", String.join(",", groupAndNameList));
    }
}
