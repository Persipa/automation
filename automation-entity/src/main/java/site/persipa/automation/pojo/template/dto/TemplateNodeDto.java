package site.persipa.automation.pojo.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessNodeTypeEnum;


import java.util.List;

/**
 * @author persipa
 */
@Data
public class TemplateNodeDto {

    /**
     * id（仅更新用）
     */
    private String id;

    /**
     * 模板id
     */
    @NotBlank
    private String configId;

    /**
     * 当前节点执行的类型
     */
    @NotNull
    private ProcessNodeTypeEnum nodeType;

    /**
     * 当前节点在整个流程中所处的位置
     */
    private Long sort;

    /**
     * 当前节点执行的方法
     */
    @NotBlank
    private String methodId;

    /**
     * 节点方法执行所需参数
     */
    private List<TemplateNodeEntityDto> nodeEntities;
}