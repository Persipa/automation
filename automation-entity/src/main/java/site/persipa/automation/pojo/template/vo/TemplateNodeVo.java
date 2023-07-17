package site.persipa.automation.pojo.template.vo;

import lombok.Data;
import site.persipa.automation.enums.process.ProcessNodeTypeEnum;
import site.persipa.automation.pojo.reflect.vo.ReflectMethodBriefVo;

import java.util.List;

/**
 * 节点模板 vo
 *
 * @author persipa
 */
@Data
public class TemplateNodeVo {

    /**
     *  节点id
     */
    private String id;

    /**
     * 节点执行的类型
     */
    private ProcessNodeTypeEnum nodeType;

    /**
     * 当前节点在整个流程中所处的位置
     */
    private Long sort;

    /**
     * 当前节点执行的方法
     */
    private ReflectMethodBriefVo method;

    /**
     * 执行方法所需的参数
     */
    private List<TemplateNodeEntityVo> entities;

}