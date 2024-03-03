package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName(value = "process_result_item", keepGlobalPrefix = true)
public class ProcessResultItem {

    @TableId
    private String id;

    private String configId;

    private String processId;

    private String result;

    private Boolean used;

    private Boolean isNew;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
