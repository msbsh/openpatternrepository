package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import nl.rug.search.opr.entities.BaseEntity;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 06.10.2009
 */
@Entity
public class Category extends BaseEntity<Category> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator="PK_CATEGORY")
    @TableGenerator(name="PK_CATEGORY",allocationSize=5,initialValue=0)
    private Long id;

    @Column(nullable=false,unique=true)
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public static Category createCategory(String name, Category parent){
        Category c = new Category();
        c.setName(name);
        c.setParent(parent);
        return c;
    }

    @ManyToOne
    private Category parent;
    
    @OneToMany
    private Collection<Category> children = new ArrayList<Category>();;

    @ManyToMany(mappedBy = "categories")
    private Collection<Pattern> categoryPatterns = new ArrayList<Pattern>();

    /* Getter and Setter methods */
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Pattern> getCategoryPatterns() {

        return categoryPatterns;
    }

    public void setCategoryPatterns(Collection<Pattern> categoryPatterns) {
        this.categoryPatterns = categoryPatterns;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return !parent.equals(this);
    }

    public List<Category> getPath() {
        List<Category> path;

        if (hasParent()) {
           path = parent.getPath();
        } else {
            path = new LinkedList<Category>();
        }
        path.add(this);
        return path;
    }

    public Collection<Category> getChildren() {
        return children;
    }

    public void setChildren(Collection<Category> children) {
        this.children = children;
    }

    @Override
    protected boolean dataEquals(Category other) {
        return areEqual(name, name, true);
    }

    @Override
    protected Object[] getHashCodeData() {
        return new Object[]{name};
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public Category getThis() {
        return this;
    }
    
}
