package site.persipa.btbtt.pojo.process;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import site.persipa.btbtt.enums.spider.ProcessConfigStatusEnum;

import java.time.LocalDateTime;

/**
 * 一个流程配置，每个实体代表一个流程
 *
 * @author persipa
 */
@Data
@TableName("process_config")
public class ProcessConfig {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源获取uri
     */
    private String resourcePostUri;

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
    private LocalDateTime lastProcessTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
}
