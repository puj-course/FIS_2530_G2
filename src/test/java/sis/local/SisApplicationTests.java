package sis.local;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = com.sis.Main.main.class) // le decimos al springboot que la clase de arranque
// no es esta, sino el "Main.main"

/*
Springboot por defecto busca SisApplication, 
como nuestros tests unitarios buscan esa clase, no se encuentra

*/
@ActiveProfiles(resolver = SisApplicationTests.ProfileResolver.class)
class SisApplicationTests {

    static class ProfileResolver implements org.springframework.test.context.ActiveProfilesResolver {
        @Override
        public String[] resolve(Class<?> testClass) {
            return TestProfileSelector.isDockerAvailable() ? new String[]{"default"} : new String[]{"no-docker"};
        }
    }

    @Test
    void contextLoads() {
        System.out.println("✅ Context loaded correctly");
    }
}
