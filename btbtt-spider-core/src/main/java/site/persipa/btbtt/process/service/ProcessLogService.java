package site.persipa.btbtt.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.btbtt.enums.process.ProcessTypeEnum;
import site.persipa.btbtt.pojo.process.ProcessLog;

/**
 * @author persipa
 */
public interface ProcessLogService extends IService<ProcessLog> {

    String saveLog(String configId, ProcessTypeEnum processType);

    boolean completeLog(String logId, boolean success);

}
