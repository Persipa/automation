package site.persipa.automation.pojo.template;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 配置模板
 *
 * @author persipa
 */
@Data
@TableName("automation_template_config")
public class TemplateConfig {

    @TableId
    private String id;

    /**
     * 模板名
     */
    private String templateName;

    /**
     * 模板代码
     */
    private String templateCode;
}