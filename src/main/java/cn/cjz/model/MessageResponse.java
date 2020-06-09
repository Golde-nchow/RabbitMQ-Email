package cn.cjz.model;

import cn.hutool.json.JSONUtil;
import lombok.Builder;
import lombok.Data;

/**
 * @author: Kam-Chou
 * @date: 2020/6/9 16:22
 * @description: 响应信息
 * @version: 1.0
 */
@Data
@Builder
public class MessageResponse {

    private Integer status;

    private String message;

    private String data;

    public static String success() {
        MessageResponse success = MessageResponse
                .builder()
                .status(1)
                .message("成功")
                .data(null)
                .build();

        return JSONUtil.parse(success).toStringPretty();
    }
}
