package com.bytedance.juejin.cloud;

import com.bytedance.juejin.basic.cloud.JuejinCloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * 微服务沸点启动类
 */
@JuejinCloudApplication
public class CloudPinApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPinApplication.class, args);
    }
}
