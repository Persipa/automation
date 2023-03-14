package site.persipa.btbtt.pojo.jsoup;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName("jsoup_entity")
public class JsoupEntity {

    private String id;

    private String classId;

    private String constructorId;

    private String entityValue;

}
