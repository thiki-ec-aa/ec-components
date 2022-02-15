package net.thiki.ec.component.log.autoconfigure;

import net.thiki.ec.component.log.autoconfigure.AssertionExceptionHandler;
import net.thiki.ec.component.log.starter.Greeter;
import net.thiki.ec.component.log.starter.GreetingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@ConditionalOnMissingBean(annotation = ControllerAdvice.class)
public class ExceptionConfig {

    @Bean
    AssertionExceptionHandler assertionExceptionHandler(){
        return new AssertionExceptionHandler();
    }
}
