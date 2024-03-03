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
    INIT("init", "init"),

    /**
     * 正在执行
     */
    EXECUTING("executing", "executing"),

    /**
     * 执行成功
     */
    SUCCESS("success", "success"),

    /**
     * 执行失败
     */
    FAIL("fail", "fail"),

    /**
     * 跳过执行
     */
    SKIP("skip", "skip"),

    /**
     * 拒绝执行
     */
    REFUSE("refuse", "refuse"),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;
}
