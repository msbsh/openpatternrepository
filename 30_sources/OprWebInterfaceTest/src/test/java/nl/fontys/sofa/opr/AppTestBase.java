/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.opr;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

/**
 *
 * @author Lydia
 */
public class AppTestBase extends SeleneseTestCase {

    protected Number patternCount = 0;

    @Override
    public void setUp() throws Exception {
        selenium = new DefaultSelenium(System.getProperty("selenium.server.url"), Integer.parseInt(System.getProperty("selenium.server.port")),
                System.getProperty("selenium.application.browser"), System.getProperty("selenium.application.base"));
        selenium.start();
        selenium.setSpeed("2000");
    }

    @Override
    public void tearDown() throws Exception {
        try {
            checkForVerificationErrors();
        } finally {
            selenium.stop();
        }
    }

    public AppTestBase() {
    }

    public AppTestBase(String testName) {
        super(testName);
    }

    private void open() {
        selenium.open(System.getProperty("selenium.application.path") + "welcome.iface");
    }

    private void browse() {
        open();
        selenium.click("//img[@alt='Image browse']");
        selenium.waitForPageToLoad("30000");
        patternCount = selenium.getXpathCount("//a[starts-with(@href,'wiki')]");
    }

    private void failTest() {
        fail("No Patterns available");
    }

    protected void waitAndVerifyText(boolean should, String text) throws Exception {
        boolean correct = false;

        for (int second = 0; second < 60; second++) {
            try {
                correct = selenium.isTextPresent(text);

                if (correct && should) {
                    verifyTrue(true);
                    return;
                }

                if (!correct && !should) {
                    verifyTrue(true);
                    return;
                }


            } catch (Exception e) {
            }
        }

        fail("Timeout at verification: '" + text + "' should be '" + should + "'");
        return;
    }

    protected Number getPatternCount() {
        return patternCount;
    }

    protected void callPattern(String patternUniqueName) {
        browse();
        if (patternCount.intValue() > 0) {
            selenium.click("//a[@href='wiki/" + patternUniqueName + "']");
            selenium.waitForPageToLoad("30000");
        } else {
            failTest();
        }
    }

    protected void addSomePattern() {
        open();
        selenium.click("//img[@alt='Image Add Pattern']");
        selenium.waitForPageToLoad("30000");
    }

    protected void editPattern() {
        selenium.click("link=Edit pattern");
        selenium.waitForPageToLoad("30000");
    }

    protected void editDescription() {
        selenium.click("link=Edit description");
        selenium.waitForPageToLoad("30000");
    }

    protected void search() {
        open();
        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
    }

    protected void chooseAnyPattern() {
        browse();
        if (patternCount.intValue() > 0) {
            selenium.click("//a[starts-with(@href,'wiki')]");
            selenium.waitForPageToLoad("30000");
        } else {
            failTest();
        }
    }

    protected String getExistPatternUniqueName() {
        browse();
        if (patternCount.intValue() > 1) {
            selenium.click("xpath=(//div/a[starts-with(@href,'wiki')])[last()-" + (patternCount.intValue() - 2) + "]");
            return getPatternUniqueName();
        } else {
            return null;
        }
    }

    protected String getPatternUniqueName() {
        String[] location = selenium.getLocation().split("/");
        return location[location.length - 1];
    }

    protected void addRelationship(String action) throws Exception {
        selenium.type(action + ":selectPatternsearch", "*");
        selenium.click("//input[@value='Search']");
        if (patternCount.intValue() > 1) {
            String thisPattern = selenium.getText("//div[@id='content']/h1");
            String patternChoose = selenium.getText("//div[@class='icePnlGrp inlinesearch']/ul/li/a");
            String patternChooseLink = "//div[@class='icePnlGrp inlinesearch']/ul/li/a";
            if (thisPattern.equals(patternChoose)) {
                Number inlineSearchCount = selenium.getXpathCount("//div[@class='icePnlGrp inlinesearch']/ul/li");
                if(inlineSearchCount.intValue() > 1){
                    patternChoose = selenium.getText("//div[@class='icePnlGrp inlinesearch']/ul/li[last()]/a");
                    patternChooseLink = "//div[@class='icePnlGrp inlinesearch']/ul/li[last()]/a";
                }
            }

            selenium.click(patternChooseLink);
            selenium.type("//tr[td='Description']/td/input", "some Relation");
            selenium.select("//tr[td='Type']/td/select", "label=Variant");
            selenium.click(action + ":addRelationship");


            waitAndVerifyText(true, "" + thisPattern + " < Variant > " + patternChoose + "");
            waitAndVerifyText(true, "some Relation");
        } else {
            waitAndVerifyText(true, "Your search did not match any patterns");
        }
    }
}
