package site.persipa.btbtt.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.dto.ProcessNodeDto;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessNodeMapper {

    @Mappings({
            @Mapping(target = "nodeStatus", ignore = true),
            @Mapping(target = "nodeType", ignore = true)
    })
    ProcessNode fromDto(ProcessNodeDto dto);
}
