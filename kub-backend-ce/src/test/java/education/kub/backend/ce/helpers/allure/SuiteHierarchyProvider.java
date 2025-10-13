package education.kub.backend.ce.helpers.allure;

import io.qameta.allure.Allure;

public class SuiteHierarchyProvider {
    public static void SetAllureTestHierarhy() {
        Allure.getLifecycle().updateTestCase(tr -> tr.getLabels().removeIf(
                label -> "suite".equals(label.getName())));
        Allure.getLifecycle().updateTestCase(tr -> tr.getLabels().removeIf(
                label -> "parentSuite".equals(label.getName())));
        Allure.getLifecycle().updateTestCase(tr -> tr.getLabels().removeIf(
                label -> "subSuite".equals(label.getName())));
        Allure.label("parentSuite", "Unit Tests");
        Allure.suite("Auth API Controller Tests");
    }
}
