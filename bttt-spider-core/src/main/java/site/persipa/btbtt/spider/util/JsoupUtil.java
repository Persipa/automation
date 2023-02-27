package site.persipa.btbtt.spider.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.persipa.btbtt.spider.enums.UserAgentEnum;

import java.io.IOException;

/**
 * @author persipa
 */
public class JsoupUtil {

    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(UserAgentEnum.randomUA())
//                .headers()
                .get();
    }
}
