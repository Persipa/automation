package site.persipa.automation.pojo.reflect;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.automation.enums.reflect.MethodResultTypeEnum;

/**
 * 反射的方法
 *
 * @author persipa
 */
@Data
@TableName(value = "reflect_method", keepGlobalPrefix = true)
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
     * 返回值的类
     */
    private String returnType;

    /**
     * 方法返回的类型
     */
    private MethodResultTypeEnum resultType;

    /**
     * 是否返回可遍历结果
     */
    private Boolean returnCollection;

}
