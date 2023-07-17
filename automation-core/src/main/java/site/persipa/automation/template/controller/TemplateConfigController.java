package site.persipa.automation.template.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.persipa.automation.pojo.template.TemplateConfig;
import site.persipa.automation.pojo.template.dto.TemplateConfigDto;
import site.persipa.automation.pojo.template.dto.TemplateConfigGenDto;
import site.persipa.automation.pojo.template.dto.TemplateConfigPageDto;
import site.persipa.automation.pojo.template.vo.TemplateConfigDetailVo;
import site.persipa.automation.pojo.template.vo.TemplateConfigEntityVo;
import site.persipa.automation.template.manager.TemplateConfigManager;
import site.persipa.cloud.pojo.page.dto.PageDto;
import site.persipa.cloud.pojo.rest.model.Result;

import javax.validation.Valid;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/template/config")
public class TemplateConfigController {

    private final TemplateConfigManager templateConfigManager;

    @PostMapping("/add")
    public Result<String> add(@RequestBody TemplateConfigDto configDto) {
        return Result.success(templateConfigManager.add(configDto));
    }

    @PostMapping("/page")
    public Result<Page<TemplateConfig>> page(@RequestBody PageDto<TemplateConfigPageDto> pageDto) {
        return Result.success(templateConfigManager.page(pageDto));
    }

    @GetMapping("/detail/{configId}")
    public Result<TemplateConfigDetailVo> detail(@PathVariable("configId") String configId){
        return Result.success(templateConfigManager.detail(configId));
    }

    @GetMapping("/findEntity/{configId}")
    public Result<TemplateConfigEntityVo> listRequiredEntities(@PathVariable("configId") String configId) {
        return Result.success(templateConfigManager.findEntity(configId));
    }

    @PostMapping("/generate")
    public Result<String> generate(@RequestBody @Valid TemplateConfigGenDto configGenDto) {
        return Result.success(templateConfigManager.generate(configGenDto));
    }
}