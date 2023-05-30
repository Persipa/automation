package site.persipa.btbtt.pojo.process.vo;

import lombok.Data;
import site.persipa.btbtt.enums.process.NodeEntityGainTypeEnum;

/**
 * @author persipa
 */
@Data
public class ProcessNodeEntityVo {

    private String entityId;

    private String previewValue;

    private NodeEntityGainTypeEnum gainType;

    private Integer argOrder;

}
