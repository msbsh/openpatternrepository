package nl.rug.search.opr.controller.wizard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.template.Component;
import nl.rug.search.opr.entities.template.Template;
import static nl.rug.search.utils.TextProcessor.html2wiki;
import static nl.rug.search.utils.TextProcessor.stripHTML;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
@ManagedBean(name = "matchingStep")
@ViewScoped
public class TemplateMatching implements WizardStep {

    private String selection = "";
    private Template template = null;
    private Map<String, TextBlock> blocks = new HashMap<String, TextBlock>();
    private static final int UNDO_SIZE = 10;
    private final Deque<UndoEntry<String, String>> undo = new ArrayDeque<UndoEntry<String, String>>(10);
    private static final String CONSEQUENCES = "CONSEQUENCES";
    private static final String FORCES = "FORCES";
    public static final String CLOSE_TEXT = "";
    public static final String OPEN_TEXT = "";
    private List<Consequence> consequences;
    private List<Force> forces;
    private boolean editPastBox = true;

    @ManagedProperty(value = "#{addWizardController}")
    private AddWizardController wizard;

    @PostConstruct
    private void load() {
        PatternVersion v = wizard.getPatternVersion();
        template = v.getTemplate();

        for (Component c : template.getTextComponents()) {
            if (!blocks.containsKey(c.getIdentifier())) {
                TextBlock block = new TextBlock();
                block.setComponent(c);
                block.setText("");
                blocks.put(c.getIdentifier(), block);
            }
        }

        for (TextBlock tb : v.getBlocks()) {
            blocks.put(tb.getComponent().getIdentifier(), tb);
        }

        consequences = v.getConsequences();
        forces = v.getForces();

        if (consequences == null) {
            consequences = new ArrayList<Consequence>();
        }

        if (forces == null) {
            forces = new ArrayList<Force>();
        }

        selection = "";
    }

    @PreDestroy
    private void unload() {
        PatternVersion v = wizard.getPatternVersion();
        v.setBlocks(blocks.values());
        v.setConsequences(consequences);
        v.setForces(forces);
    }

    public void moveTo(ActionEvent e) {
        String c = e.getComponent().getAttributes().get("identifier").toString();
        if (blocks.containsKey(c)) {
            TextBlock block = blocks.get(c);

            if (undo.size() >= UNDO_SIZE) {
                undo.removeLast();
            }
            undo.addFirst(new UndoEntry<String, String>(c, block.getText()));

            selection = html2wiki(selection);
            block.setText(block.getText() + OPEN_TEXT + selection + CLOSE_TEXT);
            blocks.put(c, block);
        }
    }

    public Template getTemplate() {
        return template;
    }

    public AddWizardController getWizard() {
        return wizard;
    }

    public void setWizard(AddWizardController wizard) {
        this.wizard = wizard;
    }


    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection.trim();       
    }

    public void addForce(ActionEvent e) {
        if (!selection.equals("")) {
            Force f = new Force();
            f.setDescription(stripHTML(selection));
            forces.add(f);
            int id = forces.size()-1;
            if (undo.size() >= UNDO_SIZE) {
                undo.removeLast();
            }
            undo.addFirst(new UndoEntry<String, String>(FORCES, id+""));
        }
    }

    public void addConsequence(ActionEvent e) {
        if (!selection.equals("")) {
            Consequence c = new Consequence();
            c.setDescription(stripHTML(selection));
            consequences.add(c);
            int id = consequences.size()-1;
            if (undo.size() >= UNDO_SIZE) {
                undo.removeLast();
            }
            undo.addFirst(new UndoEntry<String, String>(CONSEQUENCES, id+""));
        }
    }

    public void undo(ActionEvent e) {
        if (!undo.isEmpty()) {

            UndoEntry<String, String> entry = undo.removeFirst();
            String c = entry.getKey();
            if(c.equals(FORCES)) {
                int id = Integer.parseInt(entry.getValue());
                forces.remove(id);
            } else if(c.equals(CONSEQUENCES)) {
                int id = Integer.parseInt(entry.getValue());
                consequences.remove(id);
            } else if (blocks.containsKey(c)) {
                TextBlock base = blocks.get(c);
                base.setText(entry.getValue());
                blocks.put(c, base);
            }
        }
    }

    public boolean isEditPastBox() {
        return editPastBox;
    }

    public void setEditPastBox(boolean editPastBox) {
        this.editPastBox = editPastBox;
    }

    public void togglePastBox(ActionEvent e) {
        editPastBox = !editPastBox;
    }

    public List<Consequence> getConsequences() {
        return consequences;
    }

    public List<Force> getForces() {
        return forces;
    }

    public Map<String, TextBlock> getBlocks() {
        return blocks;
    }

    private class UndoEntry<K, V> implements Entry<K, V> {

        private final K key;
        private V value;

        public UndoEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }
    }
}
