package site.persipa.automation.mapstruct.process;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import site.persipa.automation.pojo.process.ProcessResultItem;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapProcessResultItemMapper {

    ProcessResultVo toVo(ProcessResultItem entity);

    ProcessResultPreviewVo resultBoToPreviewVo(ProcessResultBo resultBo);

}
