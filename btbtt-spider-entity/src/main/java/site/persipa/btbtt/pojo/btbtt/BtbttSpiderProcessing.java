package site.persipa.btbtt.pojo.btbtt;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import site.persipa.btbtt.enums.ProcessingTypeEnum;

/**
 * @author persipa
 */
@Data
public class BtbttSpiderProcessing {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String configId;

    private ProcessingTypeEnum processingType;

    private Integer sort;

    private String resultType;

    private String methodId;

}
