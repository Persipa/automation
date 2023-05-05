package site.persipa.btbtt.pojo.reflect;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName("reflect_entity")
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

}
