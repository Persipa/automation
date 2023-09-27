package site.persipa.automation.dubbo.provider.process.impl;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import site.persipa.automation.dubbo.provider.process.ProcessConfigApi;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.process.manager.ProcessManager;
import site.persipa.automation.process.service.ProcessConfigService;

/**
 * @author persipa
 */
@DubboService(version = "1.1")
@RequiredArgsConstructor
public class ProcessConfigApiImpl implements ProcessConfigApi {

    private final ProcessManager processManager;

    private final ProcessConfigService processConfigService;

    @Override
    public String execute(String processConfigId) {
        ProcessConfig processConfig = processConfigService.getById(processConfigId);
        ProcessResultBo resultBo = processManager.execute(processConfig, ProcessTypeEnum.REMOTE_CALL);
        return resultBo.getProcessId();
    }
}
