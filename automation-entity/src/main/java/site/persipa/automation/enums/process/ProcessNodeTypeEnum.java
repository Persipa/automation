package site.persipa.automation.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessNodeTypeEnum {

    SEQUENTIAL(1, "sequential"),

    LOOP(2, "loop"),

    ARRAY(3, "array"),

    ;

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String value;
}
