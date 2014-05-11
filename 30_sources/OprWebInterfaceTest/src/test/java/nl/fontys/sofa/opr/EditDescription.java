package nl.fontys.sofa.opr;

/**
 *
 * @author lydia
 */
public class EditDescription extends AppTestBase {

    public void testEditDescription() throws Exception {
        chooseAnyPattern();
        editDescription();

        verifyTrue(selenium.isTextPresent("Version ID"));

        assertTrue(selenium.isElementPresent("editDescription:versionId"));
        selenium.type("editDescription:author", "No Author availeble");


        selenium.isVisible("editDescription:addForce");
        Number number = selenium.getXpathCount("//div[@id='editDescription:forces']/*");
        if (number.intValue() > 0) {
            selenium.click("//div[@id='editDescription:forces']/div[last()]/a");

            selenium.select("//div[@id='editDescription:forces']//select[@class='iceSelOneLb']", "label=Dependability");
            selenium.type("//textarea[@class='iceInpTxtArea']", "very nice dependability");
            selenium.click("//a[text()='++']");
            selenium.click("//a/span[text()='view']");
            verifyTrue(selenium.isTextPresent("very nice dependability"));
        }

        Number numberAfterEdit = selenium.getXpathCount("//div[@id='editDescription:forces']/*");
        assertEquals(number, numberAfterEdit);


        selenium.setSpeed("3000");
        selenium.click("editDescription:submitEditVersion");
        verifyTrue(selenium.isTextPresent("Pattern has been edited!"));
    }

    public void testDelete() throws Exception {
        chooseAnyPattern();
        editDescription();

        Number number = selenium.getXpathCount("//div[@id='editDescription:consequences']/*");

        selenium.click("editDescription:addConsequence");
        selenium.select("//div[@id='editDescription:consequences']//select[@class='iceSelOneLb']", "label=Capability");
        selenium.type("//textarea[@class='iceInpTxtArea']", "nice capability");
        selenium.click("//a[text()='+']");

        selenium.click("//a/span[text()='view']");

        Number numberAfterAdd = selenium.getXpathCount("//div[@id='editDescription:consequences']/*");
        assertEquals(number.intValue() + 1, numberAfterAdd);

        selenium.click("editDescription:submitEditVersion");
        verifyTrue(selenium.isTextPresent("Pattern has been edited!"));
    }
}
