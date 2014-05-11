package nl.rug.search.opr.entities.pattern.relation;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import nl.rug.search.opr.entities.pattern.Driver;
import nl.rug.search.opr.entities.pattern.Pattern;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 06.10.2009
 */
@Entity
public class Relationship extends Driver implements Serializable {

    private static final long serialVersionUID = 1L;
    private String description;
    @OneToOne(cascade = CascadeType.REMOVE)
    private RelationshipType type;
    @ManyToOne
    private Pattern patternA;
    @ManyToOne
    private Pattern patternB;

    public static Relationship createRelationship(String description, Pattern patternA, Pattern patternB, RelationshipType type) {
        Relationship r = new Relationship();
        r.setDescription(description);
        r.setPatternA(patternA);
        r.setPatternB(patternB);
        r.setType(type);
        return r;
    }

    /* Methods */
    public Pattern getRelatedPattern(Pattern current) {
        if (this.patternA.equals(current)) {
            return this.patternB;
        } else {
            return this.patternA;
        }
    }

    /* Getter and Setter methods */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Pattern getPatternA() {
        return patternA;
    }

    public void setPatternA(Pattern patternA) {
        this.patternA = patternA;
    }

    public Pattern getPatternB() {
        return patternB;
    }

    public void setPatternB(Pattern patternB) {
        this.patternB = patternB;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public boolean hasSamePatternCombinationAs(Relationship r) {
        if (getPatternA().equals(r.getPatternA()) &&
                getPatternB().equals(r.getPatternB()) &&
                r.getType().equals(getType())) {
            return true;
        }
        if (getPatternA().equals(r.getPatternB()) &&
                getPatternB().equals(r.getPatternA()) &&
                r.getType().equals(getType())) {
            return true;
        }
        return false;
    }

    @Override
    public Driver getThis() {
        return this;
    }

    /**
     * @param p pattern to comaper with
     * @return Returns true if p is part of this relation
     */
    public boolean belongsToPattern(Pattern p) {
        if (getPatternA().equals(p)){
            return true;
        }
        if (getPatternB().equals(p)){
            return true;
        }
        return false;
    }
}
