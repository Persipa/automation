package site.persipa.btbtt.pojo.jsoup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName("jsoup_constructor_arg")
public class JsoupConstructorArg {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String className;

    private Integer sort;

    private Integer argType;

    private String argValue;
}
