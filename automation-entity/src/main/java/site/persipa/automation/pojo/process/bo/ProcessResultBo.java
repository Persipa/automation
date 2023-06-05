package site.persipa.automation.pojo.process.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.persipa.automation.enums.process.ProcessStatusEnum;

/**
 * @author persipa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResultBo {

    private String configId;

    private String configName;

    private ProcessStatusEnum processStatus;

    private String logId;

    private Integer resultCount;

    private Object result;

    private String message;

    private Integer saveCount;
}
