package cn.cjz.aspect;

import cn.cjz.constant.Constant;
import cn.cjz.model.Mail;
import cn.cjz.mq.MessageHelper;
import cn.cjz.service.MessageServiceImpl;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: Kam-Chou
 * @date: 2020/6/16 11:44
 * @description: 消费者通用逻辑切面类
 * @version: 1.0
 */
@Aspect
@Component
@Slf4j
public class ConsumerCommonAspect {

    @Autowired
    private MessageServiceImpl messageService;

    @Pointcut("@annotation(cn.cjz.annotation.ConsumerCommon)")
    void pointcut() {

    }

    @Around("pointcut()")
    @SuppressWarnings("all")
    public void checkAndCommit(ProceedingJoinPoint joinPoint) throws IOException {
        // 获取参数
        Object[] args = joinPoint.getArgs();
        Message message = null;
        Channel channel = null;

        // 判断参数类型
        for (Object arg : args) {
            if (arg instanceof Message) {
                message = (Message) arg;
            } else if (arg instanceof Channel){
                channel = (Channel) arg;
            }
        }

        // 幂等性校验
        // 转换为 Mail 类
        Mail mail = MessageHelper.msgToObj(message, Mail.class);

        String correlationId = mail.getMsgId();
        cn.cjz.model.Message messageDto = messageService.selectById(correlationId);

        // 如果查询不到消息，或者消息已经是成功消费过的，那么就不消费，直接退出
        if (null == messageDto || messageDto.getMsgStatus().equals(Constant.MessageStatus.CONSUMERED)) {
            log.error("消息被重复消费, 消息id: {}", correlationId);
            return;
        }

        // 调用目标类逻辑
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        // ACK
        MessageProperties properties = message.getMessageProperties();
        long deliveryTag = properties.getDeliveryTag();
        try {
            // 发送邮件，并对消息进行确认.
            messageService.updateStatus(correlationId, Constant.MessageStatus.CONSUMERED);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 如果发送邮件失败, 返回到队列中.
            channel.basicNack(deliveryTag, false, true);
        }
    }

}
