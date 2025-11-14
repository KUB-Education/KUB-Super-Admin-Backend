package education.kub.backend.ce.infrastructure.components.auth;

import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ExecutorProperties;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.LoginProvider;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginComponent {
    public LoginProperties loginData;
    public ExecutorProperties executor = new ExecutorProperties();

    @Step("First login")
    public void FirstLogin() {
        LoginProvider.FirstLogin(executor, loginData);
    }
}
