package cn.cjz.service;

import cn.cjz.config.RabbitConfig;
import cn.cjz.dao.MessageMapper;
import cn.cjz.model.Mail;
import cn.cjz.model.Message;
import cn.cjz.model.MessageResponse;
import cn.cjz.mq.MessageHelper;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: Kam-Chou
 * @date: 2020/6/9 15:51
 * @description: 消息业务层
 * @version: 1.0
 */
@Service
public class MessageServiceImpl {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 通过消息的correlationId查询出消息入库的信息
     * @param correlationId 消息对应的id
     * @return              数据库的 MessageDto 类
     */
    public Message selectById(String correlationId) {
        return messageMapper.selectByPrimaryKey(correlationId);
    }

    /**
     * 消息入库
     */
    public MessageResponse send(Mail mail) {
        String msgId = IdUtil.simpleUUID();
        mail.setMsgId(msgId);

        // 消息入库
        Date time = new Date();
        DateTime createTime = new DateTime(time);
        // 下次重试时间在失败的一分钟后
        DateTime nextTryTime = DateUtil.offsetMinute(time, 1);

        Message message = Message
                .builder()
                .msgId(msgId)
                .msgContent(JSONUtil.parse(mail).toString())
                .msgExchange(RabbitConfig.MAIL_EXCHANGE)
                .msgRoutingKey(RabbitConfig.MAIL_ROUTING_KEY)
                .createTime(createTime.toSqlDate())
                .nextRetryTime(nextTryTime.toSqlDate())
                .updateTime(createTime.toSqlDate())
                .build();
        messageMapper.insertSelective(message);

        // 该消息关联的id
        CorrelationData correlationData = new CorrelationData(msgId);

        // 发送消息到rabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitConfig.MAIL_EXCHANGE,
                RabbitConfig.MAIL_ROUTING_KEY,
                MessageHelper.objToMsg(mail),
                correlationData
        );

        return MessageResponse.success();
    }

    /**
     * 修改消息的状态
     * @param correlationId 消息ID
     * @param status        消息状态
     */
    public void updateStatus(String correlationId, Integer status) {
        Date time = new Date();
        DateTime updateTime = new DateTime(time);

        Message message = Message.builder()
                .msgId(correlationId)
                .msgStatus(status)
                .updateTime(updateTime.toSqlDate())
                .build();
        messageMapper.updateByPrimaryKeySelective(message);
    }
}
