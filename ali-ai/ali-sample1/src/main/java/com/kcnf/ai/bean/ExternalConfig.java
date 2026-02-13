package com.kcnf.ai.bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
        value = "file:${app.config.path:/data/kcnf/ali-ai.properties}",
        ignoreResourceNotFound = true
)
public class ExternalConfig {
}
