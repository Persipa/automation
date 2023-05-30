package site.persipa.automation.pojo.reflect;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.automation.enums.reflect.ReflectEntityConstructorType;

/**
 * 实例的构造器，多个实例可公用一个构造器
 *
 * @author persipa
 */
@Data
@TableName("reflect_entity_constructor")
public class ReflectEntityConstructor {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所构造的实例的类的id
     */
    private String classId;

    /**
     * 构造所需参数数量
     */
    private Integer argCount;

    /**
     * 构造方式
     */
    private ReflectEntityConstructorType constructType;

}
