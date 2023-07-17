package site.persipa.automation.pojo.template.vo;

import lombok.Data;

/**
 * 实例模板基础信息 vo
 *
 * @author persipa
 */
@Data
public class TemplateEntityBriefVo {

    /**
     * 实例模板id
     */
    private String id;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 备注
     */
    private String remark;
}