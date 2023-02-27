package site.persipa.btbtt.spider.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import site.persipa.btbtt.pojo.BtbttResult;
import site.persipa.btbtt.pojo.BtbttSpiderConfig;
import site.persipa.btbtt.pojo.BtbttSpiderResult;
import site.persipa.btbtt.pojo.BtbttSpiderSelector;
import site.persipa.btbtt.spider.constant.BtbttConstant;
import site.persipa.btbtt.spider.service.BtbttResultService;
import site.persipa.btbtt.spider.service.BtbttSpiderResultService;
import site.persipa.btbtt.spider.service.BtbttSpiderSelectorService;
import site.persipa.btbtt.spider.service.BtbttSpiderConfigService;
import site.persipa.btbtt.spider.util.JsoupUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
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
}
