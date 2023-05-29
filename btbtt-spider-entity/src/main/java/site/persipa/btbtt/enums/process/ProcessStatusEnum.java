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
public enum ProcessStatusEnum {

    /**
     * 初始化
     */
    INIT(1, "init"),

    /**
     * 正在执行
     */
    EXECUTING(5, "executing"),

    /**
     * 执行成功
     */
    SUCCESS(91, "success"),

    /**
     * 执行失败
     */
    FAIL(92, "fail"),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;
}
