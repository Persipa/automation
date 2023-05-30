package site.persipa.automation.reflect.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.persipa.automation.pojo.reflect.dto.ReflectClassDto;
import site.persipa.automation.pojo.reflect.dto.ReflectClassSearchDto;
import site.persipa.automation.pojo.reflect.vo.ReflectClassVo;
import site.persipa.automation.reflect.manager.ReflectClassManager;
import site.persipa.cloud.pojo.page.dto.PageDto;
import site.persipa.cloud.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/reflect/class")
public class ClassController {

    @Autowired
    private ReflectClassManager processingClassManager;

    @GetMapping("/exist")
    @CrossOrigin
    public Result<Boolean> existClass(@RequestParam String className) {
        return Result.success(processingClassManager.existClass(className));
    }

    @PostMapping("/page")
    @CrossOrigin
    public Result<Page<ReflectClassVo>> pageClasses(@Validated @RequestBody PageDto<ReflectClassSearchDto> searchDto) {
        return Result.success(processingClassManager.page(searchDto));
    }

    @PostMapping("/search")
    public Result<ReflectClassVo> searchClass(@Validated @RequestBody ReflectClassDto dto) {
        return Result.success(processingClassManager.search(dto));
    }

    @PostMapping("/add")
    public Result<Boolean> addClass(@Validated @RequestBody ReflectClassDto dto) {
        return Result.success(processingClassManager.add(dto));
    }
}
