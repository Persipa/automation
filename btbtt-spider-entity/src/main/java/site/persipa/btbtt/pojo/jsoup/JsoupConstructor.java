package site.persipa.btbtt.pojo.jsoup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.btbtt.enums.JsoupConstructorType;

/**
 * @author persipa
 */
@Data
@TableName("jsoup_constructor")
public class JsoupConstructor {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String classId;

    private Integer argCount;

    private JsoupConstructorType constructType;

}
