package site.persipa.btbtt.process.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.btbtt.pojo.process.dto.ProcessNodeDto;
import site.persipa.btbtt.process.manager.ProcessNodeManager;
import site.persipa.cloud.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/btbttSpider/processing")
public class ProcessNodeController {

    @Autowired
    private ProcessNodeManager processNodeManager;

    @PostMapping
    public Result<String> addNode(@RequestBody @Validated ProcessNodeDto nodeDto) {
        return Result.success(processNodeManager.add(nodeDto));
    }
}
