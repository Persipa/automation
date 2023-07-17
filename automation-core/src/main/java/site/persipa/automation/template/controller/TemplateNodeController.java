package site.persipa.automation.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.automation.pojo.template.dto.TemplateNodeDto;
import site.persipa.automation.template.manager.TemplateNodeManager;
import site.persipa.cloud.pojo.rest.model.Result;

import javax.validation.Valid;

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