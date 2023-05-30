package site.persipa.automation.process.util;

import cn.hutool.http.ssl.DefaultSSLFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.persipa.automation.enums.btbtt.UserAgentEnum;

import java.io.IOException;

/**
 * @author persipa
 */
public class JsoupUtil {

    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(UserAgentEnum.randomUA())
                .sslSocketFactory(new DefaultSSLFactory())
//                .headers()
                .get();
    }
}
