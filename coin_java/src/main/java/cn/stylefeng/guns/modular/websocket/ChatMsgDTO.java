package cn.stylefeng.guns.modular.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 消息模版
 * @author AnYuan
 */

@Data
public class ChatMsgDTO implements Serializable {
//    "date": "2020/05/20 23:19:07",
//            "text": { "text": msg },
//            "mine": true,
//            "name": "JwChat",
//            "img": "../image/three.jpeg"

    /**
     * 消息内容
     */
    private Text text;
    /**
     * 消息时间
     */
    private String date;
    private boolean mine;
    private String name;
    private Long memId;
    private String img;
    private String contentType;

    public ChatMsgDTO() {
        this.date = localDateTimeToString();
    }

    public ChatMsgDTO(String name, String text,String img,String contentType) {
        Text text1 = new Text();
        text1.setText(text);
        this.text = text1;
        this.name = name;
        this.img = img;
        this.contentType = contentType;
        this.date = localDateTimeToString();
    }


    /**
     * 获取当前时间
     * @return String 12:00:00
     */
    private String localDateTimeToString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTimeFormatter.format( LocalDateTime.now());
    }
}


