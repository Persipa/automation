package site.persipa.automation.process.manager;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import site.persipa.automation.enums.process.ProcessExecuteCompletionStatEnum;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.mapstruct.process.MapProcessExecutionMapper;
import site.persipa.automation.mapstruct.process.MapProcessResultItemMapper;
import site.persipa.automation.mapstruct.process.MapProcessResultMapper;
import site.persipa.automation.pojo.process.ProcessExecution;
import site.persipa.automation.pojo.process.ProcessExecutionResult;
import site.persipa.automation.pojo.process.ProcessExecutionTicket;
import site.persipa.automation.pojo.process.ProcessResultItem;
import site.persipa.automation.pojo.process.bo.ProcessResultBo;
import site.persipa.automation.pojo.process.dto.ProcessResultDto;
import site.persipa.automation.pojo.process.vo.ProcessExecutionResultVo;
import site.persipa.automation.pojo.process.vo.ProcessResultCombineVo;
import site.persipa.automation.pojo.process.vo.ProcessResultVo;
import site.persipa.automation.process.service.ProcessExecutionResultService;
import site.persipa.automation.process.service.ProcessExecutionTicketService;
import site.persipa.automation.process.service.ProcessResultItemService;
import site.persipa.automation.process.service.ProcessExecutionService;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ProcessResultManager {

    private final ProcessExecutionService processExecutionService;
    private final ProcessResultItemService processResultItemService;
    private final ProcessExecutionResultService processExecutionResultService;
    private final ProcessExecutionTicketService processExecutionTicketService;

    private final MapProcessResultMapper mapProcessResultMapper;
    private final MapProcessResultItemMapper mapProcessResultItemMapper;
    private final MapProcessExecutionMapper mapProcessExecutionMapper;

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
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public void saveResult(ProcessResultBo processResultBo) {
        ProcessExecution processResult = mapProcessResultMapper.fromResultBo(processResultBo);
        processExecutionService.save(processResult);

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

    @Transactional(rollbackFor = Exception.class)
    public void saveResult(String executionId, Object resultObject) {
        if (StringUtils.hasLength(executionId) && resultObject != null) {
            List<ProcessExecutionResult> resultList = new ArrayList<>();
            if (resultObject instanceof Iterable<?> iterable) {
                for (Object o : iterable) {
                    ProcessExecutionResult executionResult = new ProcessExecutionResult();
                    executionResult.setExecutionId(executionId);
                    executionResult.setContent(o.toString());
                    resultList.add(executionResult);
                }
            } else if (resultObject.getClass().isArray()) {
                int length = Array.getLength(resultObject);
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(resultObject, i);
                    if (!(o instanceof Serializable)) {
                        ProcessExecutionResult executionResult = new ProcessExecutionResult();
                        executionResult.setExecutionId(executionId);
                        executionResult.setContent(o.toString());
                        resultList.add(executionResult);
                    }
                }
            } else {
                ProcessExecutionResult executionResult = new ProcessExecutionResult();
                executionResult.setExecutionId(executionId);
                executionResult.setContent(resultObject.toString());
                resultList.add(executionResult);
            }

            // 保存
            if (!resultList.isEmpty()) {
                processExecutionResultService.saveBatch(resultList);
            }
        }
    }

    public ProcessExecutionResultVo getResultByTicket(String ticketId) {
        // todo 校验ticket 有效期
        ProcessExecutionTicket ticket = processExecutionTicketService.getById(ticketId);
        Assert.notNull(ticket, "票据有误");
        ProcessExecution processExecution = processExecutionService.getById(ticket.getExecutionId());
        ProcessExecutionResultVo result;
        if (processExecution == null) {
            result = new ProcessExecutionResultVo();
            result.setCompletionStat(ProcessExecuteCompletionStatEnum.UNKNOWN);
        } else {
            result = mapProcessExecutionMapper.toResultVo(processExecution);
            List<ProcessExecutionResult> executionResultList = this.listByTicket(ticketId);
            result.setResults(executionResultList);
        }
        return result;
    }

    public List<ProcessExecutionResult> listByTicket(String ticketId) {
        ProcessExecutionTicket ticket = processExecutionTicketService.getById(ticketId);
        Assert.notNull(ticket, "ticket 不正确");
        // 如果有引用值则优先引用的值
        if (StringUtils.hasLength(ticket.getRelationId())) {
            ticket = processExecutionTicketService.getById(ticket.getRelationId());
            Assert.notNull(ticket, "关联ticket 不正确");
        }
        ProcessExecution processExecution = processExecutionService.getById(ticket.getExecutionId());
        Assert.notNull(processExecution, "无执行记录");
        if (ProcessExecuteCompletionStatEnum.SUCCESS.equals(processExecution.getCompletionStat())) {
            return processExecutionResultService.list(Wrappers.lambdaQuery(ProcessExecutionResult.class)
                    .eq(ProcessExecutionResult::getExecutionId, processExecution.getId()));
        }
        return Collections.emptyList();
    }

}