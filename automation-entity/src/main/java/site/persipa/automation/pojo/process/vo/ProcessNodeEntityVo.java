package site.persipa.automation.pojo.process.vo;

import lombok.Data;
import site.persipa.automation.enums.process.NodeEntityGainTypeEnum;

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
