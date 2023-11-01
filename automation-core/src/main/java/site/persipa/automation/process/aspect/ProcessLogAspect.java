package site.persipa.automation.process.aspect;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.ProcessLog;
import site.persipa.automation.process.service.ProcessLogService;

/**
 * @author persipa
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ProcessLogAspect {

    private final ProcessLogService processLogService;

    @Pointcut("execution(* site.persipa.automation.process.manager.ProcessManager.execute(..))")
    public void configProcess() {
    }

    @Before("configProcess()")
    public void beforeProcessExecute(JoinPoint joinPoint) {
        ProcessConfig processConfig = null;

        for (Object arg : joinPoint.getArgs()) {
            Assert.notNull(arg, () -> new IllegalArgumentException("参数缺失"));
            if (arg instanceof ProcessConfig) {
                processConfig = (ProcessConfig) arg;
            }
        }
        ProcessLog processLog = new ProcessLog();
        if (processConfig != null) {
            processLog.setConfigName(processConfig.getConfigName());
            processLog.setConfigRemark(processConfig.getRemark());
            processLog.setOperationType("execute");
            // todo params
            processLog.setParams(null);
        }

        processLogService.save(processLog);
    }

}
