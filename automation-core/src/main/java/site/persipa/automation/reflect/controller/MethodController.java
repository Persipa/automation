package site.persipa.automation.reflect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.persipa.automation.pojo.reflect.dto.ReflectMethodDto;
import site.persipa.automation.reflect.manager.ReflectMethodManager;
import site.persipa.common.entity.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reflect/method")
public class MethodController {

    private final ReflectMethodManager reflectMethodManager;

    @PostMapping("/add")
    public Result<String> add(ReflectMethodDto dto) {
        return Result.success(reflectMethodManager.add(dto));
    }
}
