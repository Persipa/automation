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
public enum ReflectExceptionEnum implements PersipaExceptionDef {

    REFLECT_CLASS_EXIST(999, "类已存在", ExceptionLevelEnum.WARNING),

    REFLECT_CLASS_NOT_FOUND(999, "反射类不存在", ExceptionLevelEnum.EXCEPTION),

    RELECT_CLASS_TYPE_NOT_MATCH(999, "反射类类型不匹配", ExceptionLevelEnum.EXCEPTION),

    REFLECT_METHOD_NOT_FOUND(999, "反射方法不存在", ExceptionLevelEnum.EXCEPTION),

    REFLECT_METHOD_ARGS_COUNT_INCORRECT(999, "方法参数数量不匹配", ExceptionLevelEnum.ERROR),

    REFLECT_METHOD_ARGS_POSITION_INCORRECT(999, "方法参数位置不匹配", ExceptionLevelEnum.ERROR),

    REFLECT_METHOD_RETURN_TYPE_INCORRECT(999, "方法返回类型不匹配", ExceptionLevelEnum.ERROR),

    REFLECT_CONSTRUCTOR_NOT_FOUND(999, "未知构造器", ExceptionLevelEnum.EXCEPTION),

    REFLECT_CONSTRUCTOR_ARGS_COUNT_INCORRECT(999, "构造参数数量不匹配", ExceptionLevelEnum.ERROR),

    REFLECT_CONSTRUCTOR_ARGS_POSITION_INCORRECT(999, "构造参数位置不匹配", ExceptionLevelEnum.ERROR),

    REFLECT_ENTITY_NOT_INDEPENDENT(999, "反射实例树结构有误", ExceptionLevelEnum.WARNING),

    REFLECT_ENTITY_CONSTRUCT_FAIL(999, "反射实例构造失败", ExceptionLevelEnum.ERROR),

    REFLECT_ENTITY_NOT_FOUND(999, "反射实例不存在", ExceptionLevelEnum.EXCEPTION),

    REFLECT_ENTITY_IN_USE(999, "反射实例正在使用", ExceptionLevelEnum.WARNING),

    /**
     * 原生反射方法操作异常
     *
     * @see ReflectiveOperationException
     */
    REFLECTIVE_OPERATION_EXCEPTION(999, "反射操作异常", ExceptionLevelEnum.ERROR),

    /**
     * 原生类型未找到异常
     *
     * @see ClassNotFoundException
     */
    CLASS_NOT_FOUND_EXCEPTION(999, "类型未找到", ExceptionLevelEnum.ERROR),

    /**
     * 原生方法未找到异常
     *
     * @see NoSuchMethodException
     */
    NO_SUCH_METHOD_EXCEPTION(999, "方法未找到", ExceptionLevelEnum.ERROR),

    ;


    private final int code;

    private final String msg;

    private final ExceptionLevelEnum level;

}
