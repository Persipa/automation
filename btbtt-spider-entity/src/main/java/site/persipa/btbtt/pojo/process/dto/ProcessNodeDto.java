package site.persipa.btbtt.pojo.process.dto;

import lombok.Data;
import site.persipa.btbtt.enums.ProcessingTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author persipa
 */
@Data
public class ProcessNodeDto {

    private String id;

    @NotBlank
    private String configId;

    @NotNull
    private ProcessingTypeEnum processingType;

    private Integer sort;

    private String resultType;

    @NotBlank
    private String methodId;
}
