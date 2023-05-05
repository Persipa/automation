package site.persipa.btbtt.enums.spider;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessConfigStatusEnum {

    /**
     * 初始化，新建的状态
     */
    INIT(0, "init"),

    /**
     * 正在编辑，不可执行
     */
    EDITING(10, "editing"),

    /**
     * 已保存，可执行，但待验证
     */
    SAVED(11, "saved"),

    /**
     * 可执行，验证通过
     */
    VERIFY_PASS(21, "verifyPass"),

    /**
     * 不可执行，验证不通过
     */
    VERIFY_FAIL(20, "verifyFail"),

    /**
     * 执行发生错误，停止
     */
    PROCESSING_ERROR(30, "processingError"),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;
}
