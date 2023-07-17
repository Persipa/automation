package site.persipa.automation.pojo.template.vo;

import lombok.Data;
import site.persipa.automation.enums.process.NodeEntityGainTypeEnum;

/**
 * 模板节点的参数 vo
 *
 * @author persipa
 */
@Data
public class TemplateNodeEntityVo {

    /**
     * 模板实例的id
     */
    private String id;

    /**
     * 当前实例获取的方式
     */
    private NodeEntityGainTypeEnum gainType;

    /**
     * 当前实例在方法的参数中的位置
     */
    private Integer argOrder;

    /**
     * 默认值
     */
    private String defaultValue;

}