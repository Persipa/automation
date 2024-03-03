package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName(value = "process_execution_ticket", keepGlobalPrefix = true)
public class ProcessExecutionTicket {

    /**
     * 票据id
     */
    private String id;

    /**
     * 引用id
     */
    private String relationId;

    /**
     * 是否使用
     */
    private Boolean used;

    /**
     * 执行id
     */
    private String executionId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
