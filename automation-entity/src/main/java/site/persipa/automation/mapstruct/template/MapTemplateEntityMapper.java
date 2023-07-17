package site.persipa.automation.mapstruct.template;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.automation.pojo.template.TemplateEntity;
import site.persipa.automation.pojo.template.dto.TemplateEntityDto;
import site.persipa.automation.pojo.template.vo.TemplateEntityBriefVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapTemplateEntityMapper {

    @Mappings({
            @Mapping(target = "parentId", ignore = true),
    })
    TemplateEntity fromDto(TemplateEntityDto dto);

    TemplateEntityBriefVo toBriefVo(TemplateEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "entityValue", source = "defaultValue"),
            @Mapping(target = "subEntities", ignore = true)
    })
    ReflectEntityDto toReflectEntityDto(TemplateEntity entity);


}