package nl.rug.search.opr.entities.pattern;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 */
public enum Indicator {

    verynegative, negative, neutral, positive, verypositive;

    public String value() {
        return name();
    }

    public static Indicator fromValue(String v) {
        return valueOf(v);
    }
}
