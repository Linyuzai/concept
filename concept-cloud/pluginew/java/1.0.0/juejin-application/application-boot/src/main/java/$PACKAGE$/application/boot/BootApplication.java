package $PACKAGE$.application.boot;

import $PACKAGE$.basic.boot.$CLASS$BootApplication;
import org.springframework.boot.SpringApplication;

/**
 * 单体应用启动类
 */
@$CLASS$BootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
