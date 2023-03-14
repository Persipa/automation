package site.persipa.btbtt.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum JsoupClassType {

    BASIC_DATA_TYPE(1, "basic"),

    PACKAGING_DATA_TYPE(2, "packaging"),

    ;

    @EnumValue
    private final int code;

    private final String value;
}
