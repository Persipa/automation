package site.persipa.btbtt.pojo.process.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author persipa
 */
@Data
public class ProcessConfigDto {

    private String id;

    @NotBlank(message = "配置名称不可为空")
    private String resourceName;

    private String resourcePostUri;

}
