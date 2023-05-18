package com.bytedance.juejin.notification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @GetMapping("notification")
    public String test() {
        return "notification";
    }
}
