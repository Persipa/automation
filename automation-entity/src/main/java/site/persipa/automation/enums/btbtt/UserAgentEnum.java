package site.persipa.automation.enums.btbtt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

/**
 * 枚举的UA
 *
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum UserAgentEnum {

    /**
     * Windows 10 Edge 90
     */
    WIN_10_EDGE_90("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36 Edg/90.0.818.51"),

    /**
     * macOS 10.15.7 Safari 15.0
     */
    MAC_1015_SAFARI_15("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15"),

    /**
     * Windows 10 Edge 97
     */
    WIN_10_EDGE_97("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36 Edg/97.0.1072.69"),

    ;

    private final String userAgent;

    public static String randomUA() {
        UserAgentEnum[] values = UserAgentEnum.values();
        Random random = new Random();
        int i = random.nextInt(values.length);
        return values[i].getUserAgent();
    }

}
