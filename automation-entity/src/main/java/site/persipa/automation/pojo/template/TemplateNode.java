package site.persipa.automation.pojo.template;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessNodeTypeEnum;

/**
 * 节点模板
 *
 * @author persipa
 */
@Data
@TableName("automation_template_node")
public class TemplateNode {

    @TableId
    private String id;

    /**
     * 模板id
     */
    private String configId;

    /**
     * 当前节点执行的类型
     */
    private ProcessNodeTypeEnum nodeType;

    /**
     * 当前节点在整个流程中所处的位置
     */
    private Long sort;

    /**
     * 当前节点执行的方法
     */
    private String methodId;
}