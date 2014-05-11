package nl.rug.search.utils;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author cm
 */
public class TagDictionary {

    public static final String TAG_BLACKLIST = "META-INF/tagBlacklist.xml.zip";
    private HashSet<String> wordList = new HashSet<String>();

    private static class Holder {

        private static final TagDictionary INSTANCE = new TagDictionary();
    }

    private TagDictionary() {
        synchronized (this) {
            try {
                init();
            } catch (IOException ex) {
                System.out.println("Could not initialize Blacklist");
            }
        }
    }

    public static TagDictionary getInstance() {
        return Holder.INSTANCE;
    }

    private void init() throws IOException {

        InputStream is = TagDictionary.class.getClassLoader().getResourceAsStream(TAG_BLACKLIST);

        if (is == null) {
            throw new IOException();
        }

        ZipInputStream zipStream = new ZipInputStream(is);
        ZipEntry firstEntry = zipStream.getNextEntry();
        XMLDecoder xDecoder = new XMLDecoder(zipStream);

        wordList.clear();
        wordList.addAll((List<String>) xDecoder.readObject());

        xDecoder.close();
        zipStream.close();

        if (wordList == null) {
            throw new IOException();
        }
    }
    
    public boolean isBlacklisted(String word) {
        return wordList.contains(word.toLowerCase());
    }
}
