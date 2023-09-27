package site.persipa.automation.process.manager;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.persipa.automation.common.properties.ProcessNotificationProperties;
import site.persipa.automation.constant.RedisConstant;
import site.persipa.automation.enums.exception.ProcessExceptionEnum;
import site.persipa.automation.enums.process.ProcessConfigStatusEnum;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.ProcessNode;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.process.service.ProcessConfigService;
import site.persipa.automation.process.service.ProcessNodeService;
import site.persipa.automation.process.util.TemplateUtil;
import site.persipa.cloud.exception.PersipaCustomException;
import site.persipa.cloud.exception.PersipaRuntimeException;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.List;

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

    private final ProcessNotificationProperties processNotificationProperties;

    private final StringRedisTemplate redisTemplate;

    /**
     * 准备执行配置
     *
     * @param configId    准备执行配置的id
     * @param processType 执行的类型
     * @return 用户获取结果的id
     */
    public String prepareExecute(String configId, ProcessTypeEnum processType) {
        ProcessConfig processConfig = processConfigService.getById(configId);
        Assert.notNull(processConfig, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));
        if (processConfig.getProcessStatus() == null || !processConfig.getProcessStatus().isExecutable()) {
            throw new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NON_EXECUTABLE);
        }

        // 抢占
        String processIdKey = RedisConstant.PROCESS_EXECUTE_PROCESS_ID_KEY_PREFIX + configId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(processIdKey))) {
            return redisTemplate.opsForValue().get(processIdKey);
        }
        String processId = IdUtil.fastSimpleUUID();
        Boolean holdSuccess = redisTemplate.opsForValue().setIfAbsent(RedisConstant.PROCESS_EXECUTE_PROCESS_ID_KEY_PREFIX + configId, processId);
        if (Boolean.FALSE.equals(holdSuccess)) {
            return redisTemplate.opsForValue().get(processIdKey);
        } else {
            // todo 将待执行待配置 添加到执行队列中

        }

        return processId;
    }

    /**
     * 执行配置
     *
     * @param processConfig 需要执行的配置
     * @param processType   执行的类型
     * @return 执行结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessResultBo execute(ProcessConfig processConfig, ProcessTypeEnum processType) {
        ProcessResultBo result = new ProcessResultBo();

        String configId = processConfig.getId();
        result.setConfigId(configId);
        result.setConfigName(processConfig.getConfigName());
        result.setProcessType(processType);

        // 验证是否可以执行
        ProcessConfigStatusEnum configStatus = processConfig.getProcessStatus();
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
                processConfig.setProcessStatus(ProcessConfigStatusEnum.PROCESSING_ERROR);
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

    /**
     * 将执行的结果转换为模板的描述结果
     *
     * @param resultBo 执行的结果
     * @return 用于描述的结果
     */
    public String parseResultBo(ProcessResultBo resultBo) {
        String template = ProcessStatusEnum.SUCCESS.equals(resultBo.getProcessStatus()) ?
                processNotificationProperties.getSuccess() : processNotificationProperties.getFail();
        return TemplateUtil.fill(template, resultBo);
    }

}
