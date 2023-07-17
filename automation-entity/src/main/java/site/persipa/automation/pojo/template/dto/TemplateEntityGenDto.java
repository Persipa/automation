package site.persipa.automation.pojo.template.dto;

import lombok.Data;

/**
 * 实例模板生成 dto
 *
 * @author persipa
 */
@Data
public class TemplateEntityGenDto {

    /**
     * 实例模板的id
     */
    private String id;

    /**
     * 具体的值
     */
    private String value;
}