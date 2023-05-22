package site.persipa.btbtt.enums.reflect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum PackagingDataTypeEnum {

    INTEGER(10, "Integer", Integer.class),

    SHORT(11, "Short", Short.class),

    LONG(12, "Long", Long.class),

    FLOAT(21, "Float", Float.class),

    DOUBLE(22, "Double", Double.class),

    CHARACTER(31, "Character", Character.class),

    STRING(32, "String", String.class),

    BYTE(4, "Byte", Byte.class),

    BOOLEAN(5, "Boolean", Boolean.class),

    ;

    @EnumValue
    private final int code;

    private final String value;

    private final Class<?> packagingType;

    public static PackagingDataTypeEnum parseByClassName(String className) {
        if (className.startsWith("java.lang.")) {
            className = className.replace("java.lang.", "");
        } else {
            return null;
        }
        for (PackagingDataTypeEnum typeEnum : PackagingDataTypeEnum.values()) {
            if (typeEnum.getValue().equals(className)) {
                return typeEnum;
            }
        }
        return null;
    }
}
