
# A starter for the exception and log advisor for spring boot **web app**

[ref1:spring-boot-custom-starter](https://www.baeldung.com/spring-boot-custom-starter)

[ref2:spring-boot-custom-auto-configuration](https://www.baeldung.com/spring-boot-custom-auto-configuration)

## usage

1. add a dependency in your pom:

```xml
    <dependencies>
        <dependency>
            <groupId>net.thiki.ec.component</groupId>
            <artifactId>ex-log-advisor-spring-boot-starter</artifactId>
            <version>0.1.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

2. make sure no exception handler(annotated by @ExceptionHandler) for RuntimeException to avoid override the configuration in AssertionExceptionHandler.
