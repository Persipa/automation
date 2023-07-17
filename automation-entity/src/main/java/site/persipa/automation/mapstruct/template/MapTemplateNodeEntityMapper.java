package site.persipa.automation.mapstruct.template;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.enums.process.NodeEntityGainTypeEnum;
import site.persipa.automation.pojo.process.dto.ProcessNodeEntityDto;
import site.persipa.automation.pojo.template.TemplateEntity;
import site.persipa.automation.pojo.template.TemplateNodeEntity;
import site.persipa.automation.pojo.template.dto.TemplateNodeEntityDto;
import site.persipa.automation.pojo.template.vo.TemplateNodeEntityVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapTemplateNodeEntityMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    TemplateNodeEntity fromDto(TemplateNodeEntityDto dto, String nodeId);

    @Mapping(target = "id", source = "nodeEntity.id")
    TemplateNodeEntityVo toVo(TemplateNodeEntity nodeEntity, TemplateEntity templateEntity);

    @Mappings({
            @Mapping(target = "entityId", source = "entityId"),
    })
    ProcessNodeEntityDto toProcessDto(TemplateNodeEntity nodeEntity, String entityId);

    default String fromGainTypeEnum(NodeEntityGainTypeEnum gainTypeEnum) {
        return gainTypeEnum.getValue();
    }
}