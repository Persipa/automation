package site.persipa.btbtt.pojo.process;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import site.persipa.btbtt.enums.process.ProcessStatusEnum;
import site.persipa.btbtt.enums.process.ProcessTypeEnum;

import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
@TableName("process_log")
public class ProcessLog {

    private String id;

    private String configId;

    private ProcessTypeEnum processType;

    private ProcessStatusEnum processStatus;

    private LocalDateTime executeTime;

    private LocalDateTime completeTime;

}
