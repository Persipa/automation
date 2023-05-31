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

    /**
     * 跳过执行
     */
    SKIP(93, "skip"),

    /**
     * 拒绝执行
     */
    REFUSE(94, "refuse"),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;
}
