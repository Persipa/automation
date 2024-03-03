package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName(value = "process_execution_result", keepGlobalPrefix = true)
public class ProcessExecutionResult implements Serializable {

    @Serial
    private static final long serialVersionUID = -1320024095599859480L;
    /**
     * 结果id
     */
    private String id;

    /**
     * 执行id
     */
    private String executionId;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
