package site.persipa.automation.pojo.process.vo;

import lombok.Data;
import site.persipa.automation.enums.process.ProcessNodeStatusEnum;
import site.persipa.automation.enums.process.ProcessNodeTypeEnum;

import java.util.List;

/**
 * @author persipa
 */
@Data
public class ProcessNodeVo {

    private String id;

    private String configId;

    private String configName;

    /**
     * 当前节点的状态
     */
    private ProcessNodeStatusEnum nodeStatus;

    /**
     * 当前节点执行的类型
     */
    private ProcessNodeTypeEnum nodeType;

    /**
     * 当前节点在整个流程中所处的位置
     */
    private Long sort;

    private String methodId;

    private String methodClassName;

    private String methodName;

    private List<ProcessNodeEntityVo> entities;

}
