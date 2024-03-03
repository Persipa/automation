package site.persipa.automation.enums.reflect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ReflectEntityConstructorType {

    NULL("null", "null"),

    BASIC_DATA_TYPE("basic", "basic"),

    PACKAGING_DATA_TYPE("packaging", "packaging"),

    NORMAL_DATA_TYPE("normal", "normal"),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;
}
