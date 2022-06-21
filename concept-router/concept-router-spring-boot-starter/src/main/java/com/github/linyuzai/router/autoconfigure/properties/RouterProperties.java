package com.github.linyuzai.router.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.router")
public class RouterProperties {

    private boolean enabled = true;

    private ManagementProperties management = new ManagementProperties();

    @Data
    public static class ManagementProperties {

        private boolean enabled = true;
    }
}
