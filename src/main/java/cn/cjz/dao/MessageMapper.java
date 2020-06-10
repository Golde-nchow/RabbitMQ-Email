package cn.cjz.dao;

import cn.cjz.model.Message;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author: Kam-Chou
 * @date: 2020/6/9 15:49
 * @description: 消息Mapper类
 * @version: 1.0
 */
@Component
public interface MessageMapper extends Mapper<Message> {

    /**
     * 查询超时的邮件消息（由于失败导致1分钟后再重试）
     */
    @Select("SELECT * FROM msg_log WHERE msg_status = 0 AND now() >= next_retry_time")
    List<Message> selectTimeoutMessage();

}
