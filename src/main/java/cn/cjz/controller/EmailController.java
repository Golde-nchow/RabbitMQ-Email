package cn.cjz.controller;

import cn.cjz.model.Mail;
import cn.cjz.model.MessageResponse;
import cn.cjz.service.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Kam-Chou
 * @date: 2020/6/11 10:47
 * @description: 暴露给外部的发送邮件地址
 * @version: 1.0
 */
@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    private MessageServiceImpl messageService;

    @PostMapping("send")
    public MessageResponse send(Mail mail, Errors errors) {
        if (errors.hasErrors()) {
            String msg = errors.getFieldError().getDefaultMessage();
            return MessageResponse.error(msg);
        }

        if (mail != null && mail.getTo() != null) {
            return MessageResponse.error("请把数据填写完整");
        }

        return messageService.send(mail);
    }

}
