package site.persipa.btbtt.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessingExceptionEnum {

    CONSTRUCT_ARGS_COUNT_INCORRECT(10000, "构造参数数量不匹配"),

    CONSTRUCTOR_NOT_FOUND(10001, "无此构造方法"),

    METHOD_NOT_FOUND(10100, "无此成员方法"),

    REFLECTIVE_OPERATION_EXCEPTION(20000, "反射方法执行失败"),

    METHOD_ARGS_COUNT_INCORRECT(20001, "方法参数数量不匹配"),

    CLASS_TYPE_NOT_MATCH_EXCEPTION(30000, "类型匹配失败"),

    CLASS_NOT_FOUND(30001, "无此类"),

    ;

    private final Integer code;

    private final String msg;

    }
