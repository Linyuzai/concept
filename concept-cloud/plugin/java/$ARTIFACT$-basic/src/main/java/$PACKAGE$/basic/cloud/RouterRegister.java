package $PACKAGE$.basic.cloud;

import org.springframework.cloud.client.discovery.event.InstancePreRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class RouterRegister {

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
    }
}
