package sis.local;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("no-docker")
public class NoDockerConfig {

    @Bean
    public String noDockerMessage() {
        System.out.println("🧪 Running tests with profile 'no-docker' (Docker disabled)");
        return "noDocker";
    }
}