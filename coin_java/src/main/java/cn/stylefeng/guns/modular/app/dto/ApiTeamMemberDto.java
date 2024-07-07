package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ApiTeamMemberDto {
    /**
     * 手机号
     */
//    private String phone;
    /**
     * 账号
     */
    private String account;
    /**
     * 等级
     */
    private String type;
     /**
     * 等级
     */
    private String typeValue;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 时间戳
     */
    private long registerTimeStamp;

}
