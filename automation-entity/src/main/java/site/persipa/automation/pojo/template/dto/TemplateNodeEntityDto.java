package site.persipa.automation.pojo.template.dto;

import lombok.Data;
import site.persipa.automation.enums.process.NodeEntityGainTypeEnum;

/**
 * @author persipa
 */
@Data
public class TemplateNodeEntityDto {

    /**
     * 当前实例获取的方式
     */
    private NodeEntityGainTypeEnum gainType;

    /**
     * 当前模板实例id，当{@link #gainType} 为{@link NodeEntityGainTypeEnum#INPUT} 时无效
     */
    private String entityId;

    /**
     * 当前实例在方法的参数中的位置
     */
    private Integer argOrder;

}