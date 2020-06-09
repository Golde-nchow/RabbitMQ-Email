package cn.cjz.dao;

import cn.cjz.model.Message;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author: Kam-Chou
 * @date: 2020/6/9 15:49
 * @description: 消息Mapper类
 * @version: 1.0
 */
@Component
public interface MessageMapper extends Mapper<Message> {
}
