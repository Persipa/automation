package site.persipa.automation.dubbo.provider.process.impl;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.StringUtils;
import site.persipa.automation.constant.RabbitConstant;
import site.persipa.automation.dubbo.provider.process.ProcessConfigApi;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.ProcessExecutionLog;
import site.persipa.automation.pojo.process.ProcessExecutionTicket;
import site.persipa.automation.pojo.process.bo.ProcessCallBo;
import site.persipa.automation.pojo.process.bo.ProcessExecuteBo;
import site.persipa.automation.process.manager.ProcessConfigManager;
import site.persipa.automation.process.manager.ProcessNodeManager;
import site.persipa.automation.process.service.ProcessConfigService;
import site.persipa.automation.process.service.ProcessExecutionLogService;
import site.persipa.automation.process.service.ProcessExecutionTicketService;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@DubboService(version = "2.0")
@RequiredArgsConstructor
public class ProcessConfigApiImpl implements ProcessConfigApi {

    private final ProcessConfigManager processConfigManager;
    private final ProcessConfigService processConfigService;
    private final ProcessNodeManager processNodeManager;
    private final ProcessExecutionLogService processExecutionLogService;
    private final ProcessExecutionTicketService processExecutionTicketService;

    private final AmqpTemplate amqpTemplate;

    @Override
    public String execute(ProcessCallBo callBo) {
        String configId = callBo.getConfigId();
        String caller = StringUtils.hasLength(callBo.getCaller()) ? callBo.getCaller() : "unknown caller";
        ProcessTypeEnum processType = ProcessTypeEnum.REMOTE_CALL;

        ProcessConfig processConfig = processConfigService.getById(configId);
        // 记录
        ProcessExecutionLog processLog = new ProcessExecutionLog();
        processLog.setConfigId(configId);
        processLog.setConfigName(processConfig.getConfigName());
        processLog.setProcessType(processType);
        processLog.setCaller(caller);
        processLog.setCallTime(LocalDateTime.now());
        processExecutionLogService.save(processLog);

        ProcessExecutionTicket ticket = processExecutionTicketService.generateTicket();
        // 发送mq 执行
        amqpTemplate.convertAndSend(RabbitConstant.AUTOMATION_PROCESS_EXECUTE_DIRECT_EXCHANGE, RabbitConstant.AUTOMATION_PROCESS_EXECUTE_ROUTING,
                new ProcessExecuteBo(configId, processType, ticket.getId()));

        return ticket.getId();
    }

    @Override
    public Boolean remove(String configId) {
        return processConfigManager.remove(configId);
    }

    @Override
    public Boolean verify(String processConfigId) {
        return processNodeManager.verifyBatch(processConfigId) >= 0;
    }
}
