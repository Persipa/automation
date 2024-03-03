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
public enum ProcessNodeStatusEnum {

    EDITING("editing", "editing"),

    SAVED("saved", "saved"),

    EXCEPTION("exception", "exception"),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;

}
