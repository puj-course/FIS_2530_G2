/*
package sis.local;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SisApplicationTests {

	@Test
	void contextLoads() {
	}

}

ORIGINAL VERSION, REPLACED BC BOTH WINDOWS RUNNER AND MACOS RUNNER DO NOT HAVE DOCKER AS DEFAULT
*/

package sis.local;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
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