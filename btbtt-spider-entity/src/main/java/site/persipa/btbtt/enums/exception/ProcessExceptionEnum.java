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
public enum ProcessExceptionEnum implements PersipaExceptionDef {

    CONFIG_NOT_EXIST(770050101, "配置不存在", ExceptionLevelEnum.ERROR),

    CONFIG_NON_EXECUTABLE(770050102, "配置不可执行", ExceptionLevelEnum.EXCEPTION),

    CONFIG_NAME_DUPLICATE(770050103, "配置名称重复", ExceptionLevelEnum.EXCEPTION),

    CONFIG_CLONE_FAILED(770050104, "配置复制失败", ExceptionLevelEnum.EXCEPTION),

    ;

    private final int code;

    private final String msg;

    private final ExceptionLevelEnum level;

}
