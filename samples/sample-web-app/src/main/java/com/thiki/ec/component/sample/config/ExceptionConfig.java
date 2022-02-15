package com.thiki.ec.component.sample.config;

import com.thiki.ec.component.sample.ex.AssertionExceptionHandler;
import net.thiki.ec.component.log.starter.Greeter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@ConditionalOnMissingBean(annotation = ControllerAdvice.class)
public class ExceptionConfig {

    @Autowired
    private Greeter greeter;

    @Bean
    AssertionExceptionHandler assertionExceptionHandler(){
        greeter.greet();
        return new AssertionExceptionHandler(greeter);
    }
}
