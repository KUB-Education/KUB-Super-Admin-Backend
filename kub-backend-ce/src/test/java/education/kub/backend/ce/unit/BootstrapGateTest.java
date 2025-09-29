package education.kub.backend.ce.unit;

import education.kub.backend.ce.domain.appstate.entity.AppStateEntity;
import education.kub.backend.ce.domain.appstate.repository.AppStateRepository;
import education.kub.backend.ce.domain.bootstrap.service.BootstrapGate;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.infrastructure.password.generator.PasswordGeneratorImpl;
import education.kub.backend.ce.infrastructure.password.hasher.impl.BCryptPasswordHasherImpl;
import education.kub.backend.ce.infrastructure.password.service.PasswordServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BootstrapGate.class,
        PasswordServiceImpl.class,
        PasswordGeneratorImpl.class,
        BCryptPasswordHasherImpl.class,
})
@ExtendWith(MockitoExtension.class)
public class BootstrapGateTest {

    @MockitoBean
    AppStateRepository appStateRepository;

    @MockitoBean
    UserRepository userRepository;

    @Autowired
    BootstrapGate bootstrapGate;

    Path file;

    @BeforeEach
    void setUp() throws IOException {
        file = Paths.get("bootstrap.token");
        Files.deleteIfExists(file);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(file);
    }

    @Test
    void onApplicationReady_shouldGenerateValidTokenFile_whenAppStateBootstrappedIsFalse_andUserCountIsZero() throws IOException {
        var appState = new AppStateEntity(1L, "bootstrapped", "false");
        when(appStateRepository.findByKey("bootstrapped")).thenReturn(Optional.of(appState));
        when(userRepository.count()).thenReturn(0L);

        bootstrapGate.onApplicationReady();

        assertTrue(Files.exists(file));
        String token = Files.readString(file);
        assertFalse(token.isBlank());

        assertTrue(bootstrapGate.checkAccess(token));
    }

}
