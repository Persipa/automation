package site.persipa.automation.xxl.job;

import cn.hutool.core.text.StrFormatter;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.automation.dubbo.consumer.BarkClientManager;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.process.manager.ProcessManager;
import site.persipa.automation.process.service.ProcessConfigService;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessJob {

    private final ProcessManager processManager;

    private final ProcessConfigService processConfigService;

    private final BarkClientManager barkClientManager;

    @XxlJob("BtbttSpiderHandler")
    public void execute() {
        String configId = XxlJobHelper.getJobParam();
        ProcessConfig processConfig = processConfigService.getById(configId);
        if (processConfig == null) {
            XxlJobHelper.handleFail(StrFormatter.format("任务不存在:{}", configId));
            return;
        }
        ProcessResultBo resultBo = processManager.execute(processConfig, ProcessTypeEnum.AUTO);
        String notification = processManager.parseResultBo(resultBo);

        if (ProcessStatusEnum.SUCCESS.equals(resultBo.getProcessStatus())) {
            XxlJobHelper.log(notification);
            XxlJobHelper.log("执行结果：{}", resultBo.getResult().toString());
        } else {
            XxlJobHelper.handleFail(resultBo.getMessage());
        }

        barkClientManager.sendMessage(null, notification);
    }
}
