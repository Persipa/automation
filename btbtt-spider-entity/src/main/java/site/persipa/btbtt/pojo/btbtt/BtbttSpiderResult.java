package site.persipa.btbtt.pojo.btbtt;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("btbtt_spider_result")
public class BtbttSpiderResult {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String configId;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;

}
