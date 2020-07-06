package com.journaldev.bootifulmongodb.conf;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMTPConf {
    private String username;
    private String from;
    private String password;
}
