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

    SEQUENTIAL("sequential", "sequential"),

    LOOP("loop", "loop"),

    ARRAY("array", "array"),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;
}
