package site.persipa.btbtt.pojo.process;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.btbtt.enums.ProcessingTypeEnum;

/**
 * 流程执行的节点，每个实例包含一个执行的动作，可以执行一个方法
 *
 * @author persipa
 */
@Data
@TableName("process_node")
public class ProcessNode {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 流程id
     */
    private String configId;

    /**
     * 当前节点执行的类型
     */
    private ProcessingTypeEnum processingType;

    /**
     * 当前节点在整个流程中所处的位置
     */
    private Integer sort;

    /**
     * 当前节点执行后的结果类型，备用
     */
    private String resultType;

    /**
     * 当前节点执行的方法
     */
    private String methodId;

    //    private ProcessingReturnTypeEnum returnType;
    //
    //    private String viewMethodId;
    //
    //    private Boolean iteratorView;

}
