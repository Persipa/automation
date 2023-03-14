package site.persipa.btbtt.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessingTypeEnum {

    SEQUENTIAL(1, "sequential"),

    LOOP(2, "loop"),

    ARRAY(3, "array"),

    ;

    @EnumValue
    private final Integer code;

    private final String value;
}
