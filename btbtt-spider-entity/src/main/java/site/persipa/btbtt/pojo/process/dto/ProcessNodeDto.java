package site.persipa.btbtt.pojo.process.dto;

import lombok.Data;
import site.persipa.btbtt.enums.process.ProcessNodeTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author persipa
 */
@Data
public class ProcessNodeDto {

    private String id;

    @NotBlank
    private String configId;

    @NotNull
    private ProcessNodeTypeEnum processingType;

    private Long sort;

    @NotBlank
    private String methodId;

    private List<ProcessNodeEntityDto> nodeEntities;
}
