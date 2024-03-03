package site.persipa.automation.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.persipa.common.entity.enums.ExceptionLevelEnum;
import site.persipa.common.entity.enums.PersipaExceptionDef;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum TemplateExceptionEnum implements PersipaExceptionDef {

    TEMPLATE_CODE_HAS_BEAN_USED(10300016, "模板代码已被使用", ExceptionLevelEnum.EXCEPTION),

    TEMPLATE_NOT_EXIST(10300026, "模板不存在", ExceptionLevelEnum.EXCEPTION),

    TEMPLATE_ENTITY_NOT_INDEPENDENT(10300033, "反射实例模板树结构有误", ExceptionLevelEnum.WARNING),

    TEMPLATE_ENTITY_NOT_EXIST(10300046, "模板实例不存在", ExceptionLevelEnum.EXCEPTION),

    TEMPLATE_ENTITY_LABEL_DUPLICATE(10300056, "反射实例模版标签重复", ExceptionLevelEnum.EXCEPTION),

    ;

    private final int code;

    private final String msg;

    private final ExceptionLevelEnum level;

}
