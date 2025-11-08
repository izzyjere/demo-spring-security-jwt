package com.example.demo.config;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private String[] authWhitelist;
    private String[] privileges;

    @Bean
    public ModelMapper modelMapper() {
        var mapper = new ModelMapper();
        mapper.registerModule(new RecordModule());
        return mapper;
    }
}
