package site.persipa.btbtt.pojo.reflect.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author persipa
 */
@Data
public class ProcessingClassDto {

    @NotBlank(message = "包名不可为空")
    private String packageName;

    @NotBlank(message = "类名不可为空")
    private String className;
}
