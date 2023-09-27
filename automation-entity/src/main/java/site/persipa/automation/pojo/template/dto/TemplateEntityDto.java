package site.persipa.automation.pojo.template.dto;

import lombok.Data;

import java.util.List;

/**
 * @author persipa
 */
@Data
public class TemplateEntityDto {

    private String id;

    private String classId;

    private String constructorId;

    private String label;

    private Integer sort;

    private String defaultValue;

    private String remark;

    private List<TemplateEntityDto> subEntities;

}