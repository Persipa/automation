package site.persipa.automation.mapstruct.template;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.process.dto.ProcessNodeDto;
import site.persipa.automation.pojo.process.dto.ProcessNodeEntityDto;
import site.persipa.automation.pojo.template.TemplateNode;
import site.persipa.automation.pojo.template.dto.TemplateNodeDto;
import site.persipa.automation.pojo.template.vo.TemplateNodeEntityVo;
import site.persipa.automation.pojo.template.vo.TemplateNodeVo;

import java.util.List;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapTemplateNodeMapper {

    TemplateNode fromDto(TemplateNodeDto dto);

    @Mapping(target = "method", ignore = true)
    TemplateNodeVo toVo(TemplateNode templateNode, List<TemplateNodeEntityVo> entities);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "configId", source = "processConfigId"),
            @Mapping(target = "nodeEntities", source = "nodeEntities"),
    })
    ProcessNodeDto toProcessDto(String processConfigId, TemplateNode templateNode, List<ProcessNodeEntityDto> nodeEntities);
}