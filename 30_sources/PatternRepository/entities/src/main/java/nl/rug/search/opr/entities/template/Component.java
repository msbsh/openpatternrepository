package nl.rug.search.opr.entities.template;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.opr.entities.AmbiguousEntity;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
@Entity
public class Component extends AmbiguousEntity<Component> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean mandatory = false;
    private String name = "";
    private String description = "";
    private String identifier = "";
    private int sortOrder = 0;
    @Enumerated(value = EnumType.STRING)
    private Type type;

    public Component() {
    }

    public static Component createComponent(String identifier, String name, String description, boolean mandatory, Type type, int order) {
        Component c = new Component();
        c.setIdentifier(identifier);
        c.setName(name);
        c.setDescription(description);
        c.setMandatory(mandatory);
        c.setType(type);
        c.setOrder(order);
        return c;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public int getOrder() {
        return sortOrder;
    }

    public void setOrder(int order) {
        this.sortOrder = order;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public Component getThis() {
        return this;
    }
}
