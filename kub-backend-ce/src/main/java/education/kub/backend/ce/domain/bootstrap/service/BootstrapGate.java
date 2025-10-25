package education.kub.backend.ce.domain.bootstrap.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.app.properties.AppSecurityProperties;
import education.kub.backend.ce.domain.appstate.repository.AppStateRepository;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class BootstrapGate {
    private final AtomicReference<String> bootstrapTokenHashed = new AtomicReference<>(null);
    private final AtomicReference<Instant> bootstrapTokenHashedExpiresAt = new AtomicReference<>(Instant.now());

    private final AppStateRepository appStateRepository;
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    private final AppSecurityProperties appSecurityProperties;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (isBootstrappingAllowed()) {
//            FOR DEVELOPMENT AND STAGING ENVIRONMENTS, REMOVE BEFORE RELEASING PRODUCTION BUILD
            var token = appSecurityProperties.secretKey();
            var tokenHashed = passwordService.hash(token);

//            For production
//            var token = passwordService.generate();
//
//            var file = new File("bootstrap.token");
//            try (FileWriter writer = new FileWriter(file)) {
//                writer.write(token);
//                System.out.println("Bootstrap token was placed in file bootstrap.token. Remove file after use.");
//            } catch (IOException e) {
//                System.out.println("Bootstrap token wasn't placed in file bootstrap.token.");
//                System.out.println("Reason: " + e.getMessage());
//
//                return;
//            }

            bootstrapTokenHashed.set(tokenHashed);
            bootstrapTokenHashedExpiresAt.set(Instant.now().plus(1, ChronoUnit.DAYS));
        }
    }

    private boolean isBootstrappingAllowed() {
        var appState = appStateRepository.findByKey("bootstrapped")
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        var userCount = userRepository.count();

        return appState.getValue().equals("false") && userCount == 0;
    }

    public boolean checkAccess(String token) {
        if (isBootstrappingAllowed()) {
            if (bootstrapTokenHashedExpiresAt.get() != null && bootstrapTokenHashedExpiresAt.get().isBefore(Instant.now())) {
                return false;
            } else {
                return passwordService.matches(token, bootstrapTokenHashed.get());
            }
        } else {
            return false;
        }
    }

    public void closeAccess() {
        var appState = appStateRepository.findByKey("bootstrapped")
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        appState.setValue("true");
        appStateRepository.save(appState);

        bootstrapTokenHashed.set(null);
        bootstrapTokenHashedExpiresAt.set(null);
    }
}
