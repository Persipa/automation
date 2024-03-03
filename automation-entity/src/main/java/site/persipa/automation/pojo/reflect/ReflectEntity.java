package site.persipa.automation.pojo.reflect;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName(value = "reflect_entity", keepGlobalPrefix = true)
public class ReflectEntity {

    private String id;

    /**
     * 实例的类的id
     */
    private String classId;

    /**
     * 构造实例所用构造方法id
     */
    private String constructorId;

    /**
     * 实例的值
     */
    private String entityValue;

    /**
     * 预览的值
     */
    private String previewValue;

    /**
     * 父实例的id
     */
    private String parentId;

    /**
     * 该实例再另一实例中的位置
     */
    private Integer sort;

}
