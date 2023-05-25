package site.persipa.btbtt.pojo.process.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author persipa
 */
@Data
public class ProcessConfigCloneDto {

    @NotBlank(message = "配置名称不可为空")
    private String resourceName;

    private String resourcePostUri;

    @NotBlank(message = "源配置id 不可为空")
    private String sourceConfigId;
}
