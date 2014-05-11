package nl.rug.search.opr.search.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.relation.Alternative;
import nl.rug.search.opr.entities.pattern.relation.Combination;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.pattern.relation.Variant;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
class SolrDocumentConverter {

    static SolrInputDocument convert(Pattern pattern) {
        SolrInputDocument document = new SolrInputDocument();
        PatternVersion version = convertGeneralInformation(document, pattern);

        convertRelationships(pattern, document);

        convertText(version, document);

        convertDriver(version, document);

        convertImpactIndicator(version, document);

        return document;
    }

    private static PatternVersion convertGeneralInformation(SolrInputDocument document, Pattern pattern) {
        document.addField("id", pattern.getId());
        document.addField("name", pattern.getName());
        document.addField("uniquename", pattern.getUniqueName());
        List<String> tags = new ArrayList<String>(pattern.getTags().size());
        for (Tag tag : pattern.getTags()) {
            tags.add(tag.getName());
        }
        document.addField("tag", tags);
        List<Long> categories = new ArrayList<Long>(pattern.getCategories().size());
        for (Category category : pattern.getCategories()) {
            categories.add(category.getId());
        }
        document.addField("category", categories);
        PatternVersion version = pattern.getCurrentVersion();
        document.addField("version", version.getId());
        document.addField("license", version.getLicense().getName());
        document.addField("documented", version.getDocumentedWhen());
        document.addField("source", version.getSource());
        return version;
    }

    private static void convertRelationships(Pattern pattern, SolrInputDocument document) {
        Map<Long, String> combinations = convertCombinations(pattern);
        document.addField("combination", combinations.values());
        document.addField("combinationid", combinations.keySet());
        Map<Long, String> alternatives = convertAlternatives(pattern);
        document.addField("alternative", alternatives.values());
        document.addField("alternativeid", alternatives.keySet());
        Map<Long, String> variants = convertVariants(pattern);
        document.addField("variant", variants.values());
        document.addField("variantid", variants.keySet());
    }

    private static void convertText(PatternVersion version, SolrInputDocument document) {
        List<String> contexts = convertTextblocks(version, version.getContext());
        document.addField("context", contexts);
        List<String> problems = convertTextblocks(version, version.getProblem());
        document.addField("problem", problems);
        List<String> solutions = convertTextblocks(version, version.getSolution());
        document.addField("solution", solutions);
        List<String> descriptions = convertTextblocks(version, version.getDescription());
        document.addField("description", descriptions);
    }

    private static void convertDriver(PatternVersion version, SolrInputDocument document) {
        List<String> consequences = new ArrayList<String>();
        for (Consequence c : version.getConsequences()) {
            consequences.add(c.getDescription());
        }
        document.addField("consequence", consequences);
        List<String> forces = new ArrayList<String>();
        for (Force f : version.getForces()) {
            forces.add(f.getDescription());
        }
        document.addField("force", forces);
    }

    private static void convertImpactIndicator(PatternVersion version, SolrInputDocument document) {
        Map<Long, String> veryPositive = convertQualityAttributesForIndicator(version, Indicator.verypositive);
        document.addField("verypositive", veryPositive.values());
        document.addField("verypositiveid", veryPositive.keySet());
        Map<Long, String> positive = convertQualityAttributesForIndicator(version, Indicator.positive);
        document.addField("positive", positive.values());
        document.addField("positiveid", positive.keySet());
        Map<Long, String> neutral = convertQualityAttributesForIndicator(version, Indicator.neutral);
        document.addField("neutral", neutral.values());
        document.addField("neutralid", neutral.keySet());
        Map<Long, String> negative = convertQualityAttributesForIndicator(version, Indicator.negative);
        document.addField("negative", negative.values());
        document.addField("negativeid", negative.keySet());
        Map<Long, String> veryNegative = convertQualityAttributesForIndicator(version, Indicator.verynegative);
        document.addField("verynegative", veryNegative.values());
        document.addField("verynegativeid", veryNegative.keySet());
    }

    private static Map<Long, String> convertCombinations(Pattern pattern) {
        Map<Long, String> relationships = new HashMap<Long, String>();
        for (Relationship relation : pattern.getRelations()) {
            if (relation.getType() instanceof Combination && relation.getPatternB() != null) {
                relationships.put(relation.getPatternB().getId(), relation.getPatternB().getName());
            }
        }
        return relationships;
    }

    private static Map<Long, String> convertAlternatives(Pattern pattern) {
        Map<Long, String> relationships = new HashMap<Long, String>();
        for (Relationship relation : pattern.getRelations()) {
            if (relation.getType() instanceof Alternative && relation.getPatternB() != null) {
                relationships.put(relation.getPatternB().getId(), relation.getPatternB().getName());
            }
        }
        return relationships;
    }

    private static Map<Long, String> convertVariants(Pattern pattern) {
        Map<Long, String> relationships = new HashMap<Long, String>();
        for (Relationship relation : pattern.getRelations()) {
            if (relation.getType() instanceof Variant && relation.getPatternB() != null) {
                relationships.put(relation.getPatternB().getId(), relation.getPatternB().getName());
            }
        }
        return relationships;
    }

    private static List<String> convertTextblocks(PatternVersion version, List<TextBlock> blocks) {
        List<String> result = new ArrayList<String>(blocks.size());
        for (TextBlock block : blocks) {
            result.add(block.getText());
        }
        return result;
    }

    private static Map<Long, String> convertQualityAttributesForIndicator(PatternVersion version, Indicator indicator) {
        Map<Long, String> resultMap = new HashMap<Long, String>();
        for (Force force : version.getForces()) {
            if (force.getImpactIndication() == indicator && force.getQualityAttribute() != null) {
                resultMap.put(force.getQualityAttribute().getId(), force.getQualityAttribute().getName());
            }
        }
        for (Consequence consequence : version.getConsequences()) {
            if (consequence.getImpactIndication() == indicator && consequence.getQualityAttribute() != null) {
                resultMap.put(consequence.getQualityAttribute().getId(), consequence.getQualityAttribute().getName());
            }
        }
        return resultMap;
    }
}
