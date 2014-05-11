/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.utils;

import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.WikiModel;

public class CustomWikiModel extends WikiModel {

    public CustomWikiModel(String imageBaseURL, String linkBaseURL) {
        super(Configuration.DEFAULT_CONFIGURATION, imageBaseURL, linkBaseURL);
    }

    /**
     * Append the internal wiki image link to this model.
     *
     * <br/><br/><b>Note</b>: the pipe symbol (i.e. &quot;|&quot;) splits the
     * <code>rawImageLink</code> into different segments. The first segment is
     * used as the <code>&lt;image-name&gt;</code> and typically ends with
     * extensions like <code>.png</code>, <code>.gif</code>, <code>.jpg</code> or
     * <code>.jpeg</code>.
     *
     * <br/><br/><b>Note</b>: if the image link contains a "width" attribute, the
     * filename is constructed as <code>&lt;size&gt;px-&lt;image-name&gt;</code>,
     * otherwise it's only the <code>&lt;image-name&gt;</code>.
     *
     * <br/><br/>See <a href="http://en.wikipedia.org/wiki/Image_markup">Image
     * markup</a> and see <a
     * href="http://www.mediawiki.org/wiki/Help:Images">Help:Images</a>
     *
     * @param imageNamespace
     *          the image namespace
     * @param rawImageLink
     *          the raw image link text without the surrounding
     *          <code>[[...]]</code>
     */
    @Override
    public void parseInternalImageLink(String imageNamespace, String rawImageLink) {
        if (fExternalImageBaseURL != null) {
            String imageHref = fExternalWikiBaseURL;
            String imageSrc = fExternalImageBaseURL;
            ImageFormat imageFormat = ImageFormat.getImageFormat(rawImageLink, imageNamespace);

            String imageName = imageFormat.getFilename();
            String sizeStr = imageFormat.getWidthStr();
            if (sizeStr != null) {
                imageName = sizeStr + '-' + imageName;
            }
            if (imageName.endsWith(".svg")) {
                imageName += ".png";
            }
            imageName = Encoder.encodeUrl(imageName);
            if (replaceColon()) {
                imageName = imageName.replace(':', '/');
            }
            String link = imageFormat.getLink();
            System.out.println("Link: " + link);
            if (link != null) {
                if (link.length() == 0) {
                    imageHref = "";
                } else {
                    String encodedTitle = encodeTitleToUrl(link, true);
                    System.out.println("****" + link + " das war link " + encodedTitle);
                    imageHref = imageHref.replace("${title}", encodedTitle);
                }
            } else {
                if (replaceColon()) {
                    imageHref = imageHref.replace("${title}", imageName);
                } else {
                    imageHref = imageHref.replace("${title}", imageName);
                }
            }
            imageSrc = imageSrc.replace("${image}", imageName);

            appendInternalImageLink(imageHref, imageSrc, imageFormat);
        }
    }

    @Override
    public boolean replaceColon() {
        return true;
    }
}
