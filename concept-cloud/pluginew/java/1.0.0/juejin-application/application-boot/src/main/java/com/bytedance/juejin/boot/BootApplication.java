package com.bytedance.juejin.boot;

import com.bytedance.juejin.basic.boot.JuejinBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * 单体应用启动类
 */
@JuejinBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
