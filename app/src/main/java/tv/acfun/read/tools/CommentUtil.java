package tv.acfun.read.tools;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;

import com.harreke.easyapp.listeners.OnTagClickListener;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/08
 */
public class CommentUtil {
    //    private static EmotGetter mEmotGetter = null;
    private final static String TAG = "CommentUtil";

    public static Spanned convertUBB(String input, OnTagClickListener tagClickListener) {
        SAXParserFactory factory;
        SAXParser parser;
        XMLReader reader;
        Spanned spanned = null;

        input = input.replaceAll("\\[emot=([a-z]+?),([0-9]+?)/\\]", "<img src=\"$1/$2\"/>");
        input = input.replaceAll("\\[color=#([a-z0-9]+?)\\]", "<font color=\"#$1\">").replace("[/color]", "</font>");
        input = input.replaceAll("\\[font=[\\S\\s]+?\\]", "").replace("[/font]", "");
        input = input.replaceAll("\\[size=([0-9]+?)px\\]", "<font size=\"$1px;\">").replace("[/size]", "</font>");
        input = input.replace("[b]", "<b>").replace("[/b]", "</b>");

        input = input.replaceAll("\\[img=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/img\\]", "<a href=\"$2\">$1</a>&nbsp;");
        input = input.replaceAll("\\[at\\]([\\S\\s]+?)\\[/at\\]", "<at>$1</at>");

        Log.e(null, input);

        spanned = Html.fromHtml(input, tagClickListener);

        return spanned;
    }

    private static class HtmlParser implements ContentHandler {
        private String input;
        private XMLReader reader;

        public HtmlParser(String input) {
            this.input = input;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

        }

        @Override
        public void endDocument() throws SAXException {

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {

        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

        }

        public Spanned parse() {
            SpannableStringBuilder builder;

            try {
                reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                reader.setContentHandler(this);
                reader.parse(new InputSource(new StringReader(input)));
                builder = new SpannableStringBuilder();
            } catch (ParserConfigurationException e) {
                Log.e(TAG, "Parser configuration exception");
            } catch (SAXException e) {
                Log.e(TAG, "SAX exception");
            } catch (IOException e) {
                Log.e(TAG, "IO exception");
            }

            return null;
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {

        }

        @Override
        public void setDocumentLocator(Locator locator) {

        }

        @Override
        public void skippedEntity(String name) throws SAXException {

        }

        @Override
        public void startDocument() throws SAXException {

        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {

        }
    }

    //    public static EmotGetter getEmotGetter() {
    //        if (mEmotGetter == null) {
    //            mEmotGetter = new EmotGetter();
    //        }
    //
    //        return mEmotGetter;
    //    }

    //    private static class EmotGetter implements Html.ImageGetter {
    //        @Override
    //        public Drawable getDrawable(String source) {
    //            return FileUtil.readDrawable(AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/" + source,
    //                    (int) (48 * AcFunRead.Density), (int) (48 * AcFunRead.Density));
    //        }
    //    }
    //
    //    public class CommentHandler implements Html.TagHandler {
    //        private int end = 0;
    //        private int start = 0;
    //        private OnTagClickListener mTagClickListener;
    //
    //        public CommentHandler(OnTagClickListener tagClickListener) {
    //            mTagClickListener = tagClickListener;
    //        }
    //
    //        private void endLink(String tag, Editable output) {
    //            end = output.length();
    //            output.setSpan(new TagClickableSpan());
    //        }
    //
    //        @Override
    //        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
    //            if (tag.equals("link")) {
    //                if (opening) {
    //                    startLink(tag, output);
    //                } else {
    //                    endLink(tag, output);
    //                }
    //            } else if (tag.equals("at")) {
    //                //                if (opening) {
    //                //                    startAt(tag, output);
    //                //                } else {
    //                //                    endAt(tag, output);
    //                //                }
    //            }
    //        }
    //
    //        private void startLink(String tag, Editable output) {
    //            start = output.length();
    //        }
    //    }
}
