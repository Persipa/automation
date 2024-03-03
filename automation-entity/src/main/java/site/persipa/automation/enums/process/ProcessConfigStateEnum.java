package site.persipa.automation.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import site.persipa.common.entity.enums.EnumFindHelper;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessConfigStateEnum {

    /**
     * 初始化，新建的状态
     */
    INIT("init", "init", false),

    /**
     * 正在编辑，不可执行
     */
    EDITING("editing", "editing", false),

    /**
     * 已保存，可执行，但待验证
     */
    SAVED("saved", "saved", true),

    /**
     * 可执行，验证通过
     */
    VERIFY_PASS("verifyPass", "verifyPass", true),

    /**
     * 不可执行，验证不通过
     */
    VERIFY_FAIL("verifyFail", "verifyFail", false),

    /**
     * 执行发生错误，停止
     */
    PROCESSING_ERROR("processingError", "processingError", false),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;

    private final boolean executable;

    public static final EnumFindHelper<ProcessConfigStateEnum, String> VALUE_HELPER =
            new EnumFindHelper<>(ProcessConfigStateEnum.class, ProcessConfigStateEnum::getValue);
}
