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
public enum ProcessTypeEnum {

    /**
     * 自动执行
     */
    AUTO(1, "auto", true),

    /**
     * 手动执行
     */
    MANUAL(2, "manual", true),


    /**
     * 预览
     */
    PREVIEW(21, "preview", false),

    /**
     * 测试
     */
    TEST(22, "test", false),

    /**
     * 远程调用
     */
    REMOTE_CALL(3, "remoteCall", true),
    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;

    private final boolean saveResult;

}
