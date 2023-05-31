package site.persipa.automation.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import site.persipa.automation.pojo.process.ProcessResult;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessResultMapper {

    ProcessResultVo toVo(ProcessResult entity);

    ProcessResultPreviewVo resultBoToPreviewVo(ProcessResultBo resultBo);

}
