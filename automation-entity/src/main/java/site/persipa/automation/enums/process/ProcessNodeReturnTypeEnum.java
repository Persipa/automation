package site.persipa.automation.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 */
@Getter
@AllArgsConstructor
public enum ProcessNodeReturnTypeEnum {

    VOID(0, "void"),

    BASIC(1, "basic"),

    INSTANCE(3, "instance"),

    ARRAY(4, "array"),

    COLLECTION(5, "collection"),

    ;

    @EnumValue
    private final int code;

    private final String value;

}
