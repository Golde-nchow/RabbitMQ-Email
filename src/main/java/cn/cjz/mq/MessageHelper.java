package cn.cjz.mq;

import cn.hutool.json.JSONUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

/**
 * @author: Kam-Chou
 * @date: 2020/6/10 18:38
 * @description: amqp消息类转换类
 * @version: 1.0
 */
public class MessageHelper {

    /**
     * 普通对象转amqpMessage对象
     * @param object 普通对象
     * @return       amqpMessage对象
     */
    public static Message objToMsg(Object object) {
        if (object == null) {
            return null;
        }
        Message message = MessageBuilder
                .withBody(JSONUtil.toJsonStr(object).getBytes())
                .build();
        // 实现消息的持久化
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        // 设置数据格式 JSON
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return message;
    }

    /**
     * amqp Message对象转Object
     * @param message amqp Message对象
     * @return        普通对象
     */
    public static <T> T msgToObj(Message message, Class<T> clazz) {
        if (message == null || clazz == null) {
            return null;
        }
        return JSONUtil.toBean(new String(message.getBody()), clazz);
    }

}
