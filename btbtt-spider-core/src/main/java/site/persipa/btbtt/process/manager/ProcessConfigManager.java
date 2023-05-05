package site.persipa.btbtt.process.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.spider.ProcessConfigStatusEnum;
import site.persipa.btbtt.mapper.process.MapProcessConfigMapper;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.dto.SpiderConfigDto;
import site.persipa.btbtt.process.service.ProcessConfigService;

/**
 * @author persipa
 */
@Component
public class ProcessConfigManager {

    @Autowired
    private ProcessConfigService spiderConfigService;

    @Autowired
    private MapProcessConfigMapper mapProcessConfigMapper;

    public boolean addConfig(SpiderConfigDto spiderConfigDto) {
        String resourceName = spiderConfigDto.getResourceName();
        long count = spiderConfigService.count(Wrappers.lambdaQuery(ProcessConfig.class)
                .eq(ProcessConfig::getResourceName, resourceName));
        if (count > 0) {
            // todo thr
            return false;
        }
        ProcessConfig spiderConfig = mapProcessConfigMapper.dto2Pojo(spiderConfigDto);
        spiderConfig.setProcessStatus(ProcessConfigStatusEnum.INIT);
        return spiderConfigService.save(spiderConfig);
    }
}
