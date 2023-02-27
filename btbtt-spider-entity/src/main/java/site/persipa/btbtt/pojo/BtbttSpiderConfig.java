package site.persipa.btbtt.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("btbtt_spider_config")
public class BtbttSpiderConfig {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String resourceName;

    private String resourcePostUri;

    private Boolean enabled;

    private Integer processStatus;

    private LocalDateTime lastProcessTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
}
