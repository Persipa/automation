package site.persipa.btbtt.spider.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.ProcessingTypeEnum;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.enums.spider.ProcessingEntityGainTypeEnum;
import site.persipa.btbtt.exception.jsoup.ProcessingException;
import site.persipa.btbtt.jsoup.manager.JsoupEntityManager;
import site.persipa.btbtt.jsoup.manager.JsoupMethodManager;
import site.persipa.btbtt.pojo.btbtt.BtbttSpiderProcessing;
import site.persipa.btbtt.pojo.btbtt.BtbttSpiderProcessingEntity;
import site.persipa.btbtt.spider.service.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
public class SpiderManager {

    @Autowired
    private BtbttSpiderProcessingService spiderProcessingService;

    @Autowired
    private BtbttSpiderProcessingEntityService spiderProcessingEntityService;

    @Autowired
    private JsoupMethodManager jsoupMethodManager;
    @Autowired
    private JsoupEntityManager jsoupEntityManager;

    public void process2(String configId) throws ReflectiveOperationException {
        List<BtbttSpiderProcessing> processingList = spiderProcessingService.list(Wrappers.lambdaQuery(BtbttSpiderProcessing.class)
                .eq(BtbttSpiderProcessing::getConfigId, configId)
                .orderByAsc(BtbttSpiderProcessing::getSort));
        Object o = null;
        for (BtbttSpiderProcessing processing : processingList) {
            ProcessingTypeEnum processingType = processing.getProcessingType();
            try {
                switch (processingType) {
                    case SEQUENTIAL:
                        o = this.processingJsoupMethod(processing.getId(), o);
                        break;
                    case LOOP:
                        if (o instanceof Iterable) {
                            List<Object> list = new ArrayList<>();
                            for (Object value : (Iterable<?>) o) {
                                Object singleResult = this.processingJsoupMethod(processing.getId(), value);
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
                        Class<?> resultType = Class.forName(processing.getResultType());
                        Object resultArray = Array.newInstance(resultType, length);
                        for (int i = 0; i < length; i++) {
                            Object singleResult = this.processingJsoupMethod(processing.getId(), Array.get(o, i));
                            Array.set(resultArray, i, singleResult);
                        }
                        o = resultArray;
                        break;
                }
            } catch (ProcessingException e) {
                // todo 记录日志
                System.out.println(e.getDescription());
            }
            // todo 是否需要校验呢？？？？
            Class<?> returnType = Class.forName(processing.getResultType());
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
        BtbttSpiderProcessing processing = spiderProcessingService.getById(processingId);
        // 获取执行的方法
        String methodId = processing.getMethodId();

        // 获取执行的参数
        List<BtbttSpiderProcessingEntity> processingEntityList = spiderProcessingEntityService
                .list(Wrappers.lambdaQuery(BtbttSpiderProcessingEntity.class)
                        .eq(BtbttSpiderProcessingEntity::getProcessingId, processingId)
                        .orderByAsc(BtbttSpiderProcessingEntity::getArgOrder));
        // 获取所有需要构造的参数
        List<String> constructEntityIdList = processingEntityList.stream()
                .filter(processingEntity -> ProcessingEntityGainTypeEnum.CONSTRUCT.equals(processingEntity.getGainType()))
                .map(BtbttSpiderProcessingEntity::getEntityId)
                .collect(Collectors.toList());
        Map<String, Object> constructEntityMap = new HashMap<>();
        for (String entityId : constructEntityIdList) {
            Object tempArg = jsoupEntityManager.construct(entityId);
            constructEntityMap.put(entityId, tempArg);
        }

        List<Object> argList = new ArrayList<>();
        for (BtbttSpiderProcessingEntity processingEntity : processingEntityList) {
            Object tempArg = null;
            ProcessingEntityGainTypeEnum gainType = processingEntity.getGainType();
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
