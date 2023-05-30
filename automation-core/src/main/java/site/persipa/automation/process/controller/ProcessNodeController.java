package site.persipa.automation.process.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.persipa.automation.pojo.process.dto.ProcessNodeDto;
import site.persipa.automation.pojo.process.vo.ProcessNodeVo;
import site.persipa.automation.process.manager.ProcessNodeManager;
import site.persipa.cloud.pojo.rest.model.Result;

import java.util.List;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/process/node")
public class ProcessNodeController {

    @Autowired
    private ProcessNodeManager processNodeManager;

    @PostMapping("/add")
    public Result<String> addNode(@RequestBody @Validated ProcessNodeDto nodeDto) {
        return Result.success(processNodeManager.add(nodeDto));
    }

    @PostMapping("/remove/{nodeId}")
    public Result<Boolean> removeNode(@PathVariable("nodeId") String nodeId) {
        return Result.success(processNodeManager.remove(nodeId));
    }

    @GetMapping("/listByConfigId/{configId}")
    public Result<List<ProcessNodeVo>> listByConfigId(@PathVariable("configId") String configId) {
        return Result.success(processNodeManager.listByConfigId(configId));
    }

    @PostMapping("/verify/node/{nodeId}")
    public Result<Boolean> verify(@PathVariable("nodeId") String nodeId) {
        return Result.success(processNodeManager.verify(nodeId));
    }

    @PostMapping("/verify/config/{configId}")
    public Result<Integer> verifyBatch(@PathVariable("configId") String configId) {
        return Result.success(processNodeManager.verifyBatch(configId));
    }
}
