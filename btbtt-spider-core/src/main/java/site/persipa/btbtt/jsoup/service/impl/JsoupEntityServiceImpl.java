package site.persipa.btbtt.jsoup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.jsoup.mapper.JsoupEntityMapper;
import site.persipa.btbtt.jsoup.service.JsoupEntityRelationService;
import site.persipa.btbtt.jsoup.service.JsoupEntityService;
import site.persipa.btbtt.pojo.jsoup.JsoupEntity;
import site.persipa.btbtt.pojo.jsoup.JsoupEntityRelation;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Service
public class JsoupEntityServiceImpl extends ServiceImpl<JsoupEntityMapper, JsoupEntity> implements JsoupEntityService {

    @Autowired
    private JsoupEntityRelationService jsoupEntityRelationService;

    @Override
    public List<JsoupEntity> subEntityList(String entityId) {
        JsoupEntity jsoupEntity = this.getById(entityId);
        List<JsoupEntityRelation> relationList = jsoupEntityRelationService.list(Wrappers.lambdaQuery(JsoupEntityRelation.class)
                .eq(JsoupEntityRelation::getConstructorId, jsoupEntity.getConstructorId())
                .eq(JsoupEntityRelation::getEntityId, entityId));
        Map<String, Integer> subEntityIdMap = relationList.stream()
                .collect(Collectors.toMap(JsoupEntityRelation::getSubEntityId, JsoupEntityRelation::getSort, (k1, k2) -> k1));
        List<JsoupEntity> subEntityList = this.listByIds(subEntityIdMap.keySet());
        subEntityList.sort(Comparator.comparing(entity -> subEntityIdMap.get(entity.getId())));
        return subEntityList;
    }
}
