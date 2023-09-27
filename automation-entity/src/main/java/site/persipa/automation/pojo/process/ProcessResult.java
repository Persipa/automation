package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("process_result")
public class ProcessResult {

    @TableId
    private String id;

    /**
     * 配置id
     */
    private String configId;

    /**
     * 执行id
     */
    private String processId;

    /**
     * 执行类型
     */
    private ProcessTypeEnum processType;

    /**
     * 执行结果
     */
    private ProcessStatusEnum processStatus;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

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
