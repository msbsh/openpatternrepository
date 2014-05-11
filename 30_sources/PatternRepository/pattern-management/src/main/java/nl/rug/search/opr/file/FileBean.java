package nl.rug.search.opr.file;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.imageio.ImageIO;
import javax.persistence.Query;
import nl.rug.search.opr.ChecksumUtil;
import nl.rug.search.opr.ConfigConstants;
import nl.rug.search.opr.Configuration;
import nl.rug.search.opr.dao.GenericDaoBean;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.License;
import org.semanticdesktop.aperture.mime.identifier.MimeTypeIdentifier;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;
import org.semanticdesktop.aperture.util.IOUtil;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 * @author Martin Verspai <google@verspai.de>
 * @version 2.0
 */
@Stateless
public class FileBean extends GenericDaoBean<File, Long> implements FileLocal {

    private static final int FILE_EDITION_TIME_IN_HOURS = 2;
    private static final int MAX_THUMBNAIL_WIDTH_IN_PX = 1000;
    private static final Logger logger = Logger.getLogger(FileBean.class.getName());
    @Resource
    private TimerService timerService;
    private MimeTypeIdentifier identifier = new MagicMimeTypeIdentifier();

    @Override
    public void init() {
        /*
         * This is set to be the default value
         * Gets overwritten by properties file
         */
        int cleanUpFiles = 90;
        int readint = -1;

        try {
            Object obj = Configuration.getInstance().getProperty("cleanUpIntervallInMins");
            readint = Integer.parseInt(obj.toString());

            if (readint > 0) {
                cleanUpFiles = readint;
            }

        } catch (NumberFormatException e) {
            logger.log(Level.INFO, "File Janitor: Mal formated input string: {0} for recurrent timer execution."
                    + "\nSetting timer to: {1} minutes.", new Object[]{readint, cleanUpFiles});
        } catch (Exception ex) {
        }

        timerService.createTimer(timeInMillis(1, TimeUnit.minute), timeInMillis(cleanUpFiles, TimeUnit.minute), null);
        logger.log(Level.INFO, "File Janitor: Created recurring timer.\nExecution set to every {0} minutes.", cleanUpFiles);
    }

    @Timeout
    private void timerExpired(Timer timer) {
        cleanUpFiles();
    }

    @Override
    public void cleanUpFiles() {
        try {
            List<File> files = getAll();

            for (File f : files) {
                if (editionTimeExpired(f.getDateCreated()) && !hasReference(f)) {
                    logger.log(Level.INFO, "File Janitor: Removing file: {0}", f.getName());
                    removeFile(f);
                }
            }
        } catch (Exception e) {
            logger.info("File Janitor: Encountered an error while cleaning files!");
        }
    }

    private boolean editionTimeExpired(Date date) {
        long difference = new Date().getTime() - date.getTime();

        return (difference > timeInMillis(FILE_EDITION_TIME_IN_HOURS, TimeUnit.hour)) ? true : false;
    }

    @Override
    public void removeFile(File file) {

        if (file == null) {
            throw new IllegalArgumentException("A Null entity cannot be deleted");
        }

        if (!hasReference(file)) {
            makeTransient(file);
        }
    }

    @Override
    @Deprecated
    public File makePersistent(File entity) {
        throw new UnsupportedOperationException("Use add to store a file in the database");
    }

    @Override
    public File add(License license, String name, InputStream is) throws FileException, IOException {

        ByteArrayOutputStream bos = null;
        try {
            byte[] magicIdentifier = IOUtil.readBytes(is, identifier.getMinArrayLength());
            String mimeType = identifier.identify(magicIdentifier, "", null);
            List<String> supportedMimeTypes = getSupportedMimeTypes();
            if (!supportedMimeTypes.contains(mimeType)) {
                throw new FileException(String.format("Mime type '%s' not supported. Supported are %s",
                        mimeType, getSupportedMimeTypes().toString()));
            }

            bos = new ByteArrayOutputStream();
            bos.write(magicIdentifier); // neccesary, cause stream is not resetted
            int b = is.read();
            while (b != -1) {
                bos.write(b);
                b = is.read();
            }

            byte[] content = bos.toByteArray();

            // check file size
            int fileSizeMb = getMaximumFileSizeMb();
            float sizeKb = content.length / 1024;
            float sizeMb = sizeKb / 1024;
            if ((sizeMb) > fileSizeMb) {
                throw new FileException(String.format(
                        "File is too large. Maximum: %d MB", fileSizeMb));
            }

            File f = super.makePersistent(mapToFile(name, mimeType, license, content));

            return f;

        } finally {
            try {
                bos.close();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    private File mapToFile(String name, String mime, License license, byte[] content) {
        String checksum = ChecksumUtil.calculate(content);

        File file = new File();

        file.setName(name);
        file.setMime(mime);
        file.setContent(content);
        file.setChecksum(checksum);
        file.setLicense(license);
        file.setSizeInBytes(content.length);

        return file;
    }

    @Override
    public byte[] getThumbnail(File file, int size) {

        ByteArrayOutputStream bos = null;
        try {
            if (!file.getMime().startsWith("image")) {
                return null;
            }

            if (size > MAX_THUMBNAIL_WIDTH_IN_PX) {
                return getThumbnail(file, MAX_THUMBNAIL_WIDTH_IN_PX);
            }

            bos = new ByteArrayOutputStream();
            try {
                BufferedImage image = ImageIO.read(file.getContentAsStream());
                BufferedImage thumb = ThumbnailCreator.scaleByWidth(image, size);
                ImageIO.write(thumb, "PNG", bos);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public byte[] getThumbnail(File file, int size, boolean scaleCubic) {

        ByteArrayOutputStream bos = null;
        try {
            if (!file.getMime().startsWith("image")) {
                return null;
            }

            if (size > MAX_THUMBNAIL_WIDTH_IN_PX) {
                return getThumbnail(file, MAX_THUMBNAIL_WIDTH_IN_PX, scaleCubic);
            }

            bos = new ByteArrayOutputStream();
            try {
                BufferedImage image = ImageIO.read(file.getContentAsStream());
                BufferedImage thumb = null;
                if (scaleCubic) {
                    thumb = ThumbnailCreator.scaleCubic(image, size);
                } else {
                    thumb = ThumbnailCreator.scaleByWidth(image, size);
                }
                ImageIO.write(thumb, "PNG", bos);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }

            return bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean hasReference(File f) {
        Query q = createNativeQuery(ConfigConstants.QUERY_HAS_REFERENCES);
        q.setParameter(1, f.getId());

        int result = Integer.parseInt(((java.util.AbstractCollection<Object>) q.getSingleResult()).iterator().next().toString());

        return result > 0 ? true : false;
    }

    private long timeInMillis(long value, TimeUnit unit) {
        long calculated = value;

        switch (unit) {
            case second:
                calculated *= (1000);
                break;
            case minute:
                calculated *= (60 * 1000);
                break;
            case hour:
                calculated *= (60 * 60 * 1000);
                break;
            case day:
                calculated *= (60 * 60 * 24 * 1000);
                break;
            default:
                break;
        }

        return calculated;
    }

    private enum TimeUnit {

        second, minute, hour, day;
    }

    @Override
    public List<String> getSupportedMimeTypes() {
        return Configuration.getInstance().getList(
                ConfigConstants.UPLOAD_SUPPORTED_MIME_TYPES);
    }

    @Override
    public int getMaximumFileSizeMb() {
        String fileSize = Configuration.getInstance().getProperty(
                ConfigConstants.UPLOAD_MAX_SIZE_MB).toString();
        return Integer.parseInt(fileSize);
    }
}
