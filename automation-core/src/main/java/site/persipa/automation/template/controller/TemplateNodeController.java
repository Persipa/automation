package site.persipa.automation.template.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.automation.pojo.template.dto.TemplateNodeDto;
import site.persipa.automation.template.manager.TemplateNodeManager;
import site.persipa.common.entity.pojo.rest.model.Result;


/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/template/node")
public class TemplateNodeController {

    private final TemplateNodeManager templateNodeManager;

    @PostMapping("/upsert")
    public Result<String> upsert(@RequestBody @Valid TemplateNodeDto nodeDto) {
        return Result.success(templateNodeManager.upsert(nodeDto));
    }

}