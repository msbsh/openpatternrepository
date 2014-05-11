package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.utils.TagDictionary;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import nl.rug.search.opr.ConfigConstants;
import nl.rug.search.opr.dao.GenericDaoBean;
import nl.rug.search.utils.TextProcessor;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 * @author Martin Verspai <martin@verspai.de>
 * @version 2.0
 * @date 19.11.2010
 */
@Stateless
public class TagBean extends GenericDaoBean<Tag, Long> implements TagLocal {

    private static final Logger logger = Logger.getLogger(TagBean.class.getName());

    private static final int tagMultiplicator = 5;
    private static final int minLength = 1;

    @Override
    public Collection<Tag> getProposedTags(PatternVersion pattern) {
        return getProposedTags(pattern, -1);
    }

    private Map<String, Integer> generateCountOfBlocks(Collection<TextBlock> blocks) {
        Map<String, Integer> count = new HashMap<String, Integer>();

        TagDictionary blacklist = TagDictionary.getInstance();

        for (TextBlock block : blocks) {
            String text = TextProcessor.stripHTML(block.getText());
            text = TextProcessor.stripSpecials(text);
            text = text.toLowerCase();

            count.putAll(generateCountForString(new StringTokenizer(text), blacklist));
        }
        return count;
    }

    private Map<String, Integer> generateCountForString(StringTokenizer st, TagDictionary blacklist) {
        Map<String, Integer> count = new HashMap<String, Integer>();

        while (st.hasMoreTokens()) {
            String word = st.nextToken();

            if (blacklist.isBlacklisted(word)) {
                continue;
            }

            if (word.length() <= minLength) {
                continue;
            }

            if (count.containsKey(word)) {
                count.put(word, count.get(word).intValue() + 1);
            } else {
                count.put(word, 1);
            }
        }
        return count;
    }

    private Collection<Tag> generateProposedTagsOfList(List<Entry<String, Integer>> list, int amount) {
        Collection<Tag> proposedTags = new ArrayList<Tag>();
        int i = 1;
        for (Entry<String, Integer> word : list) {
            proposedTags.add(new Tag(word.getKey()));

            if (amount >= 0) {
                if (i >= amount) {
                    break;
                }
                i++;
            }
        }
        return proposedTags;
    }

    @Override
    public Collection<Tag> getProposedTags(PatternVersion pattern, int amount) {
        Map<String, Integer> count = generateCountOfBlocks(pattern.getBlocks());

        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(count.entrySet());

        for (Entry<String, Integer> word : list) {
            Tag t = null;
            if ((t = getByName(word.getKey())) != null) {
                word.setValue(word.getValue() + t.getTagPatterns().size() * tagMultiplicator);
            }
        }

        Collections.sort(list, COMPARATOR_DESC);
        Collection<Tag> proposedTags = generateProposedTagsOfList(list, amount);

        return proposedTags;
    }

    @Override
    public Tag getByName(String tag) {
        Tag result = null;
        tag = tag.toLowerCase();
        try {
            Tag tmp = null;

            Query q = createQuery(ConfigConstants.QUERY_GET_TAG_BY_NAME);
                q.setParameter("param", tag);

            tmp = (Tag) q.getSingleResult();

            result = getManager().getReference(Tag.class, tmp.getId());
        } catch (NoResultException e) {
            // ignore the exception
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public Collection<Tag> getSimiliarTags(String search) {
        Collection<Tag> tags = new ArrayList<Tag>();

        try {
            Query q = createQuery(ConfigConstants.QUERY_GET_SIMILAR_TAGS);
            q.setParameter("param", "%" + search.toLowerCase() + "%");

            tags = q.getResultList();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return tags;
    }

    @Override
    public Collection<Tag> getUsedTags() {
        Collection<Tag> tags = new ArrayList<Tag>();
        try {
            Query q = createQuery(ConfigConstants.QUERY_GET_USED_TAGS);
            tags = q.getResultList();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return tags;
    }

    @Override
    public Collection<Tag> getFavouriteTags(int amount) {
        List<Tag> tags = createQuery(ConfigConstants.QUERY_GET_NON_ZERO_TAGS).getResultList();

        return orderTags(tags, amount);
    }

    private Collection<Tag> orderTags(Collection<Tag> tags, int amount) {
        int filled = 0;
        Collection<Tag> orderedTags = new ArrayList<Tag>();

        while(!tags.isEmpty()) {
            Tag highest = null;
            for(Tag t : tags) {
                if(highest == null || t.getTagPatterns().size() > highest.getTagPatterns().size()) {
                    highest = t;
                }
            }
            orderedTags.add(highest);
            tags.remove(highest);
            ++filled;
            if(filled == amount)
                break;
        }

        return orderedTags;
    }

    @Override
    public Collection<Tag> persistByName(Collection<Tag> tags) {
        Collection<Tag> cleanedTags = removeDuplicateNames(tags);
        Collection<Tag> persistedTags = new LinkedList<Tag>();
        Tag persistedTag = null;

        for (Tag tag : cleanedTags){
            persistedTag = getByName(tag.getName());
            if (persistedTag == null){
                persistedTag = makePersistent(tag);
            }
            persistedTags.add(persistedTag);
        }
        return persistedTags;
    }

    @Override
    public Collection<Tag> removeDuplicateNames(Collection<Tag> tags) {
        Collection c = new LinkedList<Tag>();
        for (Tag t : tags){
            if (!c.contains(t)){
                c.add(t);
            }
        }
        return c;
    }
    
    private static final Comparator<Entry<String, Integer>> COMPARATOR_DESC = new Comparator<Entry<String, Integer>>() {

        @Override
        public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
            return (o2.getValue() - o1.getValue());
        }
    };

}
