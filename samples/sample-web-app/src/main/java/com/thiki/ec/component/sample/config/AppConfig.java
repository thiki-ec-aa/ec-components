package com.thiki.ec.component.sample.config;

import net.thiki.ec.component.log.autoconfigure.ExceptionConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration

// can disable ExceptionConfig in any @Configuration bean.
//@EnableAutoConfiguration(exclude = ExceptionConfig.class)
public class AppConfig {

}
