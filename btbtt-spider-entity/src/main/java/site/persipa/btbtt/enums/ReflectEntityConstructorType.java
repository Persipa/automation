package site.persipa.btbtt.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ReflectEntityConstructorType {

    NULL(0, "null"),

    BASIC_DATA_TYPE(1, "basic"),

    PACKAGING_DATA_TYPE(2, "packaging"),

    NORMAL_DATA_TYPE(3, "normal"),

    ;

    @EnumValue
    private final Integer code;

    private final String value;
}
