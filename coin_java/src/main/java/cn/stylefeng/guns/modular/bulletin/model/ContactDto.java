package cn.stylefeng.guns.modular.bulletin.model;


import cn.stylefeng.guns.modular.bulletin.entity.Contact;
import lombok.Data;

/**
 * 客服 传输bean
 */
@Data
public class ContactDto extends Contact {

    private String startTime;
    private String endTime;
}
