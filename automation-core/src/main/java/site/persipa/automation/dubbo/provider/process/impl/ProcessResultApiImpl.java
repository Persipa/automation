package site.persipa.automation.dubbo.provider.process.impl;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import site.persipa.automation.dubbo.provider.process.ProcessResultApi;
import site.persipa.automation.pojo.process.dto.ProcessResultDto;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;
import site.persipa.automation.process.manager.ProcessResultManager;

import java.util.List;

/**
 * @author persipa
 */
@DubboService(version = "1.0")
@RequiredArgsConstructor
public class ProcessResultApiImpl implements ProcessResultApi {

    private final ProcessResultManager processResultManager;

    @Override
    public List<ProcessResultVo> list(ProcessResultDto processResultDto) {
        return processResultManager.list(processResultDto);
    }

    @Override
    public List<ProcessResultVo> listResultItem(String processId) {
        return processResultManager.listByProcessId(processId);
    }

}
