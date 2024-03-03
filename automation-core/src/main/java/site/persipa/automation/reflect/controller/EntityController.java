package site.persipa.automation.reflect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.persipa.automation.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.automation.pojo.reflect.vo.ReflectEntityPreviewVo;
import site.persipa.automation.reflect.manager.ReflectEntityManager;
import site.persipa.common.entity.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reflect/entity")
public class EntityController {

    private final ReflectEntityManager reflectEntityManager;

    @PostMapping("/add")
    public Result<String> addEntity(@RequestBody ReflectEntityDto dto,
                                    @RequestParam(required = false, defaultValue = "false") boolean force) {
        return Result.success(reflectEntityManager.addEntity(dto, force));
    }

    @PostMapping("/remove/{entityId}")
    public Result<Boolean> removeEntity(@PathVariable("entityId") String entityId) {
        return Result.success(reflectEntityManager.remove(entityId));
    }

    @GetMapping("/preview/{entityId}")
    public Result<ReflectEntityPreviewVo> previewEntity(@PathVariable("entityId") String entityId) {
        return Result.success(reflectEntityManager.preview(entityId));
    }
}
