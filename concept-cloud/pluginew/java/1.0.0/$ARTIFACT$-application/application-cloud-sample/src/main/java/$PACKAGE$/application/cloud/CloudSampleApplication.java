package $PACKAGE$.application.cloud;

import $PACKAGE$.basic.cloud.$CLASS$CloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * 微服务示例启动类
 */
@$CLASS$CloudApplication
public class CloudSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSampleApplication.class, args);
    }
}
