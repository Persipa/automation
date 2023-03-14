package site.persipa.btbtt.pojo.jsoup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.btbtt.enums.JsoupClassType;

/**
 * @author persipa
 */
@Data
@TableName("jsoup_class")
public class JsoupClass {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String packageName;

    private String className;

    private JsoupClassType classType;

    private String parentClassId;

    private Boolean iterable;

    private Boolean deleted;

}
