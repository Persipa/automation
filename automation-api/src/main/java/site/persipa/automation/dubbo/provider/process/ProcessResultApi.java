package site.persipa.automation.dubbo.provider.process;

import site.persipa.automation.pojo.process.dto.ProcessResultDto;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;

import java.util.List;

/**
 * @author persipa
 */
public interface ProcessResultApi {

    /**
     * 根据条件查询结果列表
     *
     * @param processResultDto 查询条件
     * @return 直接结果数组
     */
    List<ProcessResultVo> list(ProcessResultDto processResultDto);

    /**
     * 获取执行结果
     *
     * @param processId 执行id
     * @return 执行的结果
     */
    List<ProcessResultVo> listResultItem(String processId);
}
