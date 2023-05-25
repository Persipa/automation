package site.persipa.btbtt.reflect.manager;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import site.persipa.btbtt.enums.exception.ReflectExceptionEnum;
import site.persipa.btbtt.enums.reflect.PackagingDataTypeEnum;
import site.persipa.btbtt.enums.reflect.ReflectClassType;
import site.persipa.btbtt.mapstruct.reflect.MapReflectClassMapper;
import site.persipa.btbtt.pojo.reflect.ReflectClass;
import site.persipa.btbtt.pojo.reflect.dto.ReflectClassDto;
import site.persipa.btbtt.pojo.reflect.dto.ReflectClassSearchDto;
import site.persipa.btbtt.pojo.reflect.vo.ReflectClassVo;
import site.persipa.btbtt.reflect.service.ReflectClassService;
import site.persipa.cloud.exception.PersipaRuntimeException;
import site.persipa.cloud.pojo.page.dto.PageDto;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ReflectClassManager {

    private final ReflectClassService classService;

    private final MapReflectClassMapper mapReflectClassMapper;

    public boolean existClass(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public Page<ReflectClassVo> page(PageDto<ReflectClassSearchDto> searchPageDto) {
        Page<ReflectClass> page = Page.of(searchPageDto.getCurrent(), searchPageDto.getSize(), true);
        ReflectClassSearchDto searchDto = searchPageDto.getPayload();
        String classTypeStr = searchDto.getClassType();
        ReflectClassType classType = ReflectClassType.parseByValue(classTypeStr);
        Wrapper<ReflectClass> queryWrapper = Wrappers.lambdaQuery(ReflectClass.class)
                .like(StringUtils.hasText(searchDto.getPackageName()), ReflectClass::getPackageName, searchDto.getPackageName())
                .like(StringUtils.hasText(searchDto.getClassName()), ReflectClass::getClassName, searchDto.getClassName())
                .eq(classType != null, ReflectClass::getClassType, classType)
                .orderBy(true, true, ReflectClass::getPackageName, ReflectClass::getClassName);
        Page<ReflectClass> classPage = classService.page(page, queryWrapper);

        return mapReflectClassMapper.pojoPage2VoPage(classPage);
    }

    public Boolean add(ReflectClassDto dto) {
        String classFullName = dto.getPackageName() + "." + dto.getClassName();
        Class<?> clazz;
        try {
            clazz = Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            throw new PersipaRuntimeException(ReflectExceptionEnum.CLASS_NOT_FOUND_EXCEPTION, classFullName);
        }
        ReflectClass existClass = classService.getOne(Wrappers.lambdaQuery(ReflectClass.class)
                .eq(ReflectClass::getPackageName, clazz.getPackageName())
                .eq(ReflectClass::getClassName, clazz.getSimpleName()), false);
        Assert.isNull(existClass, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_CLASS_EXIST, classFullName));
        ReflectClass reflectClass = new ReflectClass();
        reflectClass.setPackageName(clazz.getPackageName());
        reflectClass.setClassName(clazz.getSimpleName());
        reflectClass.setIterable(clazz.isArray() || Iterable.class.isAssignableFrom(clazz));
        if (PackagingDataTypeEnum.parseByClassName(clazz.getName()) != null) {
            reflectClass.setClassType(ReflectClassType.PACKAGING_DATA_TYPE);
        } else {
            reflectClass.setClassType(ReflectClassType.NORMAL_DATA_TYPE);
        }

        return classService.save(reflectClass);
    }

    public ReflectClassVo search(ReflectClassDto dto) {
        String classFullName = dto.getPackageName() + "." + dto.getClassName();
        Class<?> clazz;
        try {
            clazz = Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            throw new PersipaRuntimeException(ReflectExceptionEnum.CLASS_NOT_FOUND_EXCEPTION, classFullName);
        }
        ReflectClassVo vo = new ReflectClassVo();
        vo.setPackageName(clazz.getPackageName());
        vo.setClassName(clazz.getSimpleName());
        vo.setIterable(clazz.isArray() || Iterable.class.isAssignableFrom(clazz));
        if (PackagingDataTypeEnum.parseByClassName(clazz.getName()) != null) {
            vo.setClassType(ReflectClassType.PACKAGING_DATA_TYPE);
        } else {
            vo.setClassType(ReflectClassType.NORMAL_DATA_TYPE);
        }
        return vo;
    }
}
