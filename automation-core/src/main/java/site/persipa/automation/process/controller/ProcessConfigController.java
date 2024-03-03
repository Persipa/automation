package site.persipa.automation.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.persipa.automation.pojo.process.ProcessConfig;
import site.persipa.automation.pojo.process.dto.ProcessConfigCloneDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigPageDto;
import site.persipa.automation.pojo.process.dto.ProcessConfigUpdateDto;
import site.persipa.automation.pojo.process.vo.ProcessConfigDetailVo;
import site.persipa.automation.pojo.process.vo.ProcessResultPreviewVo;
import site.persipa.automation.process.manager.ProcessConfigManager;
import site.persipa.common.entity.pojo.page.dto.PageDto;
import site.persipa.common.entity.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/process/config")
public class ProcessConfigController {

    private final ProcessConfigManager processConfigManager;

    @PostMapping("/add")
    public Result<String> add(@Valid @RequestBody ProcessConfigDto processConfigDto) {
        return Result.success(processConfigManager.add(processConfigDto));
    }

    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody ProcessConfigUpdateDto dto) {
        return Result.success(processConfigManager.update(dto));
    }

    @PostMapping("/clone")
    public Result<String> clone(@Valid @RequestBody ProcessConfigCloneDto cloneDto) {
        return Result.success(processConfigManager.cloneConfig(cloneDto));
    }

    @PostMapping("/page")
    public Result<Page<ProcessConfig>> page(@RequestBody PageDto<ProcessConfigPageDto> pageDto) {
        return Result.success(processConfigManager.page(pageDto));
    }

    @GetMapping("/detail/{configId}")
    public Result<ProcessConfigDetailVo> detail(@PathVariable("configId") String configId){
        return Result.success(processConfigManager.detail(configId));
    }

    @PostMapping("/preview/{configId}")
    public Result<ProcessResultPreviewVo> preview(@PathVariable("configId")String configId) {
        return Result.success(processConfigManager.previewResult(configId));
    }

    @PostMapping("/execute/{configId}")
    public Result<String> execute(@PathVariable("configId") String configId) {
        return Result.success(processConfigManager.executeManually(configId));
    }

    @PostMapping("/remove/{configId}")
    public Result<Boolean> remove(@PathVariable("configId") String configId) {
        return Result.success(processConfigManager.remove(configId));
    }

}
