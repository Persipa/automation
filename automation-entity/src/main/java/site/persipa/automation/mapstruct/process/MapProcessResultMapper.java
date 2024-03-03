package site.persipa.automation.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.process.ProcessExecution;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessResultMapper {

    @Mappings({
            @Mapping(target = "completionStat", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "executionRemark", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "startTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ticket", ignore = true)
    })
    ProcessExecution fromResultBo(ProcessResultBo resultBo);
}
