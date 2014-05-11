
package nl.rug.search.opr.controller;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.file.FileLocal;

/**
 *
 * @author cm
 */
@ManagedBean(name="listFilesController")
@RequestScoped
public class ListFilesController {

    @EJB
    private FileLocal fileBean;


    private boolean preview = false;
    private byte[] content = new byte[]{};

    public List<File> getFiles() {
        return fileBean.getAll();
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public void preview(ActionEvent e) {
        File id = (File)e.getComponent().getAttributes().get("identification");
        content = new byte[]{};
        File file = fileBean.getById(id.getId());
        content = file.getContent();
        preview = true;
    }

}
