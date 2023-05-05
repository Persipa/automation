package site.persipa.btbtt.reflect.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.persipa.btbtt.exception.reflect.ProcessingException;
import site.persipa.btbtt.exception.process.ProcessingClassException;
import site.persipa.btbtt.reflect.manager.ReflectClassManager;
import site.persipa.btbtt.pojo.reflect.dto.ProcessingClassDto;
import site.persipa.btbtt.pojo.reflect.dto.ProcessingClassSearchDto;
import site.persipa.btbtt.pojo.reflect.vo.ReflectClassVo;
import site.persipa.cloud.pojo.page.dto.PageDto;
import site.persipa.cloud.pojo.rest.model.Result;

/**
 * @author persipa
 */
@RestController
@RequestMapping("/processing/class")
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
    public Result<Page<ReflectClassVo>> pageClasses(@Validated @RequestBody PageDto<ProcessingClassSearchDto> searchDto) {
        return Result.success(processingClassManager.page(searchDto));
    }

    @PostMapping("/search")
    public Result<ReflectClassVo> searchClass(@Validated @RequestBody ProcessingClassDto dto) throws ProcessingException {
        return Result.success(processingClassManager.search(dto));
    }

    @PostMapping("/add")
    public Result<Boolean> addClass(@Validated @RequestBody ProcessingClassDto dto) throws ProcessingException, ProcessingClassException {
        return Result.success(processingClassManager.add(dto));
    }
}
