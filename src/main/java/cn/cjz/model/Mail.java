package cn.cjz.model;

import lombok.Data;

/**
 * @author: Kam-Chou
 * @date: 2020/6/9 16:27
 * @description: 邮件类
 * @version: 1.0
 */
@Data
public class Mail {

    private String msgId;

    private String to;

    private String title;

    private String content;
}
