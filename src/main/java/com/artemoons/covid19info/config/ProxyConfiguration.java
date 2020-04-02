//package com.artemoons.covid19info.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.telegram.telegrambots.bots.DefaultBotOptions;
//
//import java.net.Authenticator;
//import java.net.PasswordAuthentication;
//
//@Configuration
//@RequiredArgsConstructor
//@ConditionalOnProperty(value = {"host", "port", "type"}, prefix = "telegram.proxy")
//@Import(ProxyProperties.class)
//public class ProxyConfiguration {
//
//    private final ProxyProperties properties;
//
//    @Bean
//    public DefaultBotOptions proxyOptions() {
//        if (properties.hasAuthData()) {
//            Authenticator.setDefault(new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(properties.getUser(), properties.getPassword().toCharArray());
//                }
//            });
//        }
//        DefaultBotOptions botOptions = new DefaultBotOptions();
//        botOptions.setProxyHost(properties.getHost());
//        botOptions.setProxyPort(properties.getPort());
//        botOptions.setProxyType(properties.getType());
//        return botOptions;
//    }
//
//}
