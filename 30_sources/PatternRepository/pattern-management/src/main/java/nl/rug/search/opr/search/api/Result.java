
package nl.rug.search.opr.search.api;

import java.util.Collection;

/**
 *
 * @author cm
 */
public class Result {

    private long id;
    private String name;
    private String uniquename;
    private float score;
    private Collection<String> highlighted;

    public Collection<String> getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(Collection<String> highlighted) {
        this.highlighted = highlighted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getUniquename() {
        return uniquename;
    }

    public void setUniquename(String uniquename) {
        this.uniquename = uniquename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
