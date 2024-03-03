package site.persipa.automation.process.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.persipa.automation.pojo.process.ProcessExecutionTicket;
import site.persipa.automation.process.mapper.ProcessExecutionTicketMapper;
import site.persipa.automation.process.service.ProcessExecutionTicketService;

/**
 * @author persipa
 */
@Service
public class ProcessExecutionTicketServiceImpl extends ServiceImpl<ProcessExecutionTicketMapper, ProcessExecutionTicket>
        implements ProcessExecutionTicketService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessExecutionTicket generateTicket() {
        ProcessExecutionTicket ticket = new ProcessExecutionTicket();
        ticket.setUsed(false);
        this.save(ticket);
        return ticket;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bind(String ticketId, String executionId, String relationId) {
        ProcessExecutionTicket ticket = this.getById(ticketId);
        Assert.notNull(ticket, "找不到票据");
        ticket.setExecutionId(executionId);
        ticket.setRelationId(relationId);
        this.updateById(ticket);
    }

}
