package site.persipa.automation.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.dto.ProcessConfigCloneDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigPageDto;
import site.persipa.automation.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.automation.process.manager.ProcessConfigManager;
import site.persipa.cloud.pojo.page.dto.PageDto;
import site.persipa.cloud.pojo.rest.model.Result;

import javax.validation.Valid;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/process/config")
public class ProcessConfigController {

    private final ProcessConfigManager processConfigManager;

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