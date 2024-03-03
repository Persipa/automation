package site.persipa.automation.pojo.process.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessNodeTypeEnum;

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
    private ProcessNodeTypeEnum nodeType;

    private Long sort;

    @NotBlank
    private String methodId;

    private List<ProcessNodeEntityDto> nodeEntities;
}
