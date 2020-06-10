package cn.cjz.constant;

/**
 * @author: Kam-Chou
 * @date: 2020/6/10 17:12
 * @description: 常量类
 * @version: 1.0
 */
public class Constant {

    public interface MessageStatus {

        /**
         * 投递中
         */
        Integer DELEVER_WAITING = 0;

        /**
         * 投递成功
         */
        Integer DELEVER_SUCCESS = 1;

        /**
         * 投递失败
         */
        Integer DELEVER_FAIL = 2;

        /**
         * 已消费
         */
        Integer CONSUMERED = 3;

    }

}
