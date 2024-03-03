package site.persipa.automation.pojo.reflect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author persipa
 */
@Data
public class ReflectClassDto {

    @NotBlank(message = "包名不可为空")
    private String packageName;

    @NotBlank(message = "类名不可为空")
    private String className;
}
