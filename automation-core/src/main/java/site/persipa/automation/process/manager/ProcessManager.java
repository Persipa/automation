package site.persipa.automation.process.manager;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.persipa.automation.constant.RabbitConstant;
import site.persipa.automation.constant.RedisConstant;
import site.persipa.automation.enums.process.ProcessConfigStateEnum;
import site.persipa.automation.enums.process.ProcessExecuteCompletionStatEnum;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.exception.ProcessExecuteException;
import site.persipa.automation.mapstruct.process.MapProcessExecutionMapper;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.ProcessExecution;
import site.persipa.automation.pojo.process.ProcessNode;
import site.persipa.automation.pojo.process.bo.ProcessExecuteBo;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.process.service.ProcessConfigService;
import site.persipa.automation.process.service.ProcessExecutionService;
import site.persipa.automation.process.service.ProcessExecutionTicketService;
import site.persipa.automation.process.service.ProcessNodeService;
import site.persipa.common.entity.exception.PersipaCustomException;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessManager {

    private final ProcessConfigService processConfigService;
    private final ProcessNodeService processNodeService;
    private final ProcessNodeManager processNodeManager;
    private final ProcessResultManager processResultManager;
    private final ProcessExecutionService processExecutionService;
    private final ProcessExecutionTicketService processExecutionTicketService;

    private final StringRedisTemplate redisTemplate;

    private final MapProcessExecutionMapper mapProcessExecutionMapper;

    /**
     * 监听mq 队列 执行配置
     *
     * @param executeBo 执行相关配置
     */
    @RabbitListener(queuesToDeclare = {@Queue(RabbitConstant.AUTOMATION_PROCESS_EXECUTE_QUEUE)})
    public void execute(@Payload ProcessExecuteBo executeBo) {
        ProcessConfig processConfig = processConfigService.getById(executeBo.getConfigId());
        ProcessExecution processExecution = mapProcessExecutionMapper.fromExecuteBo(executeBo);
        String ticketId = executeBo.getTicket();
        if (processConfig == null) {
            processExecution.setCompletionStat(ProcessExecuteCompletionStatEnum.FAIL_NOT_EXIST);
            processExecutionService.save(processExecution);
            processExecutionTicketService.bind(ticketId, processExecution.getId(), null);
            return;
        }
        String configId = processConfig.getId();
        Boolean putSuccess = redisTemplate.opsForHash()
                .putIfAbsent(RedisConstant.PROCESS_EXECUTE_EXECUTING_HASH_KEY, configId, ticketId);
        Object o = null;
        String relationTicketId = null;
        if (Boolean.TRUE.equals(putSuccess)) {
            List<ProcessNode> nodeList = processNodeService.listByConfigId(configId, true);
            processExecution.setStartTime(LocalDateTime.now());
            try {
                for (ProcessNode processNode : nodeList) {
                    o = processNodeManager.execute(processNode, o);
                }
                processExecution.setCompleteTime(LocalDateTime.now());
                processExecution.setCompletionStat(ProcessExecuteCompletionStatEnum.SUCCESS);
            } catch (ProcessExecuteException e) {
                processExecution.setCompletionStat(ProcessExecuteCompletionStatEnum.FAIL);
                processExecution.setExecutionRemark(e.detail());
            } catch (PersipaCustomException e) {
                processExecution.setCompletionStat(ProcessExecuteCompletionStatEnum.FAIL);
                processExecution.setExecutionRemark(e.getMsg());
            } catch (Exception e) {
                processExecution.setCompletionStat(ProcessExecuteCompletionStatEnum.FAIL);
                processExecution.setExecutionRemark(e.getMessage());
            }
        } else {
            // 已有相同配置在执行 则跳过
            processExecution.setCompletionStat(ProcessExecuteCompletionStatEnum.SKIPPED);
            relationTicketId = redisTemplate.<String, String>opsForHash()
                    .get(RedisConstant.PROCESS_EXECUTE_EXECUTING_HASH_KEY, configId);
            if (!StringUtils.hasLength(relationTicketId)) {
                relationTicketId = redisTemplate.opsForValue().get(RedisConstant.PROCESS_EXECUTE_COMPLETE_CONFIG_KEY_PREFIX + configId);
            }
        }
        processExecutionService.save(processExecution);
        String executionId = processExecution.getId();
        processExecutionTicketService.bind(ticketId, executionId, relationTicketId);

        // 保存执行结果
        if (ProcessExecuteCompletionStatEnum.SUCCESS.equals(processExecution.getCompletionStat()) && o != null) {
            processResultManager.saveResult(executionId, o);
        }
        // 释放redis 锁
        redisTemplate.opsForValue()
                .set(RedisConstant.PROCESS_EXECUTE_COMPLETE_CONFIG_KEY_PREFIX + configId, ticketId, 5L, TimeUnit.SECONDS);
        redisTemplate.opsForHash().delete(RedisConstant.PROCESS_EXECUTE_EXECUTING_HASH_KEY, configId);
    }

    /**
     * 执行配置
     *
     * @param processConfig 需要执行的配置
     * @param processType   执行的类型
     * @return 执行结果
     */
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public ProcessResultBo execute(ProcessConfig processConfig, ProcessTypeEnum processType) {
        ProcessResultBo result = new ProcessResultBo();

        String configId = processConfig.getId();
        result.setConfigId(configId);
        result.setConfigName(processConfig.getConfigName());
        result.setProcessType(processType);

        // 验证是否可以执行
        ProcessConfigStateEnum configStatus = processConfig.getConfigState();
        if (configStatus == null || !configStatus.isExecutable()) {
            result.setProcessStatus(ProcessStatusEnum.REFUSE);
            return result;
        }

        // 抢占
        Boolean holdSuccess = redisTemplate.opsForValue().setIfAbsent(RedisConstant.KEY_PREFIX + configId, processType.getValue());
        if (Boolean.FALSE.equals(holdSuccess)) {
            result.setProcessStatus(ProcessStatusEnum.SKIP);
            return result;
        }

        result.setExecuteTime(LocalDateTime.now());

        // 获取所有节点
        List<ProcessNode> nodeList = processNodeService.listByConfigId(configId, true);
        Object o = null;

        // 正式执行
        result.setProcessId(IdUtil.fastSimpleUUID());
        for (ProcessNode processNode : nodeList) {
            try {
                o = processNodeManager.execute(processNode, o);
            } catch (PersipaCustomException e) {
                processConfig.setConfigState(ProcessConfigStateEnum.PROCESSING_ERROR);
                processConfigService.updateById(processConfig);
                String msg = e.getMsg();
                msg = !StringUtils.hasLength(e.getDescription()) ? msg : msg + e.getDescription();

                result.setProcessStatus(ProcessStatusEnum.FAIL);
                result.setMessage(msg);
                break;
            } catch (Exception e) {
                result.setProcessStatus(ProcessStatusEnum.FAIL);
                result.setMessage(e.getMessage());
                break;
            }
        }
        result.setProcessStatus(ProcessStatusEnum.SUCCESS);
        result.setResult(o);
        result.setCompleteTime(LocalDateTime.now());
        // 计算结果数量
        if (o != null) {
            int resultCount;
            if (o instanceof Iterable) {
                resultCount = IterUtil.size((Iterable<?>) o);
            } else if (o.getClass().isArray()) {
                resultCount = Array.getLength(o);
            } else {
                resultCount = 1;
            }
            result.setResultCount(resultCount);
        }

        // 解锁
        String value = redisTemplate.opsForValue().get(RedisConstant.KEY_PREFIX + configId);
        if (processType.getValue().equals(value)) {
            redisTemplate.delete(RedisConstant.KEY_PREFIX + configId);
        }

        // 保存结果
        processResultManager.saveResult(result);

        return result;
    }

}
