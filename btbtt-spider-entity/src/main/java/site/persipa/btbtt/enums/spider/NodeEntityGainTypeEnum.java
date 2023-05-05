package site.persipa.btbtt.enums.spider;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
