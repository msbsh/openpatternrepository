package nl.rug.search.opr.webservices.types;

import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.License;

/**
 *
 * @author cm
 */
public class FileDTO {

    private Long id;
    private String mime;
    private License license;
    private String name;

    public FileDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static FileDTO assemble(File f) {
        FileDTO fdto = new FileDTO();
        fdto.setId(f.getId());
        fdto.setMime(f.getMime());
        fdto.setLicense(f.getLicense());
        fdto.setName(f.getName());
        return fdto;
    }

}
