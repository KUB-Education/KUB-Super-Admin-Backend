package education.kub.superadmin;

import education.kub.superadmin.config.AppSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppSecurityProperties.class})
public class KubSuperAdminBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubSuperAdminBackendApplication.class, args);
    }

}
