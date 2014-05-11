package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import nl.rug.search.opr.entities.AmbiguousEntity;
import nl.rug.search.opr.entities.template.Component;

/**
 *
 * @author cm
 */
@Entity
public class TextBlock extends AmbiguousEntity<TextBlock> implements Serializable {

    public final static long serialVersionUID = 1L;
    
    @Id
    @TableGenerator(name = "PK_TEXTBLOCK", allocationSize = 5, initialValue = 0)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_TEXTBLOCK")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Component component;
    
    @Lob
    private String text;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public TextBlock getThis() {
        return this;
    }
}
