package site.persipa.automation.pojo.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 模板生成实际执行的dto
 *
 * @author persipa
 */
@Data
public class TemplateConfigGenDto implements Serializable {

    private static final long serialVersionUID = -4156503896118183365L;
    /**
     * 模板id
     */
    @NotBlank
    private String id;

    /**
     * 配置的名称
     */
    private String processConfigName;

    /**
     * 要生成的实例
     */
    private List<TemplateEntityGenDto> genEntities;
}