package nl.rug.search.opr;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 13.01.2010
 */
public class ContentWrapper {

    private static final String ANCHOR_SIGN = "#";

    private String headline;
    private String anchorHead;
    private String anchorTail;
    private String body;

    public ContentWrapper(String headline, String anchorHead, String anchorTail) {
        this.headline = headline;
        this.anchorHead = anchorHead;
        this.anchorTail = anchorTail;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getHeadline() {
        return headline;
    }

    public String getAnchor() {
        return anchorTail;
    }

    public String getFullAnchor() {
        return anchorHead + ANCHOR_SIGN + anchorTail;
    }
}


