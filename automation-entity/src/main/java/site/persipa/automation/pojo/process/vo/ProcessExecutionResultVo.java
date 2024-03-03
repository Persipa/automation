package site.persipa.automation.pojo.process.vo;

import lombok.Data;
import site.persipa.automation.enums.process.ProcessExecuteCompletionStatEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessExecutionResult;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author persipa
 */
@Data
public class ProcessExecutionResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -3541214638346026159L;
    /**
     * 配置文件名
     */
    private String configName;

    /**
     * 操作类型
     */
    private ProcessTypeEnum processType;

    /**
     * 执行结果
     */
    private ProcessExecuteCompletionStatEnum completionStat;

    /**
     * 执行备注
     */
    private String executionRemark;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 执行的结果
     */
    private List<ProcessExecutionResult> results;
}
