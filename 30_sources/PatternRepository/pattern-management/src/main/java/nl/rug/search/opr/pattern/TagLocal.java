package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.Tag;
import java.util.Collection;
import javax.ejb.Local;
import nl.rug.search.opr.dao.GenericDaoLocal;

/**
 *
 * @author cm
 */
@Local
public interface TagLocal extends GenericDaoLocal<Tag,Long> {

    public Collection<Tag> getProposedTags(PatternVersion pattern);

    public Collection<Tag> getProposedTags(PatternVersion pattern, int amount);

    public Tag getByName(String tag);

    public Collection<Tag> getSimiliarTags(String search);

    public Collection<Tag> getFavouriteTags(int amount);

    public java.util.Collection<nl.rug.search.opr.entities.pattern.Tag> getUsedTags();

    public Collection<Tag> persistByName(Collection<Tag> tags);

    public Collection<Tag> removeDuplicateNames(Collection<Tag> tags);
}






























