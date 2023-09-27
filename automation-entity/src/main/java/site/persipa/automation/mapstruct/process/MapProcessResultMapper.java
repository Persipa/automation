package site.persipa.automation.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.process.ProcessResult;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessResultMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "deleted", ignore = true)
    })
    ProcessResult fromResultBo(ProcessResultBo resultBo);
}
