package site.persipa.automation.pojo.reflect;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 反射的方法的参数的定义
 *
 * @author persipa
 */
@Data
@TableName("reflect_method_arg")
public class ReflectMethodArg {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 方法id
     */
    private String methodId;

    /**
     * 类id
     */
    private String classId;

    /**
     * 参数在方法中的位置
     */
    private Integer sort;

    /**
     * 备用
     */
    private Boolean variableArg;
}
