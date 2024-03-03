package site.persipa.automation.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.process.ProcessExecutionTicket;

/**
 * @author persipa
 */
public interface ProcessExecutionTicketService extends IService<ProcessExecutionTicket> {

    ProcessExecutionTicket generateTicket();

    void bind(String ticketId, String executionId, String relationId);
}
