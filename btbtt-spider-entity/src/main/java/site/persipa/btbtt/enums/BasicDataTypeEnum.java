package site.persipa.btbtt.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum BasicDataTypeEnum {

    INT(10, "int", Integer.class),

    SHORT(11, "short", Short.class),

    LONG(12, "long", Long.class),

    FLOAT(21, "float", Float.class),

    DOUBLE(22, "double", Double.class),

    CHAR(3, "char", Character.class),

    BYTE(4, "byte", Byte.class),

    BOOLEAN(5, "boolean", Boolean.class),

    ;

    @EnumValue
    private final int code;

    private final String value;

    private final Class<?> packagingType;

    public static BasicDataTypeEnum parseByName(String name) {
        for (BasicDataTypeEnum typeEnum : BasicDataTypeEnum.values()) {
            if (typeEnum.getValue().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
