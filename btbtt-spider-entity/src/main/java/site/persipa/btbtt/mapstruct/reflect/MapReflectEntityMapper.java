package site.persipa.btbtt.mapstruct.reflect;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.btbtt.pojo.reflect.ReflectClass;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;
import site.persipa.btbtt.pojo.reflect.ReflectEntityConstructor;
import site.persipa.btbtt.pojo.reflect.dto.ReflectEntityDto;
import site.persipa.btbtt.pojo.reflect.vo.ReflectEntityPreviewVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapReflectEntityMapper {

    @Mapping(target = "parentId", ignore = true)
    ReflectEntity fromDto(ReflectEntityDto dto);

    @Mappings({
            @Mapping(target = "id", source = "reflectEntity.id"),
            @Mapping(target = "classId",source = "reflectEntity.classId"),
            @Mapping(target = "entityValue", ignore = true),
            @Mapping(target = "constructSuccess", ignore = true)
    })
    ReflectEntityPreviewVo toPreviewVo(ReflectEntity reflectEntity, ReflectClass reflectClass, ReflectEntityConstructor entityConstructor);


}
