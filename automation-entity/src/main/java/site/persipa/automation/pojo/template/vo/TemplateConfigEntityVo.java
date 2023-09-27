package site.persipa.automation.pojo.template.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 模板实体vo
 *
 * @author persipa
 */
@Data
public class TemplateConfigEntityVo implements Serializable {

    private static final long serialVersionUID = 3776961568495064741L;
    /**
     * 模板id
     */
    private String id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板code
     */
    private String templateCode;

    /**
     * 需要填充的实例数组
     */
    private List<TemplateEntityBriefVo> entities;
}