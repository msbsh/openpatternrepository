package nl.fontys.sofa.opr;

import com.thoughtworks.selenium.SeleniumException;

/**
 *
 * @author lydia
 */
public class ViewASpecificPattern extends AppTestBase {

    public void testBrowse() throws Exception {
        String patternUniqueName = getExistPatternUniqueName();
        if (patternUniqueName != null) {
            callPattern(patternUniqueName);
        } else {
            fail("Only one Pattern available");
        }
    }

    public void testViewPatternSharedRepository() throws Exception {
        String patternUniqueName = getExistPatternUniqueName();
        if (patternUniqueName != null) {
            callPattern(patternUniqueName);
            Number number = selenium.getXpathCount("//div[@id='toc']//ul/div/*");
            assertTrue(number.intValue() > 0);
            assertEquals("Edit pattern", selenium.getText("link=Edit pattern"));
            assertEquals("Edit description", selenium.getText("link=Edit description"));
        } else {
            fail("Only one Pattern available");
        }
    }

    public void testViewUniquePatternID() throws Exception {
        try {
            selenium.open("/web-interface//wiki/l");
        } catch (SeleniumException ex) {
        }
        selenium.waitForPageToLoad("30000");

        verifyTrue(selenium.isTextPresent("Search"));
    }
}
