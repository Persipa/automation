package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("process_log")
public class ProcessLog {

    private String id;

    /**
     * 配置文件名
     */
    private String configName;

    /**
     * 配置文件备注
     */
    private String configRemark;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 参数
     */
    private String params;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
