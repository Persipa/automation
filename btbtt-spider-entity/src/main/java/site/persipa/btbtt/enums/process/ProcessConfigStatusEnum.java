package site.persipa.btbtt.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import site.persipa.cloud.helper.enums.EnumFindHelper;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessConfigStatusEnum {

    /**
     * 初始化，新建的状态
     */
    INIT(0, "init", false),

    /**
     * 正在编辑，不可执行
     */
    EDITING(10, "editing", false),

    /**
     * 已保存，可执行，但待验证
     */
    SAVED(11, "saved", true),

    /**
     * 可执行，验证通过
     */
    VERIFY_PASS(21, "verifyPass", true),

    /**
     * 不可执行，验证不通过
     */
    VERIFY_FAIL(20, "verifyFail", false),

    /**
     * 执行发生错误，停止
     */
    PROCESSING_ERROR(30, "processingError", false),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;

    private final boolean executable;

    public static final EnumFindHelper<ProcessConfigStatusEnum, String> VALUE_HELPER =
            new EnumFindHelper<>(ProcessConfigStatusEnum.class, ProcessConfigStatusEnum::getValue);
}
