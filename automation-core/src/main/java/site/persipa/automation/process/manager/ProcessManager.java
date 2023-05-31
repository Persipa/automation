package site.persipa.automation.process.manager;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import site.persipa.automation.constant.RedisConstant;
import site.persipa.automation.enums.process.ProcessConfigStatusEnum;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.ProcessNode;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.process.service.ProcessConfigService;
import site.persipa.automation.process.service.ProcessLogService;
import site.persipa.automation.process.service.ProcessNodeService;
import site.persipa.cloud.exception.PersipaCustomException;

import java.lang.reflect.Array;
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

    private final ProcessLogService processLogService;

    private final ProcessResultManager processResultManager;

    private final StringRedisTemplate redisTemplate;

    public ProcessResultBo execute(ProcessConfig processConfig, ProcessTypeEnum processType) {
        ProcessResultBo result = new ProcessResultBo();
        String configId = processConfig.getId();
        if (configId == null) {
            result.setProcessStatus(ProcessStatusEnum.REFUSE);
            return result;
        }
        result.setConfigId(configId);

        // 存日志
        String logId = processLogService.saveLog(configId, processType);
        result.setLogId(logId);

        // 验证是否可以执行
        ProcessConfigStatusEnum configStatus = processConfig.getProcessStatus();
        if (configStatus == null || !configStatus.isExecutable()) {
            processLogService.completeLog(logId, ProcessStatusEnum.REFUSE);
            result.setProcessStatus(ProcessStatusEnum.REFUSE);
            return result;
        }

        // 获取所有节点
        List<ProcessNode> nodeList = processNodeService.listByConfigId(configId, true);
        Object o = null;

        // 抢占
        Boolean holdSuccess = redisTemplate.opsForValue().setIfAbsent(RedisConstant.KEY_PREFIX + configId, processType.getValue());
        if (Boolean.FALSE.equals(holdSuccess)) {
            processLogService.completeLog(logId, ProcessStatusEnum.SKIP);
            result.setProcessStatus(ProcessStatusEnum.SKIP);
            return result;
        }
        // 正式执行
        for (ProcessNode processNode : nodeList) {
            try {
                o = processNodeManager.execute(processNode, o);
            } catch (PersipaCustomException e) {
                processConfig.setProcessStatus(ProcessConfigStatusEnum.PROCESSING_ERROR);
                processConfigService.updateById(processConfig);
                String msg = e.getMsg();
                msg = StrUtil.isEmpty(e.getDescription()) ? msg : msg + e.getDescription();

                result.setProcessStatus(ProcessStatusEnum.FAIL);
                result.setMessage(msg);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                result.setProcessStatus(ProcessStatusEnum.FAIL);
                result.setMessage(e.getMessage());
                break;
            }
        }
        result.setProcessStatus(ProcessStatusEnum.SUCCESS);
        result.setResult(o);
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
        if (processType.isSaveResult()) {
            Integer saveCount = processResultManager.saveResult(result);
            result.setSaveCount(saveCount);
        }

        // 记录日志
        processLogService.completeLog(logId, result.getProcessStatus());

        return result;
    }

}
