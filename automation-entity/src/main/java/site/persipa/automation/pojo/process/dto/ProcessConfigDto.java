package site.persipa.automation.pojo.process.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author persipa
 */
@Data
public class ProcessConfigDto {

    private String id;

    @NotBlank(message = "配置名称不可为空")
    private String configName;

    private String remark;

}
