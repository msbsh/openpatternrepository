package nl.fontys.sofa.opr;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de> & lydia
 */
public class SearchUI extends AppTestBase {

    private void performSearchWithQuery(String query) throws Exception {
        selenium.setSpeed("500");
        search();
        selenium.type("search:searchQuery", query);
        selenium.click("search:searchButton");
        for (int second = 0;; second++) {
            if (second >= 20) {
                fail("timeout");
            }
            try {
                Number count = selenium.getXpathCount("//div[@id='search:searchresults']");

                if (count != null && count.intValue() > 0) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    public void testSearchNormal() throws Exception {
        performSearchWithQuery("*");
        verifyTrue(selenium.getXpathCount("//div[@id='search:searchresults']/div[@class='icePnlGrp item']").intValue() >= 0);
    }
}
