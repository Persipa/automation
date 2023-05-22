package site.persipa.btbtt.mapstruct.reflect;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import site.persipa.btbtt.pojo.reflect.ReflectClass;
import site.persipa.btbtt.pojo.reflect.vo.ReflectClassVo;

/**
 * @author persipa
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapReflectClassMapper {

    @Mappings({
            @Mapping(target = "optimizeJoinOfCountSql", ignore = true),
            @Mapping(target = "records", source = "records")
    })
    Page<ReflectClassVo> pojoPage2VoPage(Page<ReflectClass> jsoupClassPage);
}
