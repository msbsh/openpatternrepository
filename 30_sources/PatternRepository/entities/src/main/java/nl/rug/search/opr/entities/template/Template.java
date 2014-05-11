package nl.rug.search.opr.entities.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import nl.rug.search.opr.entities.BaseEntity;
import nl.rug.search.opr.entities.pattern.TextBlock;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
@Entity
public class Template extends BaseEntity<Template> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "PK_TEMPLATE", allocationSize = 5, initialValue = 0)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_TEMPLATE")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private String author;
    @OneToMany
    private List<Component> components = new ArrayList<Component>();
    @Transient
    private Set<Type> textTypes = EnumSet.of(Type.CONTEXT, Type.DESCRIPTION, Type.PROBLEM, Type.SOLUTION);
    @Transient
    private Set<Type> listTypes = EnumSet.of(Type.FORCES, Type.CONSEQUENCES);

    public static Template createTemplate(String name, String author, String description, List<Component> components) {
        Template t = new Template();
        t.setName(name);
        t.setAuthor(author);
        t.setDescription(description);
        t.setComponents(components);
        return t;
    }

    public TextBlock createTextBlock(int componentIndex, String text) {
        TextBlock t = new TextBlock();
        t.setComponent(components.get(componentIndex));
        t.setText(text);
        return t;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        Collections.sort(components, new ComponentComparator());
        return components;
    }

    public List<Component> getTextComponents() {
        List<Component> textComponents = new ArrayList<Component>();
        for (Component c : getComponents()) {
            if (textTypes.contains(c.getType())) {
                textComponents.add(c);
            }
        }
        Collections.sort(textComponents, new ComponentComparator());
        return textComponents;
    }

    public Component getForces() {

        for (Component c : getComponents()) {
            if (c.getType() == Type.FORCES) {
                return c;
            }
        }

        return null;
    }

    public Component getConsequenceList() {

        for (Component c : getComponents()) {
            if (c.getType() == Type.CONSEQUENCES) {
                return c;
            }
        }

        return null;
    }

    public boolean hasForces() {
        return getForces() != null;
    }

    public boolean hasConsequenceList() {
        return getConsequenceList() != null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected boolean dataEquals(Template other) {
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
    public Template getThis() {
        return this;
    }

    private class ComponentComparator implements Comparator<Component> {

        @Override
        public int compare(Component o1, Component o2) {
            return (int) Math.signum(o1.getOrder() - o2.getOrder());
        }
    }
}
