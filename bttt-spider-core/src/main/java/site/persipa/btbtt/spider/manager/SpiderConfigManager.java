package site.persipa.btbtt.spider.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.pojo.BtbttSpiderConfig;
import site.persipa.btbtt.pojo.dto.SpiderConfigDto;
import site.persipa.btbtt.spider.mapper.mapstruct.MapSpiderConfigMapper;
import site.persipa.btbtt.spider.service.BtbttSpiderConfigService;

/**
 * @author persipa
 */
@Component
public class SpiderConfigManager {

    @Autowired
    private BtbttSpiderConfigService spiderConfigService;

    public void addConfig(SpiderConfigDto spiderConfigDto) {
        BtbttSpiderConfig spiderConfig = MapSpiderConfigMapper.INSTANCE.dto2Pojo(spiderConfigDto);
        spiderConfigService.save(spiderConfig);
    }
}
