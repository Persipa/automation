package site.persipa.btbtt.process.manager;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.exception.ProcessExceptionEnum;
import site.persipa.btbtt.enums.process.ProcessConfigStatusEnum;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.bo.ProcessResultBo;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.btbtt.process.service.ProcessNodeService;
import site.persipa.cloud.exception.PersipaCustomException;
import site.persipa.cloud.exception.PersipaRuntimeException;

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


    public ProcessResultBo execute(String configId) {
        // 获取配置
        ProcessConfig config = processConfigService.getById(configId);
        Assert.notNull(config, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NOT_EXIST));

        ProcessConfigStatusEnum processStatus = config.getProcessStatus();
        Assert.isTrue(processStatus != null && processStatus.isExecutable(),
                () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NON_EXECUTABLE));

        // 获取所有节点
        List<ProcessNode> nodeList = processNodeService.list(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId)
                .orderByAsc(ProcessNode::getSort));
        Object o = null;
        for (ProcessNode processNode : nodeList) {
            try {
                o = processNodeManager.execute(processNode, o);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                config.setProcessStatus(ProcessConfigStatusEnum.PROCESSING_ERROR);
                processConfigService.updateById(config);
                return ProcessResultBo.fail();
            } catch (PersipaCustomException e) {
                e.getDescription();
                e.printStackTrace();
                config.setProcessStatus(ProcessConfigStatusEnum.PROCESSING_ERROR);
                processConfigService.updateById(config);
                return ProcessResultBo.fail();
            }
        }
        config.setLastProcessTime(LocalDateTime.now());
        config.setProcessStatus(ProcessConfigStatusEnum.VERIFY_PASS);
        processConfigService.updateById(config);
        return ProcessResultBo.success(o);
    }

}
