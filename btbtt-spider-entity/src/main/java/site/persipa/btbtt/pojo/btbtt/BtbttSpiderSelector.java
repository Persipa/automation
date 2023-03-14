package site.persipa.btbtt.pojo.btbtt;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("btbtt_spider_selector")
public class BtbttSpiderSelector {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String configId;

    private String cssSelector;

    private Integer resultType;

    private String attrKey;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;


}
