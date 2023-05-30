package site.persipa.automation.pojo.reflect;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.automation.enums.reflect.ReflectClassType;

/**
 * 反射的类
 *
 * @author persipa
 */
@Data
@TableName("reflect_class")
public class ReflectClass {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 类名
     */
    private String className;

    /**
     * 类分类
     */
    private ReflectClassType classType;

    /**
     * 父类id
     */
    private String parentClassId;

    /**
     * 是否可遍历
     */
    private Boolean iterable;

    private Boolean deleted;

}
