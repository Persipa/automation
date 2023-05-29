package site.persipa.btbtt.xxl.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.process.ProcessTypeEnum;
import site.persipa.btbtt.pojo.process.bo.ProcessExecuteResultBo;
import site.persipa.btbtt.process.manager.ProcessManager;

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
