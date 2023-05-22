package site.persipa.btbtt.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessNodeStatusEnum {

    EDITING(1, "editing"),

    SAVED(2, "saved"),

    EXCEPTION(3, "exception"),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;

}
