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
public enum ProcessTypeEnum {

    /**
     * 自动执行
     */
    AUTO(1, "auto"),

    /**
     * 手动执行
     */
    MANUAL(2, "manual"),

    /**
     * 预览
     */
    PREVIEW(21, "preview"),

    /**
     * 测试
     */
    TEST(22, "test"),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;

}
