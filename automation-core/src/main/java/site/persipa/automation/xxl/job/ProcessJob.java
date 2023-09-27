package site.persipa.automation.xxl.job;

import cn.hutool.core.text.StrFormatter;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.automation.dubbo.consumer.BarkClientConsumer;
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

    private final BarkClientConsumer barkClientConsumer;

    /**
     * @deprecated 计划不再使用定时任务直接执行配置，使用定时任务操作业务系统，间接执行配置
     */
    @Deprecated
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

        boolean notify = true;
        if (ProcessStatusEnum.SUCCESS.equals(resultBo.getProcessStatus())) {
            XxlJobHelper.log(notification);
            XxlJobHelper.log("执行结果：{}", resultBo.getResult().toString());
            if (resultBo.getSaveCount() == 0) {
                notify = false;
            }
        } else {
            XxlJobHelper.handleFail(resultBo.getMessage());
        }

        if (notify) {
            barkClientConsumer.sendMessage(null, notification);
        }
    }
}
