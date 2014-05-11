/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.utils;

import info.bliki.html.HTML2WikiConverter;
import info.bliki.html.wikipedia.IHTMLToWiki;
import info.bliki.html.wikipedia.ToWikipedia;
import info.bliki.wiki.model.WikiModel;

/**
 *
 * @author cm
 */
public final class TextProcessor {

    private static final String HTML_TAGS = "\\<.*?\\>";
    private static final String FONT_TAGS = "\\<\\/?(font|FONT).*?\\>";
    private static final IHTMLToWiki converter = new ToWikipedia(false, true);
    private static final String SPECIALS = "\\p{Punct}";

    public static String shorten(String text, int length) {
        if (text.length() > length) {
            text = text.substring(0, length);
        }
        return text;
    }

    public static String shortenDots(String text, int length, String dots) {
        String shorted = TextProcessor.shorten(text, length);

        if (shorted.length() < text.length()) {
            shorted += dots;
        }

        return shorted;
    }

    public static String stripHTML(String html) {
        return html.replaceAll(HTML_TAGS, " ");
    }

    public static String stripFontTag(String html) {
        return html.replaceAll(FONT_TAGS, " ");
    }

    public static String stripSpecials(String specials) {
        return specials.replaceAll(SPECIALS, " ");
    }

    public static String breakText(String text, int chars, String breaker) {
        StringBuilder builder = new StringBuilder();
        
        while (text.length() > chars) {
            String tmp = text.substring(0,chars);
            builder.append(tmp);
            builder.append(breaker);
            text = text.substring(chars);
        }
        builder.append(text);

        return builder.toString();
    }

    public static String html2wiki(String html) {
        HTML2WikiConverter h2w = new HTML2WikiConverter(html);
        return h2w.toWiki(converter);
    }
    

    public static String wiki2html(String wiki) {
        String baseHref = System.getProperty("base.href");
        WikiModel wikiModel =
                new CustomWikiModel(baseHref.concat("/file/${image}"),
                baseHref.concat("/file/${title}")); // baseHref + "file/${title}");
        return wikiModel.render(wiki);
    }
}
