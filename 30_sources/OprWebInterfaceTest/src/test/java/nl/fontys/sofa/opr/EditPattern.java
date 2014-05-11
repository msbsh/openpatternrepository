package nl.fontys.sofa.opr;

import java.awt.event.KeyEvent;

/**
 *
 * @author lydia
 */
public class EditPattern extends AppTestBase {

    public void testEditPatternPageAvailable() throws Exception {
        chooseAnyPattern();
        String patternName = selenium.getText("//div[@id='content']/div/h1");
        String patternUniqueName = getPatternUniqueName();
        editPattern();


        selenium.isVisible("//div[@id='content']/h1");
        assertEquals(patternName, selenium.getText("//div[@id='content']/h1"));
        verifyTrue(selenium.isTextPresent(patternName));

        verifyEquals(patternName, selenium.getValue("editPattern:name"));
        verifyEquals(patternUniqueName, selenium.getValue("editPattern:uniqueName"));
    }

    public void testValidName() throws Exception {
        chooseAnyPattern();
        editPattern();

        selenium.isEditable("editPattern:name");

        selenium.focus("editPattern:name");
        selenium.type("editPattern:name", "Layers");
        selenium.focus("editPattern:uniqueName");

        selenium.focus("editPattern:name");
        selenium.type("editPattern:name", "");
        selenium.focus("editPattern:uniqueName");
        selenium.selectWindow(null);
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(true, "Name: Value is required");

        selenium.focus("editPattern:name");
        selenium.type("editPattern:name", "Layers");
        selenium.focus("editPattern:uniqueName");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(false, "Name: Value is required");

        selenium.focus("editPattern:name");
        selenium.type("editPattern:name", "?=)(");
        selenium.focus("editPattern:uniqueName");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(true, "Name: Unaccepted Input Charset");

        selenium.focus("editPattern:name");
        selenium.type("editPattern:name", "Layers");
        selenium.focus("editPattern:uniqueName");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(false, "Name: Unaccepted Input Charset");

        selenium.focus("editPattern:name");
        selenium.type("editPattern:name", "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        selenium.focus("editPattern:uniqueName");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(true, "Name: Value has more than allowed 255 chars");

        selenium.focus("editPattern:name");
        selenium.type("editPattern:name", "Layers");
        selenium.focus("editPattern:uniqueName");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(false, "Name: Value has more than allowed 255 chars");
    }

    public void testValidUniqueName() throws Exception {
        selenium.setSpeed("3000");
        chooseAnyPattern();
        String patternUniqueName = getPatternUniqueName();
        editPattern();

        selenium.isEditable("editPattern:uniqueName");

        selenium.focus("editPattern:uniqueName");
        selenium.type("editPattern:uniqueName", "layers");
        selenium.focus("editPattern:name");

        selenium.focus("editPattern:uniqueName");
        selenium.type("editPattern:uniqueName", "");
        selenium.focus("editPattern:name");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(true, "Wiki name: Value is required");

        selenium.focus("editPattern:uniqueName");
        selenium.type("editPattern:uniqueName", patternUniqueName);
        selenium.focus("editPattern:name");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(false, "Wiki name: Value is required");

        selenium.focus("editPattern:uniqueName");
        selenium.type("editPattern:uniqueName", "?=)(");
        selenium.focus("editPattern:name");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(true, "Wiki name: Unaccepted Input Charset");

        selenium.focus("editPattern:uniqueName");
        selenium.type("editPattern:uniqueName", patternUniqueName);
        selenium.focus("editPattern:name");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(false, "Wiki name: Unaccepted Input Charset");

        selenium.focus("editPattern:uniqueName");
        selenium.type("editPattern:uniqueName", "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        selenium.focus("editPattern:name");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(true, "Wiki name: Value has more than allowed 255 chars");

        selenium.focus("editPattern:uniqueName");
        selenium.type("editPattern:uniqueName", patternUniqueName);
        selenium.focus("editPattern:name");
        selenium.keyPressNative(KeyEvent.VK_TAB + "");

        waitAndVerifyText(false, "Wiki name: Value has more than allowed 255 chars");

    }

    public void testAddRemoveTag() throws Exception {
        chooseAnyPattern();
        editPattern();

        selenium.type("//input[@class='iceSelInpTxtTxt']", "test");
        selenium.click("editPattern:addTag");

        waitAndVerifyText(true, "test-");

        if (selenium.isTextPresent("work+")) {
            selenium.click("//div[span='work']/a[text()='+']");

            waitAndVerifyText(true, "work-");
        }

        selenium.click("//div[span='work']/a[text()='-']");


        selenium.click("//div[span='test']/a[text()='-']");

        waitAndVerifyText(true, "work+");
        waitAndVerifyText(false, "test-");

    }

    public void testAddRemoveCategory() throws Exception {
        chooseAnyPattern();
        editPattern();

        selenium.select("//select[@class='iceSelOneLb']", "label=Security");
        waitAndVerifyText(true, "> Categories  > Technologie  > Security");
        selenium.click("//div[@id='editPattern:selectedCategories']/a[last()]/img");
        waitAndVerifyText(false, "> Categories  > Technologie  > Security");
    }

    public void testAddRemoveRelation() throws Exception {
        chooseAnyPattern();
        editPattern();

        addRelationship("editPattern");
    }

    public void testSubmitButtonEditable() throws Exception {
        chooseAnyPattern();
        editPattern();

        selenium.isVisible("editPattern:submit");
    }
}
