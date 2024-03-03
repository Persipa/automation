package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessConfigStateEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一个流程配置，每个实体代表一个流程
 *
 * @author persipa
 */
@Data
@TableName(value = "process_config", keepGlobalPrefix = true)
public class ProcessConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 8880156624614033474L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置代码
     */
    private String configCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 配置状态
     */
    private ProcessConfigStateEnum configState;

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

    @TableLogic
    private Boolean deleted;
}
