package site.persipa.btbtt.pojo.reflect;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 反射的方法
 *
 * @author persipa
 */
@Data
@TableName("reflect_method")
public class ReflectMethod {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属类的id
     */
    private String classId;

    /**
     * 所属类的全类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 是否是静态方法
     */
    private Boolean staticMethod;

    /**
     * 所需参数数量
     */
    private Integer argCount;

    /**
     * 是否泛型，备用
     */
    private Boolean variableArgs;

    /**
     * 返回类型
     */
    private String returnType;

    /**
     * 是否返回可遍历结果
     */
    private Boolean returnCollection;

}
