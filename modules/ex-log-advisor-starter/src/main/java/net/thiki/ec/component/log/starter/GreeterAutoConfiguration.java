package net.thiki.ec.component.log.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Greeter.class)
@EnableConfigurationProperties(GreetingConfig.class)
public class GreeterAutoConfiguration {

    @Autowired
    private GreetingConfig greetingConfig;

//    @Bean
//    @ConditionalOnMissingBean
//    public GreetingConfig greeterConfig() {
//
//        String userName = greetingConfig.getUserName() == null
//          ? System.getProperty("user.name")
//          : greetingConfig.getUserName();
//
//        // ..
//
//        GreetingConfig greetingConfig = new GreetingConfig();
//        greetingConfig.setUserName(userName);
//        // ...
//        return greetingConfig;
//    }

    @Bean
    @ConditionalOnMissingBean
    public Greeter greeter(GreetingConfig greetingConfig) {
        return new Greeter(greetingConfig);
    }
}