package site.persipa.btbtt.pojo.jsoup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author persipa
 */
@Data
@TableName("jsoup_entity_relation")
public class JsoupEntityRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String entityId;

    private String constructorId;

    private String subEntityId;

    private Integer sort;
}
