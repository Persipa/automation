package site.persipa.automation.pojo.process.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessConfigStateEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author persipa
 */
@Data
public class ProcessConfigDetailVo {

    private String id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源获取uri
     */
    private String resourcePostUri;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 执行状态
     */
    private ProcessConfigStateEnum processStatus;

    /**
     * 上次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastProcessTime;

    /**
     * 流程节点
     */
    private List<ProcessNodeVo> nodes;
}