package site.persipa.automation.pojo.process;

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
@TableName(value = "process_node_entity", keepGlobalPrefix = true)
public class ProcessNodeEntity{

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 当前实例获取的方式
     */
    private NodeEntityGainTypeEnum gainType;

    /**
     * 当前实例id，当{@link #gainType} 为{@link NodeEntityGainTypeEnum#INPUT} 时无效
     */
    private String entityId;

    /**
     * 当前实例在方法的参数中的位置
     */
    private Integer argOrder;

}
