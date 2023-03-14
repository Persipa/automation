package site.persipa.btbtt.spider.mapper.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import site.persipa.btbtt.pojo.btbtt.BtbttSpiderConfig;
import site.persipa.btbtt.pojo.btbtt.dto.SpiderConfigDto;

/**
 * @author persipa
 */
@Mapper
public interface MapSpiderConfigMapper {

    MapSpiderConfigMapper INSTANCE = Mappers.getMapper(MapSpiderConfigMapper.class);

    BtbttSpiderConfig dto2Pojo(SpiderConfigDto dto);
}
