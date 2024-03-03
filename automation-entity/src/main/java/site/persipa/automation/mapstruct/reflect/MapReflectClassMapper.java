package site.persipa.automation.mapstruct.reflect;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.automation.pojo.reflect.ReflectClass;
import site.persipa.automation.pojo.reflect.vo.ReflectClassVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapReflectClassMapper {

    @Mappings({
            @Mapping(target = "optimizeJoinOfCountSql", ignore = true),
            @Mapping(target = "records", source = "records"),
            @Mapping(target = "countId", ignore = true),
            @Mapping(target = "maxLimit", ignore = true),
            @Mapping(target = "optimizeCountSql", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "searchCount", ignore = true)
    })
    Page<ReflectClassVo> pojoPage2VoPage(Page<ReflectClass> jsoupClassPage);
}
