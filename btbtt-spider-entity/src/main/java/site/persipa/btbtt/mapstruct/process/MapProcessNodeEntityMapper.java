package site.persipa.btbtt.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.btbtt.enums.process.NodeEntityGainTypeEnum;
import site.persipa.btbtt.pojo.process.ProcessNodeEntity;
import site.persipa.btbtt.pojo.process.dto.ProcessNodeEntityDto;
import site.persipa.btbtt.pojo.process.vo.ProcessNodeEntityVo;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessNodeEntityMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    ProcessNodeEntity fromDto(ProcessNodeEntityDto dto, String nodeId);

    default NodeEntityGainTypeEnum fromGainTypeValue(String gainTypeStr) {
        return NodeEntityGainTypeEnum.VALUE_HELPER.find(gainTypeStr, null);
    }

    ProcessNodeEntityVo toVo(ProcessNodeEntity nodeEntity, ReflectEntity reflectEntity);
}
