package site.persipa.btbtt.reflect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.persipa.btbtt.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.btbtt.pojo.reflect.vo.ReflectEntityPreviewVo;
import site.persipa.btbtt.reflect.manager.ReflectEntityManager;
import site.persipa.cloud.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reflect/entity")
public class EntityController {

    private final ReflectEntityManager reflectEntityManager;

    @PostMapping("/add")
    public Result<Boolean> addEntity(@RequestBody ReflectEntityDto dto,
                                     @RequestParam(required = false, defaultValue = "false") boolean force) {
        return Result.success(reflectEntityManager.addEntity(dto, force));
    }

    @GetMapping("/preview/{entityId}")
    public Result<ReflectEntityPreviewVo> previewEntity(@PathVariable("entityId") String entityId) {
        return Result.success(reflectEntityManager.preview(entityId));
    }
}
