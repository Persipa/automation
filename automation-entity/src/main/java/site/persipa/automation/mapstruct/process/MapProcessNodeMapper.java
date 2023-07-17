package site.persipa.automation.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.ProcessNode;
import site.persipa.automation.pojo.process.dto.ProcessNodeDto;
import site.persipa.automation.pojo.process.vo.ProcessNodeEntityVo;
import site.persipa.automation.pojo.process.vo.ProcessNodeVo;
import site.persipa.automation.pojo.reflect.ReflectMethod;

import java.util.List;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessNodeMapper {

    @Mappings({
            @Mapping(target = "nodeStatus", ignore = true),
    })
    ProcessNode fromDto(ProcessNodeDto dto);

    @Mappings({
            @Mapping(target = "id", source = "processNode.id"),
            @Mapping(target = "configId", source = "processNode.configId"),
            @Mapping(target = "configName", source = "processConfig.resourceName"),
            @Mapping(target = "methodClassName", source = "reflectMethod.className")
    })
    ProcessNodeVo toVo(ProcessConfig processConfig, ProcessNode processNode,
                       ReflectMethod reflectMethod, List<ProcessNodeEntityVo> entities);

}
