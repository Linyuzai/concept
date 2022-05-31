package com.github.linyuzai.connection.loadbalance.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.connection")
public class ConnectionLoadBalanceProperties {

}
