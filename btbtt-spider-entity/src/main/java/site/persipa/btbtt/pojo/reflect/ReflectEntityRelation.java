package site.persipa.btbtt.pojo.reflect;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName("reflect_entity_relation")
public class ReflectEntityRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 实例id
     */
    private String entityId;

    /**
     * 构造方法id
     */
    private String constructorId;

    /**
     * 实例属性id
     */
    private String subEntityId;

    /**
     * 实例在构造方法中的位置
     */
    private Integer sort;
}
