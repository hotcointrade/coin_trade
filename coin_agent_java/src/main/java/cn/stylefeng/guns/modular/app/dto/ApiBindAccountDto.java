package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApiBindAccountDto {

    /**
     * 绑定账号
     */
    @NotBlank
    private String bindAccount;
    /**
     * 验证码
     */
    @NotBlank
    private String msg;
//    /**
//     * 短信验证码
//     */
//    @NotBlank
//    private String smsCode;

    /**
     * 登录密码
     */
//    @NotBlank
    private String pwd;

}
