package site.persipa.automation.pojo.template.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 实例模板基础信息 vo
 *
 * @author persipa
 */
@Data
public class TemplateEntityBriefVo implements Serializable {

    private static final long serialVersionUID = 362834276536679205L;
    /**
     * 实例模板id
     */
    private String id;

    /**
     * 标识符
     */
    private String label;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 备注
     */
    private String remark;
}