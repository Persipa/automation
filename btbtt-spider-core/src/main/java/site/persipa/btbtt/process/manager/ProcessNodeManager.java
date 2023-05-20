package site.persipa.btbtt.process.manager;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.ProcessingTypeEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.enums.spider.NodeEntityGainTypeEnum;
import site.persipa.btbtt.pojo.process.ProcessConfig;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.ProcessNodeEntity;
import site.persipa.btbtt.pojo.process.dto.ProcessNodeDto;
import site.persipa.btbtt.process.service.ProcessConfigService;
import site.persipa.btbtt.process.service.ProcessNodeEntityService;
import site.persipa.btbtt.process.service.ProcessNodeService;
import site.persipa.btbtt.reflect.manager.ReflectEntityManager;
import site.persipa.btbtt.reflect.manager.ReflectMethodManager;
import site.persipa.cloud.exception.PersipaCustomException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
public class ProcessNodeManager {

    @Autowired
    private ProcessConfigService processConfigService;
    @Autowired
    private ProcessNodeService processNodeService;
    @Autowired
    private ProcessNodeEntityService processNodeEntityService;
    @Autowired
    private ReflectEntityManager reflectEntityManager;
    @Autowired
    private ReflectMethodManager reflectMethodManager;


    public String add(ProcessNodeDto processingDto) {
        String configId = processingDto.getConfigId();
        ProcessConfig processConfig = processConfigService.getById(configId);
        if (processConfig == null) {
            // todo 直接抛出异常
            return null;
        }
        List<ProcessNode> nodeList = processNodeService.list(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId));

        return null;
    }

    public Object execute(ProcessNode node, Object o) throws ReflectiveOperationException, PersipaCustomException {
        ProcessingTypeEnum processingType = node.getProcessingType();
        switch (processingType) {
            case SEQUENTIAL:
                o = this.processingJsoupMethod(node.getId(), o);
                break;
            case LOOP:
                Assert.isTrue(o instanceof Iterable, () -> new PersipaCustomException(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION));
                List<Object> list = new ArrayList<>();
                for (Object value : (Iterable<?>) o) {
                    Object singleResult = this.processingJsoupMethod(node.getId(), value);
                    list.add(singleResult);
                }
                o = list;

                break;
            case ARRAY:
                // 先校验是否是数组
                Assert.isTrue(o != null && o.getClass().isArray(),
                        () -> new PersipaCustomException(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION));
                int length = Array.getLength(o);
                Class<?> resultType = Class.forName(node.getResultType());
                Object resultArray = Array.newInstance(resultType, length);
                for (int i = 0; i < length; i++) {
                    Object singleResult = this.processingJsoupMethod(node.getId(), Array.get(o, i));
                    Array.set(resultArray, i, singleResult);
                }
                o = resultArray;
                break;
            default:
                break;
        }
        return o;
    }

    private Object processingJsoupMethod(String processingId, Object inputArg) throws ReflectiveOperationException, PersipaCustomException {
        ProcessNode node = processNodeService.getById(processingId);
        // 获取执行的方法
        String methodId = node.getMethodId();

        // 获取执行的参数
        List<ProcessNodeEntity> processingEntityList = processNodeEntityService
                .list(Wrappers.lambdaQuery(ProcessNodeEntity.class)
                        .eq(ProcessNodeEntity::getProcessingId, processingId)
                        .orderByAsc(ProcessNodeEntity::getArgOrder));
        // 获取所有需要构造的参数
        List<String> constructEntityIdList = processingEntityList.stream()
                .filter(processingEntity -> NodeEntityGainTypeEnum.CONSTRUCT.equals(processingEntity.getGainType()))
                .map(ProcessNodeEntity::getEntityId)
                .collect(Collectors.toList());
        Map<String, Object> constructEntityMap = new HashMap<>();
        for (String entityId : constructEntityIdList) {
            Object tempArg = reflectEntityManager.construct(entityId);
            constructEntityMap.put(entityId, tempArg);
        }

        List<Object> argList = new ArrayList<>();
        for (ProcessNodeEntity processingEntity : processingEntityList) {
            Object tempArg = null;
            NodeEntityGainTypeEnum gainType = processingEntity.getGainType();
            switch (gainType) {
                case INPUT:
                    tempArg = inputArg;
                    break;
                case CONSTRUCT:
                    tempArg = constructEntityMap.get(processingEntity.getEntityId());
                    break;
                default:
                    break;
            }
            argList.add(tempArg);
        }
        Object result = reflectMethodManager.invokeMethod(methodId, argList.toArray(new Object[0]));

        // 校验返回结果
        Class<?> returnType = Class.forName(node.getResultType());
        Assert.isTrue(returnType.isInstance(result), () -> new PersipaCustomException(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION));

        return result;
    }

}
