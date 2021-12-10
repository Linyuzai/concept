package com.github.linyuzai.download.autoconfigure.properties;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "concept.download")
public class DownloadConceptProperties extends DownloadConfiguration {

}
