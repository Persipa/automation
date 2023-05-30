package site.persipa.automation.enums.exception;

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

    CLASS_TYPE_NOT_MATCH_EXCEPTION(30000, "类型匹配失败", ExceptionLevelEnum.EXCEPTION),

    ;

    private final int code;

    private final String msg;

    private final ExceptionLevelEnum level;

}
