package nl.rug.search.opr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.pattern.TagLocal;
import nl.rug.search.opr.wrapper.TagWrapper;

@ManagedBean(name="tagCloudCtrl")
@ViewScoped
public class TagCloudController {

    public static final String TAG_OFTEN = "tagOften";
    public static final String TAG_SOMETIMES = "tagSometimes";
    public static final String TAG_VERY_OFTEN = "tagVeryOften";
    
    @EJB
    private TagLocal tagLocal;
    private ArrayList<Pattern> patternsForTag = new ArrayList<Pattern>();
    private ArrayList<TagWrapper> allTags;

    public void selectTag(ActionEvent event) {
        Tag t = (Tag) event.getComponent().getAttributes().get("TAG");
        patternsForTag.clear();
        patternsForTag.addAll(t.getTagPatterns());
    }

    public void selectTagById(long id) {
        Tag t = tagLocal.getById(id);
        patternsForTag.clear();
        patternsForTag.addAll(t.getTagPatterns());
    }

    public Collection<TagWrapper> getAllTags() {
        if (allTags == null) {
            ArrayList<TagWrapper> tags = new ArrayList<TagWrapper>();
            for (Tag tag : tagLocal.getUsedTags()) {
                TagWrapper tagWrapper = new TagWrapper(tag);
                tags.add(tagWrapper);
            }

            Collections.sort(tags);
            int size = tags.size();
            for (int i = 0; i < size; i++) {
                TagWrapper tagWrapper = tags.get(i);
                if (i < (size / 3)) {
                    tagWrapper.setStyleClass(TAG_SOMETIMES);
                } else if (i < (size * 2 / 3)) {
                    tagWrapper.setStyleClass(TAG_OFTEN);
                } else {
                    tagWrapper.setStyleClass(TAG_VERY_OFTEN);
                }
            }
            Collections.sort(tags, new TagWrapper.TagNameComparator());
            allTags = tags;
        } 
        return allTags;
    }

    /**
     * @return the patternsForTag
     */
    public ArrayList<Pattern> getPatternsForTag() {
        return patternsForTag;
    }

    public boolean isPatternsChosen() {
        return patternsForTag.size() > 0;
    }
}
































