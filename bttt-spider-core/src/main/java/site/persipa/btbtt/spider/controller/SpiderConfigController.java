package site.persipa.btbtt.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.btbtt.pojo.dto.SpiderConfigDto;
import site.persipa.btbtt.spider.manager.SpiderConfigManager;
import site.persipa.cloud.pojo.model.Result;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/btbttSpider")
public class SpiderConfigController {

    @Autowired
    private SpiderConfigManager spiderConfigManager;

    @PostMapping("/config")
    public Result<Boolean> addConfig(SpiderConfigDto spiderConfigDto) {
        spiderConfigManager.addConfig(spiderConfigDto);
        return Result.success(true);
    }
}
