package site.persipa.automation.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.dto.ProcessConfigCloneDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigDto;
import site.persipa.automation.pojo.process.vo.ProcessConfigDetailVo;
import site.persipa.automation.pojo.process.vo.ProcessNodeVo;

import java.util.List;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessConfigMapper {


    @Mappings({
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "configCode", ignore = true),
            @Mapping(target = "configState", ignore = true)
    })
    ProcessConfig dto2Pojo(ProcessConfigDto dto);

    @Mapping(target = "id", ignore = true)
    ProcessConfigDto cloneDto2Dto(ProcessConfigCloneDto cloneDto);

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "lastProcessTime", ignore = true),
            @Mapping(target = "processStatus", ignore = true),
            @Mapping(target = "resourceName", ignore = true),
            @Mapping(target = "resourcePostUri", ignore = true)
    })
    ProcessConfigDetailVo toDetailVo(ProcessConfig entity, List<ProcessNodeVo> nodes);
}
