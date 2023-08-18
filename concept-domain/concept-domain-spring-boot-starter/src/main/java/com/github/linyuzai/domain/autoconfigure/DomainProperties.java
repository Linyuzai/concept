package com.github.linyuzai.domain.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("concept.domain")
public class DomainProperties {

    private RecyclerProperties recycler = new RecyclerProperties();

    @Data
    public static class RecyclerProperties {

        private boolean enabled = false;

        private boolean threadLocalAutoRecycle = true;
    }
}
