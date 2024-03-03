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
public enum ProcessExceptionEnum implements PersipaExceptionDef {
    // 214        748      364        7
    // 系统代码   模块代码   错误代码   错误级别
    // 001-200  000-999  0000-999   3|6|9
    CONFIG_NOT_EXIST(10200019, "配置不存在", ExceptionLevelEnum.ERROR),

    CONFIG_NON_EXECUTABLE(10200026, "配置不可执行", ExceptionLevelEnum.EXCEPTION),

    CONFIG_NAME_DUPLICATE(10200036, "配置名称重复", ExceptionLevelEnum.EXCEPTION),

    CONFIG_CLONE_FAILED(10200046, "配置复制失败", ExceptionLevelEnum.EXCEPTION),

    ;

    private final int code;

    private final String msg;

    private final ExceptionLevelEnum level;

}
