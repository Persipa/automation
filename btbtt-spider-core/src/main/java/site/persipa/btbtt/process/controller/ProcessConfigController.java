package site.persipa.btbtt.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigCloneDto;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigDto;
import site.persipa.btbtt.pojo.process.dto.ProcessConfigPageDto;
import site.persipa.btbtt.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.btbtt.process.manager.ProcessConfigManager;
import site.persipa.cloud.pojo.page.dto.PageDto;
import site.persipa.cloud.pojo.rest.model.Result;

import javax.validation.Valid;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/process/config")
public class ProcessConfigController {

    @Autowired
    private ProcessConfigManager processConfigManager;

    @PostMapping("/add")
    public Result<String> addConfig(@Valid @RequestBody ProcessConfigDto processConfigDto) {
        return Result.success(processConfigManager.addConfig(processConfigDto));
    }

    @PostMapping("/clone")
    public Result<String> clone(@Valid @RequestBody ProcessConfigCloneDto cloneDto) {
        return Result.success(processConfigManager.cloneConfig(cloneDto));
    }

    @PostMapping("/page")
    public Result<Page<ProcessConfig>> page(@RequestBody PageDto<ProcessConfigPageDto> pageDto) {
        return Result.success(processConfigManager.page(pageDto));
    }

    @PostMapping("/preview/{configId}")
    public Result<ProcessResultPreviewVo> preview(@PathVariable("configId")String configId) {
        return Result.success(processConfigManager.previewResult(configId));
    }

    @PostMapping("/execute/{configId}")
    public Result<Boolean> execute(@PathVariable("configId") String configId) {
        return Result.success(processConfigManager.execute(configId));
    }

}
