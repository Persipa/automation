package site.persipa.automation.enums.process;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author persipa
 */
@Getter
@AllArgsConstructor
public enum ProcessExecuteCompletionStatEnum {

    /**
     * 成功
     */
    SUCCESS,

    /**
     * 失败
     */
    FAIL,

    /**
     * 失败-配置不存在
     */
    FAIL_NOT_EXIST,

    /**
     * 跳过执行
     */
    SKIPPED,

    /**
     * 拒绝执行
     */
    REFUSE,

    /**
     * 未知状态
     */
    UNKNOWN,

}
