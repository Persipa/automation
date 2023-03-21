package site.persipa.btbtt.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.btbtt.pojo.btbtt.dto.SpiderConfigDto;
import site.persipa.btbtt.spider.manager.SpiderConfigManager;
import site.persipa.cloud.pojo.model.Result;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/btbttSpider/config")
public class SpiderConfigController {

    @Autowired
    private SpiderConfigManager spiderConfigManager;

    @PostMapping
    public Result<Boolean> addConfig(SpiderConfigDto spiderConfigDto) {
        return Result.success(spiderConfigManager.addConfig(spiderConfigDto));
    }

}
