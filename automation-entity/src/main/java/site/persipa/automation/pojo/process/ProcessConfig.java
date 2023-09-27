package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessConfigStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一个流程配置，每个实体代表一个流程
 *
 * @author persipa
 */
@Data
@TableName("process_config")
public class ProcessConfig implements Serializable {

    private static final long serialVersionUID = 8880156624614033474L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 执行状态
     */
    private ProcessConfigStatusEnum processStatus;

    /**
     * 上次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastProcessTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
}
