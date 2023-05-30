package site.persipa.automation.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import site.persipa.cloud.helper.enums.EnumFindHelper;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum NodeEntityGainTypeEnum {

    /**
     * 参数是由上一个执行输入
     */
    INPUT(0, "input"),

    /**
     * 参数是由Entity 自行构造
     */
    CONSTRUCT(1, "construct"),

    ;

    @EnumValue
    private final Integer code;

    private final String value;

    public static final EnumFindHelper<NodeEntityGainTypeEnum, String> VALUE_HELPER =
            new EnumFindHelper<>(NodeEntityGainTypeEnum.class, NodeEntityGainTypeEnum::getValue);
}
