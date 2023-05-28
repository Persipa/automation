package site.persipa.btbtt.enums.reflect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum MethodResultTypeEnum {

    /**
     * 方法无返回值
     */
    VOID(0, "void"),

    /**
     * 方法返回基础累心
     */
    BASIC(1, "basic"),

    /**
     * 方法返回一个普通的对象
     */
    INSTANCE(2, "instance"),

    /**
     * 方法返回一个数组
     */
    ARRAY(3, "array"),

    /**
     * 返回返回可遍历的对象
     */
    COLLECTION(4, "collection"),

    ;

    @EnumValue
    private final int code;

    @JsonValue
    private final String value;
}
