package cn.cjz.task;

import cn.cjz.constant.Constant;
import cn.cjz.dao.MessageMapper;
import cn.cjz.model.Message;
import cn.cjz.mq.MessageHelper;
import cn.cjz.service.MessageServiceImpl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: Kam-Chou
 * @date: 2020/6/10 21:50
 * @description: 邮件重发定时器，每30秒执行一次
 * @version: 1.0
 */
@Slf4j
@Component
public class ResendEmail {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRY_COUNT = 3;

    /**
     * 每30s重新投递失败的消息
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void resend() {
        log.info("开始定时任务(重新投递消息)");

        List<Message> messages = messageMapper.selectTimeoutMessage();
        messages.forEach(message -> {
            String msgId = message.getMsgId();
            if (message.getMsgRetryCount() >= MAX_RETRY_COUNT) {
                // 超过最大重试次数
                messageService.updateStatus(msgId, Constant.MessageStatus.DELEVER_FAIL);
                log.error("已超过最大重试次数, 消息投递失败");
            } else {
                // 未超过最大重试次数
                DateTime nextTryTime = DateUtil.offsetMinute(new Date(), 1);
                messageMapper.updateRetryCount(msgId, nextTryTime.toSqlDate());

                CorrelationData correlationData = new CorrelationData(msgId);
                rabbitTemplate.convertAndSend(
                        message.getMsgExchange(),
                        message.getMsgRoutingKey(),
                        MessageHelper.objToMsg(message.getMsgContent()),
                        correlationData
                );

                log.info("该 {} 信息第{}次投递.", msgId, message.getMsgRetryCount() + 1);
            }
        });
        log.info("定时任务(重新投递)执行结束.");
    }

}
