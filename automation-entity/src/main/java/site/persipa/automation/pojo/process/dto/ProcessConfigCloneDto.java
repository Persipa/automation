package site.persipa.automation.pojo.process.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;



/**
 * @author persipa
 */
@Data
public class ProcessConfigCloneDto {

    @NotBlank(message = "配置名称不可为空")
    private String configName;

    private String remark;

    @NotBlank(message = "源配置id 不可为空")
    private String sourceConfigId;
}
