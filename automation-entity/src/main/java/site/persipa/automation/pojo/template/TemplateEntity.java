package site.persipa.automation.pojo.template;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 实体模板
 *
 * @author persipa
 */
@Data
@TableName("automation_template_entity")
public class TemplateEntity {

    @TableId
    private String id;

    /**
     * 实例的类的id
     */
    private String classId;

    /**
     * 构造实例所用构造方法id
     */
    private String constructorId;

    /**
     * 标识符
     */
    private String label;

    /**
     * 实例的值
     */
    private String defaultValue;

    /**
     * 父实例的id
     */
    private String parentId;

    /**
     * 该实例再另一实例中的位置
     */
    private Integer sort;

    /**
     * 备注的值
     */
    private String remark;
}