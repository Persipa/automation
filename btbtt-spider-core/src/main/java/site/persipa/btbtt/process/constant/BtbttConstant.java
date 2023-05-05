package site.persipa.btbtt.process.constant;

/**
 * @author persipa
 */
public class BtbttConstant {

    private static final String HOST = "http://www.btbtt11.com/";

    public static String postUrl(String path) {
        return HOST + path;
    }
}
