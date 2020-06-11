package cn.cjz.mq;

import cn.cjz.config.RabbitConfig;
import cn.cjz.constant.Constant;
import cn.cjz.model.Mail;
import cn.cjz.service.MessageServiceImpl;
import cn.hutool.extra.mail.MailUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: Kam-Chou
 * @date: 2020/6/10 17:51
 * @description: 邮件消息的消费者
 * @version: 1.0
 */
@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    private MessageServiceImpl messageService;

    /**
     * 消费邮件消息.
     * @param message 邮件消息
     * @param channel 信道
     */
    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE)
    public void consumer(Message message, Channel channel) throws IOException {
        // 转换为 Mail 类
        Mail mail = MessageHelper.msgToObj(message, Mail.class);
        log.info("收到消息: {}", mail.toString());

        String correlationId = mail.getMsgId();
        cn.cjz.model.Message messageDto = messageService.selectById(correlationId);

        // 如果查询不到消息，或者消息已经是成功消费过的，那么就不消费，直接退出
        if (null == messageDto || messageDto.getMsgStatus().equals(Constant.MessageStatus.CONSUMERED)) {
            log.error("消息被重复消费, 消息id: {}", correlationId);
            return;
        }

        MessageProperties properties = message.getMessageProperties();
        long deliveryTag = properties.getDeliveryTag();

        try {
            // 发送邮件，并对消息进行确认.
            MailUtil.send(mail.getTo(), mail.getTitle(), mail.getContent(), false);
            messageService.updateStatus(correlationId, Constant.MessageStatus.CONSUMERED);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 如果发送邮件失败, 返回到队列中.
            channel.basicNack(deliveryTag, false, true);
        }

    }

}
