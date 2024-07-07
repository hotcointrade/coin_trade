package cn.stylefeng.guns.modular.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Text implements Serializable {
    private String text;
}
