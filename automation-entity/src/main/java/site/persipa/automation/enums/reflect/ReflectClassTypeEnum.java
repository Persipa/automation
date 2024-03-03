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
public enum ReflectClassTypeEnum {

    BASIC_DATA_TYPE("basic", "basic"),

    PACKAGING_DATA_TYPE("packaging", "packaging"),

    NORMAL_DATA_TYPE("normal", "normal"),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;

    public static ReflectClassTypeEnum parseByValue(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        for (ReflectClassTypeEnum classType : ReflectClassTypeEnum.values()) {
            if (classType.getValue().equals(value)) {
                return classType;
            }
            return null;
        }
        return null;
    }
}
