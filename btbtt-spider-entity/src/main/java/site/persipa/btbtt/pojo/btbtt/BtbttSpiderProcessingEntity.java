package site.persipa.btbtt.pojo.btbtt;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.btbtt.enums.spider.ProcessingEntityGainTypeEnum;

/**
 * @author persipa
 */
@Data
@TableName("btbtt_spider_processing_entity")
public class BtbttSpiderProcessingEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String processingId;

    private ProcessingEntityGainTypeEnum gainType;

    private String entityId;

    private String argOrder;

}
