/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.opr;

/**
 *
 * @author daniel & lydia
 */
//Todo adjust to new add pattern wizard
public class AddPatternWizzard extends AppTestBase {

    private static final String NEXT_BUTTON_ID = "addWizardPatternForm:next";
    private static final String PREVIOUS_BUTTON_ID = "addWizardPatternForm:previous";
    private static final String ADD_CONSEQUENCES_ID = "//input[@value='Add Consequence']";
    private static final int buttonSleep = 2000;

    public AddPatternWizzard(String testName) {
        super(testName);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    public void testConsequences() throws Exception {
        //Test add and remove
        addSomePattern();
        selenium.click("addWizardPatternForm:name");
        selenium.type("addWizardPatternForm:name", "Das ist ein Test Pattern");
        selenium.type("addWizardPatternForm:uniqueName", "TestPattern");
        selenium.select("addWizardPatternForm:license", "label=GNU GPL Version 3");
        selenium.select("addWizardPatternForm:template", "label=POSA");
        selenium.click(NEXT_BUTTON_ID);
        sleep(buttonSleep);
        selenium.click(NEXT_BUTTON_ID);
        sleep(buttonSleep);
        selenium.click(NEXT_BUTTON_ID);
        sleep(buttonSleep);
        selenium.click(ADD_CONSEQUENCES_ID);
        selenium.select("//select[@class='iceSelOneLb']", "label=Efficiency");
        selenium.type("//textarea[@class='iceInpTxtArea']", "Efficiency Consequence");
        selenium.click("//a[text()='--']");
        selenium.click("//a[text()='+']");
        selenium.click(ADD_CONSEQUENCES_ID);
        verifyTrue(selenium.isTextPresent("edit"));
        verifyTrue(selenium.isTextPresent("Efficiency"));
        verifyTrue(selenium.isTextPresent("Efficiency Consequence"));
        selenium.click("//a[text()='X']");
        verifyFalse(selenium.isTextPresent("edit"));
        verifyFalse(selenium.isTextPresent("Efficiency Consequence"));

        //Test Navigation
        selenium.select("//select[@class='iceSelOneLb']", "label=Accessibility");
        selenium.type("//textarea[@class='iceInpTxtArea']", "Add Accessibility");
        selenium.click("//a[text()='-']");
        selenium.click(ADD_CONSEQUENCES_ID);
        selenium.click(PREVIOUS_BUTTON_ID);
        sleep(buttonSleep);
        selenium.click(NEXT_BUTTON_ID);
        sleep(buttonSleep);
        verifyTrue(selenium.isTextPresent("Add Accessibility"));
        selenium.click("//a[text()='X']");
        selenium.click("//a[text()='X']");

        //Add multiple Consequences and test Edit function
        selenium.click(ADD_CONSEQUENCES_ID);
        selenium.type("//textarea[@class='iceInpTxtArea']", "Consequence 1");
        selenium.select("//select[@class='iceSelOneLb']", "label=Standardization");
        selenium.click("//a[text()='++']");
        selenium.click(ADD_CONSEQUENCES_ID);
        selenium.select("//select[@class='iceSelOneLb']", "label=Availability");
        selenium.type("//textarea[@class='iceInpTxtArea']", "Consequence 2");
        selenium.click("//a[text()='-']");
        selenium.click(ADD_CONSEQUENCES_ID);
        selenium.type("//textarea[@class='iceInpTxtArea']", "Consequence 3");
        selenium.click("//a[text()='--']");

        //Remove all consequences

        selenium.click("//a[text()='X']");
        selenium.click("//a[text()='X']");
        verifyTrue(selenium.isTextPresent("Consequence 3"));
        selenium.click("//a[text()='X']");
        verifyFalse(selenium.isTextPresent("Consequence 3"));
        verifyFalse(selenium.isTextPresent("Consequence 2"));
        verifyFalse(selenium.isTextPresent("Consequence 1"));

    }
}
