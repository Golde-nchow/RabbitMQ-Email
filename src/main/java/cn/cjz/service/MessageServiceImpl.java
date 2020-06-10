package cn.cjz.service;

import cn.cjz.config.RabbitConfig;
import cn.cjz.dao.MessageMapper;
import cn.cjz.model.Mail;
import cn.cjz.model.Message;
import cn.cjz.model.MessageResponse;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 消息入库
     */
    public String send(Mail mail) {
        String msgId = IdUtil.simpleUUID();
        mail.setMsgId(msgId);

        // 消息入库
        Message message = Message
                .builder()
                .msgId(msgId)
                .msgContent(JSONUtil.parse(mail).toString())
                .msgExchange(RabbitConfig.MAIL_EXCHANGE)
                .msgRoutingKey(RabbitConfig.MAIL_ROUTING_KEY)
                .build();
        messageMapper.insertSelective(message);

        // 关联id
        CorrelationData correlationData = new CorrelationData(msgId);

        // 发送消息到rabbitMQ

        return MessageResponse.success();
    }

    /**
     * 修改消息的状态
     * @param correlationId 消息ID
     * @param status        消息状态
     */
    public void updateStatus(String correlationId, Integer status) {
        Message message = Message.builder()
                .msgId(correlationId)
                .msgStatus(status)
                .build();
        messageMapper.updateByPrimaryKeySelective(message);
    }
}
