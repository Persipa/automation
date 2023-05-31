package site.persipa.automation.pojo.process.vo;

import lombok.Data;
import site.persipa.automation.enums.process.ProcessStatusEnum;

/**
 * @author persipa
 */
@Data
public class ProcessResultPreviewVo {

    private String configId;

    private ProcessStatusEnum processStatus;

    private String message;

    private Integer resultCount;

    private Object result;

    private Integer saveCount;

}
