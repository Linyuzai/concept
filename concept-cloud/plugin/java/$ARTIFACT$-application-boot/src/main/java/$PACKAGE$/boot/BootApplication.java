package $PACKAGE$.boot;

import $PACKAGE$.basic.$CLASS$BootApplication;
import org.springframework.boot.SpringApplication;

/**
 * 单体应用启动模块示例
 */
@$CLASS$BootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
