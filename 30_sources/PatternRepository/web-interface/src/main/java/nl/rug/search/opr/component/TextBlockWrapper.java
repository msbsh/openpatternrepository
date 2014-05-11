package nl.rug.search.opr.component;

import javax.faces.event.ActionEvent;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.template.Component;

/**
 *
 * @author cm
 */
public class TextBlockWrapper extends TextBlock {

    private final TextBlock block;
    private boolean editMode;

    public TextBlockWrapper(TextBlock block) {
        this.block = block;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void toggleMode(ActionEvent e) {
        editMode = !editMode;
    }

    @Override
    public Component getComponent() {
        return block.getComponent();
    }

    @Override
    public String getText() {
        return block.getText();
    }

    @Override
    public Long getId() {
        return block.getId();
    }

    @Override
    public void setComponent(Component component) {
        block.setComponent(component);
    }

    @Override
    public void setText(String text) {
        block.setText(text);
    }

    @Override
    public void setId(Long textBlockId) {
        block.setId(textBlockId);
    }

    public TextBlock getTextBlock() {
        return block;
    }
}

































