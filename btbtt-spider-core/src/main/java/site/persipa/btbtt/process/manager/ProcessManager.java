package site.persipa.btbtt.process.manager;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.constant.RedisConstant;
import site.persipa.btbtt.enums.exception.ProcessExceptionEnum;
import site.persipa.btbtt.enums.process.ProcessConfigStatusEnum;
import site.persipa.btbtt.enums.process.ProcessTypeEnum;
import site.persipa.btbtt.mapstruct.process.MapProcessResultMapper;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.bo.ProcessExecuteResultBo;
import site.persipa.btbtt.pojo.process.bo.ProcessResultBo;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.btbtt.process.service.ProcessLogService;
import site.persipa.btbtt.process.service.ProcessNodeService;
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

    private final ProcessLogService processLogService;

    private final StringRedisTemplate redisTemplate;

    private final MapProcessResultMapper mapProcessResultMapper;

    public ProcessExecuteResultBo execute(String configId, ProcessTypeEnum processType) {
        // 正在执行中的不可重复执行
        Boolean holdSuccess = redisTemplate.opsForValue().setIfAbsent(RedisConstant.KEY_PREFIX + configId, processType.getValue());
        if (Boolean.FALSE.equals(holdSuccess)) {
            ProcessExecuteResultBo resultBo = new ProcessExecuteResultBo();
            resultBo.setConfigId(configId);
            resultBo.setExecuteCompleted(false);
            return resultBo;
        }
        // 记录日志并执行
        String logId = processLogService.saveLog(configId, processType);
        ProcessResultBo processResultBo = this.execute(configId);
        processLogService.completeLog(logId, processResultBo.isSuccess());

        ProcessExecuteResultBo resultBo = mapProcessResultMapper.resultBoToExecuteResultBo(configId, processResultBo);
        // 如果成功 计算结果
        if (processResultBo.isSuccess() && processResultBo.getResult() != null) {
            Object o = processResultBo.getResult();
            int resultCount;
            if (o instanceof Iterable) {
                resultCount = IterUtil.size((Iterable<?>) o);
            } else if (o.getClass().isArray()) {
                resultCount = Array.getLength(o);
            } else {
                resultCount = 1;
            }
            resultBo.setResultCount(resultCount);
        }

        // 释放redis 锁
        redisTemplate.delete(RedisConstant.KEY_PREFIX + configId);
        return resultBo;
    }

    private ProcessResultBo execute(String configId) {
        // 获取配置
        ProcessConfig config = processConfigService.getById(configId);
        Assert.notNull(config, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));

        ProcessConfigStatusEnum processStatus = config.getProcessStatus();
        Assert.isTrue(processStatus != null && processStatus.isExecutable(),
                () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NON_EXECUTABLE));

        // 获取所有节点
        List<ProcessNode> nodeList = processNodeService.listByConfigId(configId, true);
        Object o = null;
        for (ProcessNode processNode : nodeList) {
            try {
                o = processNodeManager.execute(processNode, o);
            } catch (PersipaCustomException e) {
                config.setProcessStatus(ProcessConfigStatusEnum.PROCESSING_ERROR);
                processConfigService.updateById(config);
                String msg = e.getMsg();
                msg = StrUtil.isEmpty(e.getDescription()) ? msg : msg + e.getDescription();
                return ProcessResultBo.fail(msg);
            } catch (Exception e) {
                return ProcessResultBo.fail(e.getMessage());
            }
        }
        config.setLastProcessTime(LocalDateTime.now());
        config.setProcessStatus(ProcessConfigStatusEnum.VERIFY_PASS);
        processConfigService.updateById(config);
        return ProcessResultBo.success(o);
    }

}
