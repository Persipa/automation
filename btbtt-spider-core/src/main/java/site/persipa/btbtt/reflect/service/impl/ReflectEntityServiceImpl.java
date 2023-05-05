package site.persipa.btbtt.reflect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.reflect.mapper.ReflectEntityMapper;
import site.persipa.btbtt.reflect.service.ReflectEntityRelationService;
import site.persipa.btbtt.reflect.service.ReflectEntityService;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;
import site.persipa.btbtt.pojo.reflect.ReflectEntityRelation;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Service
public class ReflectEntityServiceImpl extends ServiceImpl<ReflectEntityMapper, ReflectEntity> implements ReflectEntityService {

    @Autowired
    private ReflectEntityRelationService jsoupEntityRelationService;

    @Override
    public List<ReflectEntity> subEntityList(String entityId) {
        ReflectEntity jsoupEntity = this.getById(entityId);
        List<ReflectEntityRelation> relationList = jsoupEntityRelationService.list(Wrappers.lambdaQuery(ReflectEntityRelation.class)
                .eq(ReflectEntityRelation::getConstructorId, jsoupEntity.getConstructorId())
                .eq(ReflectEntityRelation::getEntityId, entityId));
        Map<String, Integer> subEntityIdMap = relationList.stream()
                .collect(Collectors.toMap(ReflectEntityRelation::getSubEntityId, ReflectEntityRelation::getSort, (k1, k2) -> k1));
        List<ReflectEntity> subEntityList = this.listByIds(subEntityIdMap.keySet());
        subEntityList.sort(Comparator.comparing(entity -> subEntityIdMap.get(entity.getId())));
        return subEntityList;
    }
}
