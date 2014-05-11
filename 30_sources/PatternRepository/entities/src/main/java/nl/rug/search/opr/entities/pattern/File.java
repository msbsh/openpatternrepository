package nl.rug.search.opr.entities.pattern;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import nl.rug.search.opr.entities.BaseEntity;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 06.10.2009
 */
@Entity
public class File extends BaseEntity<File> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_FILE")
    @TableGenerator(name = "PK_FILE", allocationSize = 5, initialValue = 0)
    private Long id;
    private String mime;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private License license;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    @Column(nullable = false)
    private String checksum;
    private int sizeInBytes;

    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated;

    public File() {
        this.dateCreated = new Date();
    }

    public File(String name, String type) {
        this();
        this.name = name;
        this.mime = type;
    }

    public byte[] getContent() {
        return content;
    }

    public InputStream getContentAsStream() {

        return new ByteArrayInputStream(content);
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    protected boolean dataEquals(File other) {
        return areEqual(name, other.name);
    }

    @Override
    protected Object[] getHashCodeData() {
        return new Object[]{name};
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public File getThis() {
        return this;
    }

}
