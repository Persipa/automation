package site.persipa.automation.process.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * @author persipa
 */
public class TemplateUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String fill(String template, Object... objects) {
        Set<String> paramSet = extractParam(template);
        if (paramSet.isEmpty()) {
            return template;
        }

        Map<String, Object> paramMap = new HashMap<>();
        if (objects != null) {
            for (Object object : objects) {
                paramMap.putAll(convertObject(object));
            }
        }

        for (String param : paramSet) {
            template = template.replace("{" + param + "}", paramMap.getOrDefault(param, "").toString());
        }

        return template;
    }

    private static Set<String> extractParam(String template) {
        Set<String> result = new HashSet<>();
        char[] chars = template.toCharArray();
        int startIndex = -1, endIndex = -1;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '{') {
                startIndex = i;
            }else if (c == '}') {
                endIndex = i;
            }
            if (startIndex != -1 && endIndex != -1) {
                result.add(template.substring(startIndex+1, endIndex));
                startIndex = -1;
                endIndex = -1;
            }
        }
        return result;
    }

    private static Map<String, Object> convertObject(Object o) {
        if (o == null) {
            return Collections.emptyMap();
        }
        return objectMapper.convertValue(o, new TypeReference<>(){});
    }

}
