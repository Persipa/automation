package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessTypeEnum;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName(value = "process_execution_log", keepGlobalPrefix = true)
public class ProcessExecutionLog {

    private String id;

    /**
     * 配置id
     */
    private String configId;

    /**
     * 配置文件名
     */
    private String configName;

    /**
     * 操作类型
     */
    private ProcessTypeEnum processType;

    /**
     * 调用方
     */
    private String caller;

    /**
     * 调用时间
     */
    private LocalDateTime callTime;

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
    @TableLogic
    private Boolean deleted;
}
