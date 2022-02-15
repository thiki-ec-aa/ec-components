package net.thiki.ec.component.log.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "thiki.greeter")
public class GreetingConfig {

    private String userName;
    private String morningMessage;
    private String afternoonMessage;
    private String eveningMessage;
    private String nightMessage;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMorningMessage() {
        return morningMessage;
    }

    public void setMorningMessage(String morningMessage) {
        this.morningMessage = morningMessage;
    }

    public String getAfternoonMessage() {
        return afternoonMessage;
    }

    public void setAfternoonMessage(String afternoonMessage) {
        this.afternoonMessage = afternoonMessage;
    }

    public String getEveningMessage() {
        return eveningMessage;
    }

    public void setEveningMessage(String eveningMessage) {
        this.eveningMessage = eveningMessage;
    }

    public String getNightMessage() {
        return nightMessage;
    }

    @Override
    public String toString() {
        return "GreetingConfig{" +
                "userName='" + userName + '\'' +
                ", morningMessage='" + morningMessage + '\'' +
                ", afternoonMessage='" + afternoonMessage + '\'' +
                ", eveningMessage='" + eveningMessage + '\'' +
                ", nightMessage='" + nightMessage + '\'' +
                '}';
    }

    public void setNightMessage(String nightMessage) {
        this.nightMessage = nightMessage;
    }
}
