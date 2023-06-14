package site.persipa.automation.pojo.process.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author persipa
 */
@Data
public class ProcessConfigUpdateDto {

    @NotBlank(message = "配置id 不可为空")
    private String id;

    private String resourceName;

    private String resourcePostUri;

}
