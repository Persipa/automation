package site.persipa.btbtt.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.btbtt.pojo.btbtt.dto.SpiderProcessingDto;
import site.persipa.btbtt.spider.manager.SpiderProcessingManager;
import site.persipa.cloud.pojo.model.Result;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/btbttSpider/processing")
public class SpiderProcessingController {

    @Autowired
    private SpiderProcessingManager spiderProcessingManager;

    @PostMapping
    public Result<String> addProcessing(@RequestBody @Validated SpiderProcessingDto processingDto) {
        return Result.success(spiderProcessingManager.add(processingDto));
    }
}
