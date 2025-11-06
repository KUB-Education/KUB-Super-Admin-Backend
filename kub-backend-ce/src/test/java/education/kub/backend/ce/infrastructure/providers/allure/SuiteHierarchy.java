package education.kub.backend.ce.infrastructure.providers.allure;

import io.qameta.allure.Allure;

public class SuiteHierarchy {
    public static void SetAllureTestHierarchy() {
        Allure.getLifecycle().updateTestCase(tr -> tr.getLabels().removeIf(
                label -> "suite".equals(label.getName())));
        Allure.getLifecycle().updateTestCase(tr -> tr.getLabels().removeIf(
                label -> "parentSuite".equals(label.getName())));
        Allure.getLifecycle().updateTestCase(tr -> tr.getLabels().removeIf(
                label -> "subSuite".equals(label.getName())));
        Allure.label("parentSuite", "Controller tests");
    }
}
