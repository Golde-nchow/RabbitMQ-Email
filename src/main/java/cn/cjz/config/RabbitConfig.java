package cn.cjz.config;

import cn.cjz.constant.Constant;
import cn.cjz.service.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Kam-Chou
 * @date: 2020/6/10 16:19
 * @description: RabbitMQ 配置
 * @version: 1.0
 */
@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private MessageServiceImpl messageService;

    /**
     * 邮件队列、交换器、路由键名称
     */
    public static final String MAIL_QUEUE = "mail.queue";
    public static final String MAIL_EXCHANGE = "mail.exchange";
    public static final String MAIL_ROUTING_KEY = "mail.routing.key";

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());

        // 设置发送后的回调
        rabbitTemplate.setConfirmCallback(((correlationData, ack, exception) -> {
            if (ack) {
                log.info("消息成功发送到交换器");
                String correlationDataId = correlationData.getId();
                // 修改消息的投递状态：成功
                messageService.updateStatus(correlationDataId, Constant.MessageStatus.DELEVER_SUCCESS);

            } else {
                // 如果投递失败
                log.error("消息投递失败, {}, 原因: {}", correlationData, exception);
            }
        }));

        // mandatory为true时，需要添加监听器逻辑，来处理无法路由时, 返回的消息
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息无法从exchange路由到queue...");
            log.error("exchange:{}, routingKey:{}, replyCode:{}, replyText:{}, message:{}", exchange, routingKey, replyCode, replyText, message);
        }));

        return rabbitTemplate;
    }

    /**
     * 创建邮件队列
     */
    private Queue mailQueue() {
        return new Queue(MAIL_QUEUE, true);
    }

    /**
     * 创建邮件交换器类, 类型为 direct
     */
    private DirectExchange mailExchange() {
        return new DirectExchange(MAIL_EXCHANGE, true, false);
    }

    /**
     * 将队列、交换器、路由键进行绑定
     */
    public Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
