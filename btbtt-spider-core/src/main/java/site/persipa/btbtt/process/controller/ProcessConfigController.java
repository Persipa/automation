package site.persipa.btbtt.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigPageDto;
import site.persipa.btbtt.pojo.process.dto.SpiderConfigDto;
import site.persipa.btbtt.process.manager.ProcessConfigManager;
import site.persipa.cloud.pojo.page.dto.PageDto;
import site.persipa.cloud.pojo.rest.model.Result;

import javax.validation.Valid;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/btbttSpider/config")
public class ProcessConfigController {

    @Autowired
    private ProcessConfigManager processConfigManager;

    @PostMapping("/add")
    public Result<Boolean> addConfig(@Valid @RequestBody SpiderConfigDto spiderConfigDto) {
        return Result.success(processConfigManager.addConfig(spiderConfigDto));
    }

    @PostMapping("/page")
    public Result<Page<ProcessConfig>> page(@RequestBody PageDto<ProcessConfigPageDto> pageDto) {
        return Result.success(processConfigManager.page(pageDto));
    }


    @PostMapping("/execute/{configId}")
    public Result<Object> execute(@PathVariable("configId") String configId,
                                  @RequestParam(required = false, defaultValue = "false") Boolean preview) {
        return Result.success(processConfigManager.execute(configId, preview));
    }

}
