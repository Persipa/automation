package site.persipa.automation.process.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.mapstruct.process.MapProcessResultMapper;
import site.persipa.automation.pojo.process.ProcessResult;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.pojo.process.dto.ProcessResultDto;
import site.persipa.automation.pojo.process.vo.ProcessResultCombineVo;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;
import site.persipa.automation.process.service.ProcessResultService;

import java.io.Serializable;
import java.lang.reflect.Array;
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

    private final MapProcessResultMapper mapProcessResultMapper;

    public List<ProcessResultVo> list(ProcessResultDto dto) {
        List<ProcessResult> resultList = processResultService.list(Wrappers.lambdaQuery(ProcessResult.class)
                .eq(ProcessResult::getConfigId, dto.getConfigId())
                .eq(dto.getUsed() != null, ProcessResult::getUsed, dto.getUsed()));
        return resultList.stream().map(mapProcessResultMapper::toVo)
                .collect(Collectors.toList());
    }

    public ProcessResultCombineVo listCombine(ProcessResultDto dto) {
        List<ProcessResult> processResultList = processResultService.list(Wrappers.lambdaQuery(ProcessResult.class)
                .eq(ProcessResult::getConfigId, dto.getConfigId())
                .eq(dto.getUsed() != null, ProcessResult::getUsed, dto.getUsed()));
        List<String> resultList = processResultList.stream()
                .map(ProcessResult::getResult)
                .collect(Collectors.toList());
        return new ProcessResultCombineVo(dto.getConfigId(), resultList);
    }

    public boolean read(String configId) {
        return processResultService.update(Wrappers.lambdaUpdate(ProcessResult.class)
                .set(ProcessResult::getUsed, true)
                .eq(ProcessResult::getConfigId, configId)
                .eq(ProcessResult::getUsed, false));
    }

    public boolean read(List<String> resultIdList) {
        if (CollUtil.isEmpty(resultIdList)) {
            return false;
        }
        return processResultService.update(Wrappers.lambdaUpdate(ProcessResult.class)
                .set(ProcessResult::getUsed, true)
                .in(ProcessResult::getId, resultIdList)
                .eq(ProcessResult::getUsed, false));
    }

    public Integer saveResult(ProcessResultBo processResultBo) {
        List<ProcessResult> existResultList = processResultService.list(Wrappers.lambdaQuery(ProcessResult.class)
                .eq(ProcessResult::getConfigId, processResultBo.getConfigId()));
        Set<String> existResultContentSet = existResultList.stream()
                .map(ProcessResult::getResult)
                .collect(Collectors.toSet());
        Object resultObject = processResultBo.getResult();
        if (ProcessStatusEnum.SUCCESS.equals(processResultBo.getProcessStatus()) && resultObject != null) {
            List<ProcessResult> processResultList = new ArrayList<>();
            if (resultObject instanceof Iterable) {
                for (Object o : (Iterable<?>) resultObject) {
                    if (existResultContentSet.contains(o.toString())) {
                        continue;
                    }
                    ProcessResult tempResult = new ProcessResult();
                    tempResult.setConfigId(processResultBo.getConfigId());
                    tempResult.setLogId(processResultBo.getLogId());
                    tempResult.setResult(o.toString());
                    processResultList.add(tempResult);
                }
            } else if (resultObject.getClass().isArray()) {
                int length = Array.getLength(resultObject);
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(resultObject, i);
                    if (!(o instanceof Serializable)) {
                        if (existResultContentSet.contains(o.toString())) {
                            continue;
                        }
                        ProcessResult tempResult = new ProcessResult();
                        tempResult.setConfigId(processResultBo.getConfigId());
                        tempResult.setLogId(processResultBo.getLogId());
                        tempResult.setResult(o.toString());
                        processResultList.add(tempResult);
                    }
                }
            } else {
                if (!existResultContentSet.contains(resultObject.toString())) {
                    ProcessResult tempResult = new ProcessResult();
                    tempResult.setConfigId(processResultBo.getConfigId());
                    tempResult.setLogId(processResultBo.getLogId());
                    tempResult.setResult(resultObject.toString());
                    processResultList.add(tempResult);
                }
            }
            if (!processResultList.isEmpty()) {
                processResultService.saveBatch(processResultList);
            }
            return processResultList.size();
        }
        return null;
    }

}
