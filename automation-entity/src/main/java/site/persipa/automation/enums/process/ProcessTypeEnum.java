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
    AUTO("auto", "auto", true),

    /**
     * 手动执行
     */
    MANUAL("manual", "manual", true),


    /**
     * 预览
     */
    PREVIEW("preview", "preview", false),

    /**
     * 测试
     */
    TEST("test", "test", false),

    /**
     * 远程调用
     */
    REMOTE_CALL("remoteCall", "remoteCall", true),
    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;

    private final boolean saveResult;

}
