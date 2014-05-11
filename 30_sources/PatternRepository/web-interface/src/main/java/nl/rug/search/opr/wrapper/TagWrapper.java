/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.opr.wrapper;

import java.util.Comparator;
import nl.rug.search.opr.entities.pattern.Tag;

/**
 *
 * @author heesch
 */
public class TagWrapper implements Comparable<TagWrapper>{

    private Tag tag;
    private String styleClass;

    public TagWrapper(Tag tag) {
        this.tag = tag;
    }

    /**
     * @return the tag
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * @return the styleClass
     */
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * @param styleClass the styleClass to set
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public int compareTo(TagWrapper t) {
        return this.tag.getTagPatterns().size() - t.getTag().getTagPatterns().size();
    }

    public static final class TagNameComparator implements Comparator<TagWrapper> {
        @Override
        public int compare(TagWrapper o1, TagWrapper o2) {
            return o1.tag.getName().compareTo(o2.tag.getName());
        }

    }

}
