package nl.fontys.sofa.opr;

import java.util.Date;

/**
 *
 * @author anonym & lydia
 */
public class AddPatternBasicInformation extends AppTestBase {

    public static final String INVALID_PATTERN_NAME = "!!!!INVALID!!!";
    public static final String INVALID_PATTERN_WIKINAME = "invalid name";
    private String aTestPatterName = String.valueOf(new Date().getTime());

    public void testEnterPattern() throws Exception {
        addSomePattern();
        selenium.type("addWizardPatternForm:name", aTestPatterName);
        selenium.type("addWizardPatternForm:uniqueName", aTestPatterName);
        selenium.type("addWizardPatternForm:source", "Christian Manteuffel");
        selenium.select("addWizardPatternForm:license", "label=Creative Commons");
        selenium.select("addWizardPatternForm:template", "label=POSA");
        selenium.select("addWizardPatternForm:categorySelector", "label=Categories");
        selenium.select("addWizardPatternForm:categorySelector", "label=Pattern");
        submitForm();
        selenium.isTextPresent("Pattern has been added");
    }

    public void testDuplicateWikiname() throws Exception {
        String existPatternUniqueName = getExistPatternUniqueName();
        if (existPatternUniqueName != null) {
            addSomePattern();
            selenium.type("addWizardPatternForm:uniqueName", existPatternUniqueName);
            selenium.windowFocus();
            selenium.click("addWizardPatternForm:uniqueName");
            submitForm();
            waitForTextPreset("Wiki name: The name is already in use.");
        }
    }

    public void testInvalidName() throws Exception {
        addSomePattern();
        selenium.type("addWizardPatternForm:name", INVALID_PATTERN_NAME);
        selenium.windowFocus();
        selenium.click("addWizardPatternForm:name");
        submitForm();
        waitForTextPreset("Name: Unaccepted Input Charset");
    }

    public void testInvalidWikiName() throws Exception {
        addSomePattern();
        selenium.type("addWizardPatternForm:uniqueName", INVALID_PATTERN_WIKINAME);
        selenium.windowFocus();
        selenium.click("addWizardPatternForm:uniqueName");
        submitForm();
        waitForTextPreset("Wiki name: Unaccepted Input Charset");
    }

    public void testMissingValues() throws Exception {
        addSomePattern();
        submitForm();
        waitForTextPreset("Template: Value is required");
        waitForTextPreset("License: Value is required");
        waitForTextPreset("Name: Value is required");
        waitForTextPreset("Wiki name: Value is required");
    }

    private void submitForm() {
        selenium.click("addWizardPatternForm:submit");
    }

    @SuppressWarnings("SleepWhileHoldingLock")
    private void waitForTextPreset(String text) throws InterruptedException {
        for (int second = 0;; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isTextPresent(text)) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
