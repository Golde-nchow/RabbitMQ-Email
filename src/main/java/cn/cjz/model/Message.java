package cn.cjz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.sql.Date;

/**
 * @author: Kam-Chou
 * @date: 2020/6/9 15:44
 * @description: 消息实体类
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@Table(name = "msg_log")
public class Message {

    /**
     * 消息唯一标识
     */
    @Column(name = "msg_id")
    private String msgId;

    /**
     * 消息内容
     */
    @Column(name = "msg_content")
    private String msgContent;

    /**
     * exchange交换器
     */
    @Column(name = "msg_exchange")
    private String msgExchange;

    /**
     * 路由键
     */
    @Column(name = "msg_RoutingKey")
    private String msgRoutingKey;

    /**
     * 0投递中，1投递成功，2投递失败，3已消费
     */
    @Column(name = "msg_status")
    private Integer msgStatus;

    /**
     * 重试次数
     */
    @Column(name = "msg_retryCount")
    private Integer msgRetryCount;

    /**
     * 下次重试时间
     */
    @Column(name = "next_retry_time")
    private Date nextRetryTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}
