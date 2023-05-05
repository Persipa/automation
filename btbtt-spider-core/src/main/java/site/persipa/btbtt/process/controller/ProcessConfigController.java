package site.persipa.btbtt.process.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.btbtt.pojo.process.dto.SpiderConfigDto;
import site.persipa.btbtt.process.manager.ProcessConfigManager;
import site.persipa.cloud.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/btbttSpider/config")
public class ProcessConfigController {

    @Autowired
    private ProcessConfigManager spiderConfigManager;

    @PostMapping
    public Result<Boolean> addConfig(SpiderConfigDto spiderConfigDto) {
        return Result.success(spiderConfigManager.addConfig(spiderConfigDto));
    }

}
