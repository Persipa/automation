package site.persipa.automation.mapstruct.template;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import site.persipa.automation.pojo.template.TemplateConfig;
import site.persipa.automation.pojo.template.dto.TemplateConfigDto;
import site.persipa.automation.pojo.template.vo.TemplateConfigDetailVo;
import site.persipa.automation.pojo.template.vo.TemplateConfigEntityVo;
import site.persipa.automation.pojo.template.vo.TemplateNodeVo;

import java.util.List;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapTemplateConfigMapper {

    @Mapping(target = "id", ignore = true)
    TemplateConfig fromDto(TemplateConfigDto dto);

    TemplateConfigDetailVo toDetailVo(TemplateConfig templateConfig, List<TemplateNodeVo> nodes);

    @Mapping(target = "entities", ignore = true)
    TemplateConfigEntityVo toEntityVo(TemplateConfig templateConfig);
}