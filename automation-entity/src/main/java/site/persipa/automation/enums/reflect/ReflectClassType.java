package site.persipa.automation.enums.reflect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ReflectClassType {

    BASIC_DATA_TYPE(1, "basic"),

    PACKAGING_DATA_TYPE(2, "packaging"),

    NORMAL_DATA_TYPE(3, "normal"),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;

    public static ReflectClassType parseByValue(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        for (ReflectClassType classType : ReflectClassType.values()) {
            if (classType.getValue().equals(value)) {
                return classType;
            }
            return null;
        }
        return null;
    }
}
