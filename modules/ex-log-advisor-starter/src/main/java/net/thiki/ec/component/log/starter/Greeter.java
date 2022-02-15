package net.thiki.ec.component.log.starter;

public class Greeter {


    private final GreetingConfig config;

    public Greeter(GreetingConfig greetingConfig) {
        this.config = greetingConfig;
    }
    public void greet(){
        System.out.println("hello " + config);
    }

}
