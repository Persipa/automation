package site.persipa.automation.process.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.mapstruct.process.MapProcessResultItemMapper;
import site.persipa.automation.mapstruct.process.MapProcessResultMapper;
import site.persipa.automation.pojo.process.ProcessResult;
import site.persipa.automation.pojo.process.ProcessResultItem;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.pojo.process.dto.ProcessResultDto;
import site.persipa.automation.pojo.process.vo.ProcessResultCombineVo;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;
import site.persipa.automation.process.service.ProcessResultItemService;
import site.persipa.automation.process.service.ProcessResultService;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessResultManager {

    private final ProcessResultService processResultService;

    private final ProcessResultItemService processResultItemService;

    private final MapProcessResultMapper mapProcessResultMapper;

    private final MapProcessResultItemMapper mapProcessResultItemMapper;

    /**
     * 查询结果
     *
     * @param dto 查询参数
     * @return 查询的结果数组
     */
    public List<ProcessResultVo> list(ProcessResultDto dto) {
        List<ProcessResultItem> resultList = processResultItemService.list(Wrappers.lambdaQuery(ProcessResultItem.class)
                .eq(StringUtils.hasLength(dto.getConfigId()), ProcessResultItem::getConfigId, dto.getConfigId())
                .eq(StringUtils.hasLength(dto.getProcessId()), ProcessResultItem::getProcessId, dto.getProcessId())
                .eq(dto.getIsNew() != null, ProcessResultItem::getIsNew, dto.getIsNew())
                .eq(dto.getUsed() != null, ProcessResultItem::getUsed, dto.getUsed()));
        return resultList.stream().map(mapProcessResultItemMapper::toVo)
                .collect(Collectors.toList());
    }

    /**
     * 查询结果（并将结果合并为一个字段）
     *
     * @param dto 查询参数
     * @return 查询结果
     */
    public ProcessResultCombineVo listCombine(ProcessResultDto dto) {
        List<ProcessResultItem> processResultItemList = processResultItemService.list(Wrappers.lambdaQuery(ProcessResultItem.class)
                .eq(StringUtils.hasLength(dto.getConfigId()), ProcessResultItem::getConfigId, dto.getConfigId())
                .eq(dto.getUsed() != null, ProcessResultItem::getUsed, dto.getUsed()));
        List<String> resultList = processResultItemList.stream()
                .map(ProcessResultItem::getResult)
                .collect(Collectors.toList());
        return new ProcessResultCombineVo(dto.getConfigId(), resultList);
    }

    /**
     * 标记结果已被使用
     *
     * @param configId 执行的配置id
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean read(String configId) {
        return processResultItemService.update(Wrappers.lambdaUpdate(ProcessResultItem.class)
                .set(ProcessResultItem::getUsed, true)
                .set(ProcessResultItem::getUpdateTime, LocalDateTime.now())
                .eq(ProcessResultItem::getConfigId, configId)
                .eq(ProcessResultItem::getUsed, false));
    }

    /**
     * 标记结果已被使用
     *
     * @param resultIdList 结果id 数组
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean read(List<String> resultIdList) {
        if (CollectionUtils.isEmpty(resultIdList)) {
            return false;
        }
        return processResultItemService.update(Wrappers.lambdaUpdate(ProcessResultItem.class)
                .set(ProcessResultItem::getUsed, true)
                .set(ProcessResultItem::getUpdateTime, LocalDateTime.now())
                .in(ProcessResultItem::getId, resultIdList)
                .eq(ProcessResultItem::getUsed, false));
    }

    /**
     * 保存执行的结果
     *
     * @param processResultBo 执行的结果
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveResult(ProcessResultBo processResultBo) {
        ProcessResult processResult = mapProcessResultMapper.fromResultBo(processResultBo);
        processResultService.save(processResult);

        List<ProcessResultItem> existResultList = processResultItemService.list(Wrappers.lambdaQuery(ProcessResultItem.class)
                .eq(ProcessResultItem::getConfigId, processResultBo.getConfigId())
                .eq(ProcessResultItem::getIsNew, true));
        Set<String> existResultContentSet = existResultList.stream()
                .map(ProcessResultItem::getResult)
                .collect(Collectors.toSet());

        Object resultObject = processResultBo.getResult();
        if (ProcessStatusEnum.SUCCESS.equals(processResultBo.getProcessStatus()) && resultObject != null) {
            List<ProcessResultItem> processResultItemList = new ArrayList<>();
            if (resultObject instanceof Iterable) {
                for (Object o : (Iterable<?>) resultObject) {
                    ProcessResultItem tempResult = new ProcessResultItem();
                    tempResult.setConfigId(processResultBo.getConfigId());
                    tempResult.setProcessId(processResultBo.getProcessId());
                    tempResult.setResult(o.toString());
                    tempResult.setIsNew(existResultContentSet.contains(o.toString()));
                    processResultItemList.add(tempResult);
                }
            } else if (resultObject.getClass().isArray()) {
                int length = Array.getLength(resultObject);
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(resultObject, i);
                    if (!(o instanceof Serializable)) {
                        ProcessResultItem tempResult = new ProcessResultItem();
                        tempResult.setConfigId(processResultBo.getConfigId());
                        tempResult.setProcessId(processResultBo.getProcessId());
                        tempResult.setResult(o.toString());
                        tempResult.setIsNew(existResultContentSet.contains(o.toString()));
                        processResultItemList.add(tempResult);
                    }
                }
            } else {
                ProcessResultItem tempResult = new ProcessResultItem();
                tempResult.setConfigId(processResultBo.getConfigId());
                tempResult.setProcessId(processResultBo.getProcessId());
                tempResult.setResult(resultObject.toString());
                tempResult.setIsNew(existResultContentSet.contains(resultObject.toString()));
                processResultItemList.add(tempResult);
            }
            if (!processResultItemList.isEmpty()) {
                processResultItemService.saveBatch(processResultItemList);
            }
        }
    }

    public List<ProcessResultVo> listByProcessId(String processId) {
        List<ProcessResultItem> resultItemList = processResultItemService.list(Wrappers.lambdaQuery(ProcessResultItem.class)
                .eq(ProcessResultItem::getProcessId, processId));
        return resultItemList.stream()
                .map(mapProcessResultItemMapper::toVo)
                .collect(Collectors.toList());
    }
}
