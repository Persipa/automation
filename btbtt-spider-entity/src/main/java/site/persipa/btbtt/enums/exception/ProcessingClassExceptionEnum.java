package site.persipa.btbtt.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.persipa.cloud.enums.ExceptionLevelEnum;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessingClassExceptionEnum {

    CLASS_EXIST(60000, "类已存在", ExceptionLevelEnum.WARNING),

    ;


    private final Integer code;

    private final String msg;

    private final ExceptionLevelEnum level;

}

