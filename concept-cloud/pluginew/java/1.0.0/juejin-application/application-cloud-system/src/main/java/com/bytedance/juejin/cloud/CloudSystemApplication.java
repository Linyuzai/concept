package com.bytedance.juejin.cloud;

import com.bytedance.juejin.basic.cloud.JuejinCloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * 微服务用户和消息启动类
 */
@JuejinCloudApplication
public class CloudSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSystemApplication.class, args);
    }
}
