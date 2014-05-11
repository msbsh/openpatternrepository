package nl.rug.search.opr.file;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public class ThumbnailCreator {

    public static BufferedImage scaleByWidth(BufferedImage image, int width) {
        double factor = ((double) image.getWidth() / (double) width);
        int height = (int) (image.getHeight() / factor);
        return scale(image, width, height);
    }

    public static BufferedImage scaleByHeight(BufferedImage image, int height) {
        double yfactor = ((double) image.getHeight() / (double) height);
        int width = (int) (image.getWidth() / yfactor);
        return scale(image, width, height);
    }

    public static BufferedImage scale(BufferedImage image, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform at = AffineTransform.getScaleInstance((double) width / image.getWidth(), (double) height / image.getHeight());
            g.drawRenderedImage(image, at);
        } finally {
            g.dispose();
        }
        return scaled;

    }

    public static BufferedImage scaleCubic(BufferedImage image, int size) {
        
        BufferedImage thumb =  scaleByHeight(image, size);

        if (thumb.getWidth() > size) {
            thumb =  scaleByWidth(thumb, size);
        }

        return thumb;
    }
}
