package site.persipa.automation.pojo.process;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.automation.enums.process.ProcessStatusEnum;
import site.persipa.automation.enums.process.ProcessTypeEnum;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("process_log")
public class ProcessLog {

    @TableId
    private String id;

    private String configId;

    private ProcessTypeEnum processType;

    private ProcessStatusEnum processStatus;

    private LocalDateTime executeTime;

    private LocalDateTime completeTime;

}
