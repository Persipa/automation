package site.persipa.btbtt.pojo.btbtt.dto;

import lombok.Data;
import site.persipa.btbtt.enums.ProcessingTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author persipa
 */
@Data
public class SpiderProcessingDto {

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
