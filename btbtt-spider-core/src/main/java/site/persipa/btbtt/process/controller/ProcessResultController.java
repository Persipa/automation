package site.persipa.btbtt.process.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.persipa.btbtt.pojo.process.dto.ProcessResultDto;
import site.persipa.btbtt.pojo.process.vo.ProcessResultCombineVo;
import site.persipa.btbtt.pojo.process.vo.ProcessResultVo;
import site.persipa.btbtt.process.manager.ProcessResultManager;
import site.persipa.cloud.pojo.rest.model.Result;

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

    @PostMapping("/read/{configId}")
    public Result<Boolean> read(@PathVariable("configId") String configId) {
        return Result.success(processResultManager.read(configId));
    }
}
