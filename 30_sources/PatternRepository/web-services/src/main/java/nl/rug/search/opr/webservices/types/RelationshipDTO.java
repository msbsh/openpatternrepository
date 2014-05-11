package nl.rug.search.opr.webservices.types;

import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.relation.Alternative;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;
import nl.rug.search.opr.entities.pattern.relation.Variant;

/**
 *
 * @author cm
 */
public class RelationshipDTO {

    private Long id;
    private String name;
    private RelationType type;
    private String description;
    
    public RelationshipDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    public static RelationshipDTO assemble(Pattern p, Relationship r) {
        if (r==null) return new RelationshipDTO();

        //Decide which pattern to use
        Pattern toUse = r.getPatternA();

        if(p.dataEquals(r.getPatternA())) {
            toUse = r.getPatternB();
        }

        RelationshipDTO rdto = new RelationshipDTO();

        rdto.setId(toUse.getId());
        rdto.setName(toUse.getName());
        rdto.setType(toRelationType(r.getType()));
        rdto.setDescription(r.getDescription());

        return rdto;
    }

    private static RelationType toRelationType(RelationshipType rt) {
        if (rt instanceof Variant) {
            return RelationType.VARIANT;
        } else if (rt instanceof Alternative) {
            return RelationType.ALTERNATIVE;
        } else {
            return RelationType.COMBINATION;
        }
    }


}
