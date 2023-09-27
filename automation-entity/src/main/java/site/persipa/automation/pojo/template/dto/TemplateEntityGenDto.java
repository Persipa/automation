package site.persipa.automation.pojo.template.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 实例模板生成 dto
 *
 * @author persipa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateEntityGenDto implements Serializable {

    private static final long serialVersionUID = -2811665178367627146L;
    /**
     * 实例模板的id
     */
    private String id;

    /**
     * 具体的值
     */
    private String value;
}