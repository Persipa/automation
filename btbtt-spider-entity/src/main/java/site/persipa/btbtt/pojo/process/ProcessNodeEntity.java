package site.persipa.btbtt.pojo.process;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.btbtt.enums.spider.NodeEntityGainTypeEnum;

/**
 * 方法实际执行过程中需要用到的实例
 *
 * @author persipa
 */
@Data
@TableName("process_node_entity")
public class ProcessNodeEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 节点id
     */
    private String processingId;

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
    private String argOrder;

}
