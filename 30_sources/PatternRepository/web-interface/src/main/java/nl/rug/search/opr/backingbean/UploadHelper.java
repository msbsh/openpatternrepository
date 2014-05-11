package nl.rug.search.opr.backingbean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import nl.rug.search.opr.entities.pattern.File;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import nl.rug.search.opr.file.FileException;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.file.FileLocal;
import org.icefaces.component.fileentry.FileEntry;
import org.icefaces.component.fileentry.FileEntryEvent;
import org.icefaces.component.fileentry.FileEntryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @version 2.0
 * @date 26.10.2009
 */
@ManagedBean(name = "uploadHelper")
@ViewScoped
public class UploadHelper {

    
    protected PatternVersion version;
    private License license;
    private FileEntryResults.FileInfo fileInfo;
    private String name;
    private UIComponent uploadComponent;
    @EJB
    private FileLocal fileBean;
    private Logger logger = LoggerFactory.getLogger(UploadHelper.class);


    public UploadHelper() {
    }

    @PostConstruct
    public void init() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String beanName = ctx.getExternalContext().getRequestParameterMap().get("patternVersion");

        version = findBean(beanName, PatternVersion.class);
    }

    public static <T> T findBean(String beanName, Class<T> beanClass) {
        FacesContext context = FacesContext.getCurrentInstance();
        return beanClass.cast(context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", beanClass));
    }

    public PatternVersion getVersion() {
        return version;
    }

    public void setVersion(PatternVersion version) {
        this.version = version;
    }

    public void listener(FileEntryEvent event) {
        FileEntry fileEntry = (FileEntry) event.getSource();
        FileEntryResults results = fileEntry.getResults();

        for (FileEntryResults.FileInfo fi : results.getFiles()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (fi.isSaved()) {
                fileInfo = fi;
                name = fi.getFileName();
                saveFile(fileInfo, name);
            }
        }

    }

    private void saveFile(FileEntryResults.FileInfo fileInfo, String name) {


        try {

            File file = null;
            if (fileInfo.getStatus().isSuccess()) {

                String fileName = fileInfo.getFileName();
                file = fileBean.add(license, fileName, new FileInputStream(fileInfo.getFile()));
            }
            version.getFiles().add(file);

        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("IO exception");
        } catch (FileException ex) {
        } finally {
            java.io.File physicalFile = fileInfo.getFile();
            if (physicalFile.exists()) {
                physicalFile.delete();
            }
            fileInfo = null;
            name = "";
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public UIComponent getUploadComponent() {
        return uploadComponent;
    }

    public void setUploadComponent(UIComponent uploadComponent) {
        this.uploadComponent = uploadComponent;
    }

    public String getSupportedFileTypes() {
        String out = "";
        for (String fileType : fileBean.getSupportedMimeTypes()) {

            String[] split = fileType.split("/");

            if (split.length == 2) {
                out = out.concat("." + split[1] + ", ");
            }
        }
        out = out.substring(0, out.length() - 2);

        return out;
    }

    public int getMaximumFileSize() {
        return fileBean.getMaximumFileSizeMb();
    }
}
