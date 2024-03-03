package site.persipa.automation.process.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.persipa.automation.pojo.process.dto.ProcessResultDto;
import site.persipa.automation.pojo.process.vo.ProcessExecutionResultVo;
import site.persipa.automation.pojo.process.vo.ProcessResultCombineVo;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;
import site.persipa.automation.process.manager.ProcessResultManager;
import site.persipa.common.entity.pojo.rest.model.Result;

import java.util.List;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/process/result")
public class ProcessResultController {

    private final ProcessResultManager processResultManager;

    @PostMapping("/list")
    public Result<List<ProcessResultVo>> list(@RequestBody ProcessResultDto dto) {
        return Result.success(processResultManager.list(dto));
    }

    @PostMapping("/listCombine")
    public Result<ProcessResultCombineVo> listCombine(@RequestBody ProcessResultDto dto) {
        return Result.success(processResultManager.listCombine(dto));
    }

    @GetMapping("/ticket/{ticket}")
    public Result<ProcessExecutionResultVo> getByTicket(@PathVariable("ticket") String ticket) {
        return Result.success(processResultManager.getResultByTicket(ticket));
    }

    @PostMapping("/read/{configId}")
    public Result<Boolean> read(@PathVariable("configId") String configId) {
        return Result.success(processResultManager.read(configId));
    }

    @PostMapping("/read")
    public Result<Boolean> read(@RequestBody List<String> resultIds) {
        return Result.success(processResultManager.read(resultIds));
    }
}
