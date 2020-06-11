package cn.cjz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: Kam-Chou
 * @date: 2020/6/9 15:52
 * @description: SpringBoot启动类
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan(value = "cn.cjz.dao")
public class RabbitMqEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqEmailApplication.class, args);
    }


}
