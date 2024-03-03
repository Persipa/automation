package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessExecuteCompletionStatEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName(value = "process_execution", keepGlobalPrefix = true)
public class ProcessExecution {

    @TableId
    private String id;

    /**
     * 配置id
     */
    private String configId;

    /**
     * 执行类型
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
     * 结果票据
     */
    private String ticket;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic(value = "0")
    private Boolean deleted;
}
