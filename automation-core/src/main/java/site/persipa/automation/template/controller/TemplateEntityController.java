package site.persipa.automation.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.automation.pojo.template.dto.TemplateEntityDto;
import site.persipa.automation.template.manager.TemplateEntityManager;
import site.persipa.common.entity.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/template/entity")
public class TemplateEntityController {

    private final TemplateEntityManager templateEntityManager;

    @PostMapping("/add")
    public Result<String> addEntity(@RequestBody TemplateEntityDto dto) {
        return Result.success(templateEntityManager.add(dto));
    }

}