package site.persipa.automation.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.persipa.automation.constant.RabbitConstant;

/**
 * @author persipa  1·
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue processExecuteQueue() {
        return new Queue(RabbitConstant.AUTOMATION_PROCESS_EXECUTE_QUEUE, true);
    }

    @Bean
    public DirectExchange processExecuteDirectExchange() {
        return new DirectExchange(RabbitConstant.AUTOMATION_PROCESS_EXECUTE_DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Binding processExecuteBinding() {
        return BindingBuilder
                .bind(processExecuteQueue())
                .to(processExecuteDirectExchange())
                .with(RabbitConstant.AUTOMATION_PROCESS_EXECUTE_ROUTING);
    }

}
