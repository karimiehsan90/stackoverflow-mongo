package com.journaldev.bootifulmongodb.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "project")
@Getter
@Setter
public class Conf {
    private SMTPConf smtp;
}
