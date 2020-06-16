package cn.cjz.mq;

import cn.cjz.annotation.ConsumerCommon;
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
    @ConsumerCommon
    public void consumer(Message message, Channel channel) {
        // 转换为 Mail 类
        Mail mail = MessageHelper.msgToObj(message, Mail.class);
        log.info("收到消息: {}", mail.toString());

        // 发送邮件，并对消息进行确认.
        MailUtil.send(mail.getTo(), mail.getTitle(), mail.getContent(), false);
    }

}
