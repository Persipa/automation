package site.persipa.automation.xxl.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.bo.ProcessExecuteResultBo;
import site.persipa.automation.process.manager.ProcessManager;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessJob {

    private final ProcessManager processManager;

    @XxlJob("BtbttSpiderHandler")
    public void execute() {
        String configId = XxlJobHelper.getJobParam();
        ProcessExecuteResultBo resultBo = processManager.execute(configId, ProcessTypeEnum.AUTO);
        if (resultBo.isExecuteSuccess()) {
            XxlJobHelper.log("任务（id：{}）执行成功，结果数量：{}", configId, resultBo.getResultCount());
            XxlJobHelper.log("执行结果：{}", resultBo.getResult().toString());
        } else {
            XxlJobHelper.handleFail(resultBo.getMessage());
        }
    }
}
