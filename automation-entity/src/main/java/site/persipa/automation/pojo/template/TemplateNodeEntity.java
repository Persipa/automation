package site.persipa.automation.pojo.template;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.automation.enums.process.NodeEntityGainTypeEnum;

/**
 * 方法实际执行过程中需要用到的实例
 *
 * @author persipa
 */
@Data
@TableName("automation_template_node_entity")
public class TemplateNodeEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 模板节点id
     */
    private String nodeId;

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