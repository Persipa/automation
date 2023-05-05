package site.persipa.btbtt.process.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.ProcessingTypeEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.enums.spider.NodeEntityGainTypeEnum;
import site.persipa.btbtt.exception.reflect.ProcessingException;
import site.persipa.btbtt.reflect.manager.ReflectEntityManager;
import site.persipa.btbtt.reflect.manager.ReflectMethodManager;
import site.persipa.btbtt.pojo.process.ProcessNode;
import site.persipa.btbtt.pojo.process.ProcessNodeEntity;
import site.persipa.btbtt.process.service.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
public class ProcessManager {

    @Autowired
    private ProcessNodeService processNodeService;

    @Autowired
    private ProcessNodeEntityService processNodeEntityService;

    @Autowired
    private ReflectMethodManager jsoupMethodManager;
    @Autowired
    private ReflectEntityManager jsoupEntityManager;

    public void execute(String configId) throws ReflectiveOperationException {
        List<ProcessNode> nodeList = processNodeService.list(Wrappers.lambdaQuery(ProcessNode.class)
                .eq(ProcessNode::getConfigId, configId)
                .orderByAsc(ProcessNode::getSort));
        Object o = null;
        for (ProcessNode node : nodeList) {
            ProcessingTypeEnum processingType = node.getProcessingType();
            try {
                switch (processingType) {
                    case SEQUENTIAL:
                        o = this.processingJsoupMethod(node.getId(), o);
                        break;
                    case LOOP:
                        if (o instanceof Iterable) {
                            List<Object> list = new ArrayList<>();
                            for (Object value : (Iterable<?>) o) {
                                Object singleResult = this.processingJsoupMethod(node.getId(), value);
                                list.add(singleResult);
                            }
                            o = list;
                        } else {
                            throw ProcessingException.expected(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION);
                        }
                        break;
                    case ARRAY:
                        // 先校验是否是数组
                        if (o != null && !o.getClass().isArray()) {
                            throw ProcessingException.expected(ProcessingExceptionEnum.CLASS_TYPE_NOT_MATCH_EXCEPTION);
                        }
                        int length = Array.getLength(o);
                        Class<?> resultType = Class.forName(node.getResultType());
                        Object resultArray = Array.newInstance(resultType, length);
                        for (int i = 0; i < length; i++) {
                            Object singleResult = this.processingJsoupMethod(node.getId(), Array.get(o, i));
                            Array.set(resultArray, i, singleResult);
                        }
                        o = resultArray;
                        break;
                }
            } catch (ProcessingException e) {
                // todo 记录日志
                System.out.println(e.getDescription());
            }
            // todo 暂时不做校验
            Class<?> returnType = Class.forName(node.getResultType());
            if (returnType.isInstance(o)) {
                System.out.println("body:");
                System.out.println(o);
                System.out.println("body end.");
            } else {
                System.out.println("processing error");
                System.out.println(o);
            }
        }
    }

    private Object processingJsoupMethod(String processingId, Object inputArg) throws ProcessingException {
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
            Object tempArg = jsoupEntityManager.construct(entityId);
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
        return jsoupMethodManager.invokeMethod(methodId, argList.toArray(new Object[0]));
    }

}
