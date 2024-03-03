package site.persipa.automation.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.common.entity.pojo.rest.model.Result;

/**
 * 仅测试
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/ping")
    public Result<String> ping(){
        return Result.success("pong");
    }
}
