package site.persipa.btbtt.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.btbtt.pojo.process.ProcessResult;
import site.persipa.btbtt.pojo.process.bo.ProcessExecuteResultBo;
import site.persipa.btbtt.pojo.process.bo.ProcessResultBo;
import site.persipa.btbtt.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.btbtt.pojo.process.vo.ProcessResultVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessResultMapper {

    ProcessResultVo toVo(ProcessResult entity);

    @Mapping(target = "success", source = "executeSuccess")
    ProcessResultPreviewVo executeResultBoToPreviewVo(ProcessExecuteResultBo executeResultBo);

    @Mappings({
            @Mapping(target = "executeCompleted", constant = "true"),
            @Mapping(target = "executeSuccess", source = "resultBo.success"),
            @Mapping(target = "resultCount", ignore = true)
    })
    ProcessExecuteResultBo resultBoToExecuteResultBo(String configId, String logId, ProcessResultBo resultBo);
}
