package com.p2p.node.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "node")
@Data
public class NodeConfig {

    private String storage;
    private List<String> peers;
}
