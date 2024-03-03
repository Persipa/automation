package site.persipa.automation.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.process.ProcessExecution;
import site.persipa.automation.pojo.process.bo.ProcessExecuteBo;
import site.persipa.automation.pojo.process.vo.ProcessExecutionResultVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessExecutionMapper {


    @Mappings({
            @Mapping(target = "completeTime", ignore = true),
            @Mapping(target = "completionStat", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "executionRemark", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "startTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    ProcessExecution fromExecuteBo(ProcessExecuteBo bo);

    @Mappings({
            @Mapping(target = "configName", ignore = true),
            @Mapping(target = "results", ignore = true)
    })
    ProcessExecutionResultVo toResultVo(ProcessExecution entity);
}
