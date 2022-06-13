package net.thiki.ec.component.log.autoconfigure;

import org.apache.logging.log4j.spi.StandardLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Locale;

@Configuration
@ConditionalOnMissingBean(AssertionExceptionHandler.class)
public class ExceptionAdvisorConfig {

    @Value("${logger.assertion-exception.level:DEBUG}")
    private String aeLevel;
    @Value("${logger.runtime-exception.level:ERROR}")
    private String reLevel;

    @Bean
    AssertionExceptionHandler assertionExceptionHandler(){
        return new AssertionExceptionHandler(
                StandardLevel.valueOf(aeLevel.toUpperCase()),
                StandardLevel.valueOf(reLevel.toUpperCase())
        );
    }
}
