package site.persipa.btbtt.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.persipa.cloud.enums.ExceptionLevelEnum;
import site.persipa.cloud.enums.PersipaExceptionDef;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessingExceptionEnum implements PersipaExceptionDef {

    CONSTRUCT_ARGS_COUNT_INCORRECT(10000, "构造参数数量不匹配", ExceptionLevelEnum.EXCEPTION),

    CONSTRUCTOR_NOT_FOUND(10001, "无此构造方法", ExceptionLevelEnum.EXCEPTION),

    METHOD_NOT_FOUND(10100, "无此成员方法", ExceptionLevelEnum.EXCEPTION),

    REFLECTIVE_OPERATION_EXCEPTION(20000, "反射方法执行失败", ExceptionLevelEnum.EXCEPTION),

    METHOD_ARGS_COUNT_INCORRECT(20001, "方法参数数量不匹配", ExceptionLevelEnum.EXCEPTION),

    CLASS_TYPE_NOT_MATCH_EXCEPTION(30000, "类型匹配失败", ExceptionLevelEnum.EXCEPTION),

    CLASS_NOT_FOUND(30001, "无此类", ExceptionLevelEnum.EXCEPTION),

    ;

    private final int code;

    private final String msg;

    private final ExceptionLevelEnum level;

}
