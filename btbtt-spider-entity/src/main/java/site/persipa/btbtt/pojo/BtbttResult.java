package site.persipa.btbtt.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("btbtt_result")
public class BtbttResult {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String configId;

    private String result;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;

}
