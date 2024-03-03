package site.persipa.automation.enums.reflect;

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
    VOID("void", "void"),

    /**
     * 方法返回基础累心
     */
    BASIC("basic", "basic"),

    /**
     * 方法返回一个普通的对象
     */
    INSTANCE("instance", "instance"),

    /**
     * 方法返回一个数组
     */
    ARRAY("array", "array"),

    /**
     * 返回返回可遍历的对象
     */
    COLLECTION("collection", "collection"),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String value;
}
