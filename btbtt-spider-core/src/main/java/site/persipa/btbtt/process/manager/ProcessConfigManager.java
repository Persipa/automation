package site.persipa.btbtt.process.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.exception.ProcessExceptionEnum;
import site.persipa.btbtt.enums.process.ProcessConfigStatusEnum;
import site.persipa.btbtt.mapstruct.process.MapProcessConfigMapper;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.bo.ProcessResultBo;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigPageDto;
import site.persipa.btbtt.pojo.process.dto.SpiderConfigDto;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.cloud.exception.PersipaRuntimeException;
import site.persipa.cloud.pojo.page.dto.PageDto;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessConfigManager {

    private final ProcessConfigService processConfigService;

    private final ProcessManager processManager;

    private final MapProcessConfigMapper mapProcessConfigMapper;

    public boolean addConfig(SpiderConfigDto spiderConfigDto) {
        String resourceName = spiderConfigDto.getResourceName();
        long count = processConfigService.count(Wrappers.lambdaQuery(ProcessConfig.class)
                .eq(ProcessConfig::getResourceName, resourceName));
        Assert.isTrue(count == 0, () -> new PersipaRuntimeException(ProcessExceptionEnum.CONFIG_NAME_DUPLICATE));

        ProcessConfig spiderConfig = mapProcessConfigMapper.dto2Pojo(spiderConfigDto);
        spiderConfig.setProcessStatus(ProcessConfigStatusEnum.INIT);
        return processConfigService.save(spiderConfig);
    }

    public Object execute(String configId, boolean preview) {
        ProcessResultBo processResultBo = processManager.execute(configId);
        if (preview && processResultBo.isSuccess()) {
            Object processResult = processResultBo.getResult();
            boolean canSerialize = true;
            if (processResult instanceof Iterable) {
                for (Object o : (Iterable<?>) processResult) {
                    if (!(o instanceof Serializable)) {
                        canSerialize = false;
                        break;
                    }
                }
            } else if (processResult != null && processResult.getClass().isArray()) {
                int length = Array.getLength(processResult);
                for (int i = 0; i < length; i++) {
                    if (!(Array.get(processResult, i) instanceof Serializable)) {
                        canSerialize = false;
                        break;
                    }
                }
            }
            if (canSerialize) {
                return processResultBo.getResult();
            } else {
                return processResultBo.getResult().toString();
            }
        }
        return null;
    }

    public Page<ProcessConfig> page(PageDto<ProcessConfigPageDto> pageDto) {
        ProcessConfigPageDto params = pageDto.getPayload();
        ProcessConfigStatusEnum processStatus = ProcessConfigStatusEnum.VALUE_HELPER.find(params.getProcessStatus(), null);
        Page<ProcessConfig> page = new Page<>(pageDto.getCurrent(), pageDto.getSize(), true);

        return processConfigService.page(page, Wrappers.lambdaQuery(ProcessConfig.class)
                .like(StrUtil.isNotBlank(params.getResourceName()), ProcessConfig::getResourceName, params.getResourceName())
                .eq(processStatus != null, ProcessConfig::getProcessStatus, processStatus));
    }

}
