package site.persipa.btbtt.pojo.jsoup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName("jsoup_method")
public class JsoupMethod {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String classId;

    private String className;

    private String methodName;

    private Boolean staticMethod;

    private Integer argCount;

    private Boolean variableArgs;

    private String returnType;

    private Boolean returnCollection;

}
