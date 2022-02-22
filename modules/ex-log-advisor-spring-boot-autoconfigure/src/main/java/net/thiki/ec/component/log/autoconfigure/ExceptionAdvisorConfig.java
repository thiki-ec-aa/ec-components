package net.thiki.ec.component.log.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@ConditionalOnMissingBean(AssertionExceptionHandler.class)
public class ExceptionAdvisorConfig {

    @Bean
    AssertionExceptionHandler assertionExceptionHandler(){
        return new AssertionExceptionHandler();
    }
}
