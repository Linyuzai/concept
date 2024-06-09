package com.github.linyuzai.plugin.autoconfigure.preperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.plugin")
public class PluginConceptProperties {
}
