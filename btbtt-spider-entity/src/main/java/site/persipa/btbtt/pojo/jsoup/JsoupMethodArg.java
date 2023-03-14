package site.persipa.btbtt.pojo.jsoup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName("jsoup_method_arg")
public class JsoupMethodArg {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String methodId;

    private String classId;

    private Integer sort;

    private Boolean variableArg;
}
