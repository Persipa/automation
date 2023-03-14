package site.persipa.btbtt.spider.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import site.persipa.btbtt.enums.ProcessingTypeEnum;
import site.persipa.btbtt.enums.spider.ProcessingEntityGainTypeEnum;
import site.persipa.btbtt.jsoup.manager.JsoupEntityManager;
import site.persipa.btbtt.jsoup.manager.JsoupMethodManager;
import site.persipa.btbtt.pojo.btbtt.*;
import site.persipa.btbtt.spider.constant.BtbttConstant;
import site.persipa.btbtt.spider.service.*;
import site.persipa.btbtt.spider.util.JsoupUtil;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
public class SpiderManager {

    @Autowired
    private BtbttSpiderConfigService spiderConfigService;
    @Autowired
    private BtbttSpiderSelectorService selectorService;
    @Autowired
    private BtbttSpiderResultService spiderResultService;
    @Autowired
    private BtbttResultService resultService;
    @Autowired
    private BtbttSpiderProcessingService spiderProcessingService;

    @Autowired
    private BtbttSpiderProcessingEntityService spiderProcessingEntityService;

    @Autowired
    private JsoupMethodManager jsoupMethodManager;
    @Autowired
    private JsoupEntityManager jsoupEntityManager;

    public void process(String configId) {
        BtbttSpiderConfig spiderConfig = spiderConfigService.getById(configId);
        BtbttSpiderSelector selector = selectorService.getOne(Wrappers.lambdaQuery(BtbttSpiderSelector.class)
                .eq(BtbttSpiderSelector::getConfigId, configId), false);
        String uri = spiderConfig.getResourcePostUri();
        String url = BtbttConstant.postUrl(uri);
        Document document;
        try {
            document = JsoupUtil.getDocument(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element body = document.body();
        Elements elements = body.select(selector.getCssSelector());
        List<String> spiderResultList = elements.stream()
                .map(tag -> selector.getResultType().equals(0) ? tag.text() : tag.attr(selector.getAttrKey()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(spiderResultList)) {
            BtbttSpiderResult spiderResult = new BtbttSpiderResult();
            spiderResult.setConfigId(configId);
            spiderResult.setContent(spiderResultList.toString());
            spiderResultService.save(spiderResult);
        }
        List<BtbttResult> resultList = resultService.list(Wrappers.lambdaQuery(BtbttResult.class)
                .eq(BtbttResult::getConfigId, configId));
        if (!CollectionUtils.isEmpty(resultList)) {
            Set<String> resultSet = resultList.stream()
                    .map(BtbttResult::getResult)
                    .collect(Collectors.toSet());
            spiderResultList.removeIf(resultSet::contains);
        }
        if (!CollectionUtils.isEmpty(spiderResultList)) {
            List<BtbttResult> newResultList = spiderResultList.stream()
                    .map(result -> {
                        BtbttResult btbttResult = new BtbttResult();
                        btbttResult.setConfigId(configId);
                        btbttResult.setResult(result);
                        return btbttResult;
                    }).collect(Collectors.toList());
            resultService.saveBatch(newResultList);
        }
    }

    public void process2(String configId) throws ReflectiveOperationException {
        List<BtbttSpiderProcessing> processingList = spiderProcessingService.list(Wrappers.lambdaQuery(BtbttSpiderProcessing.class)
                .eq(BtbttSpiderProcessing::getConfigId, configId)
                .orderByAsc(BtbttSpiderProcessing::getSort));
        Object o = null;
        for (BtbttSpiderProcessing processing : processingList) {
            ProcessingTypeEnum processingType = processing.getProcessingType();
            switch (processingType) {
                case SEQUENTIAL:
                    o = this.processingJsoupMethod(processing.getId(), o);
                    break;
                case LOOP:
                    if (o instanceof Iterable) {
                        List<Object> list = new ArrayList<>();
                        Iterator<?> iterator = ((Iterable<?>) o).iterator();
                        while (iterator.hasNext()) {
                            Object singleResult = this.processingJsoupMethod(processing.getId(), o);
                            list.add(singleResult);
                        }
                        o = list;
                    }
                    break;
                case ARRAY:
                    // 先校验是否是数组
                    if (o != null && !o.getClass().isArray()) {
                        // todo 非数组
                        return;
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

    private Object processingJsoupMethod(String processingId, Object inputArg) {
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
