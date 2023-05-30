package site.persipa.btbtt.reflect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.reflect.ReflectEntity;
import site.persipa.btbtt.reflect.mapper.ReflectEntityMapper;
import site.persipa.btbtt.reflect.service.ReflectEntityService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Service
public class ReflectEntityServiceImpl extends ServiceImpl<ReflectEntityMapper, ReflectEntity> implements ReflectEntityService {

    @Override
    public List<ReflectEntity> subEntityList(String entityId) {
       return this.list(Wrappers.lambdaQuery(ReflectEntity.class)
                .eq(ReflectEntity::getParentId, entityId));
    }

    @Override
    public List<ReflectEntity> allSubEntityList(String entityId) {
        List<ReflectEntity> result = new ArrayList<>();
        List<ReflectEntity> tempEntityList = this.subEntityList(entityId);
        if (!tempEntityList.isEmpty()) {
            List<ReflectEntity> list = tempEntityList.stream()
                    .map(ReflectEntity::getId)
                    .map(this::allSubEntityList)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            result.addAll(list);
        }
        result.addAll(tempEntityList);
        return result;
    }
}
