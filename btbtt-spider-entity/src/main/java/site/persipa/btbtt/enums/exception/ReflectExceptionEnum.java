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

    CLASS_NOT_FOUND(999, "未知类", ExceptionLevelEnum.EXCEPTION),

    METHOD_NOT_FOUND(999, "未知方法", ExceptionLevelEnum.EXCEPTION),

    CONSTRUCTOR_NOT_FOUND(999, "未知构造器", ExceptionLevelEnum.EXCEPTION),

    REFLECT_ENTITY_NOT_INDEPENDENT(999, "反射实例树结构有误", ExceptionLevelEnum.WARNING),

    REFLECT_ENTITY_CONSTRUCT_FAIL(999, "反射实例构造失败", ExceptionLevelEnum.ERROR),

    REFLECT_ENTITY_NOT_FOUND(999, "未知反射实例", ExceptionLevelEnum.EXCEPTION),

    ;


    private final int code;

    private final String msg;

    private final ExceptionLevelEnum level;

}
