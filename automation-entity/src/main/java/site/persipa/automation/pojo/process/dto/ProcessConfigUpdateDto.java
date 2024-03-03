package site.persipa.automation.pojo.process.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author persipa
 */
@Data
public class ProcessConfigUpdateDto {

    @NotBlank(message = "配置id 不可为空")
    private String id;

    private String configName;

    private String remark;

}
