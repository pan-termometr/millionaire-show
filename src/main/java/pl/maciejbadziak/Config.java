package pl.maciejbadziak;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class Config {
    @Bean
    Random random() {
        return new Random();
    }
}
