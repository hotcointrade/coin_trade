package cn.stylefeng.guns.modular.app.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登录vo
 */
@Data
public class LoginVo {

    /**
     * 用户名
     */
    @NotBlank(message = "账号不能为空")
//    @Pattern(regexp = "[0-9]{2,11}",message = "账号格式有误")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "[a-zA-Z0-9]{6,16}",message = "密码格式有误")
    private String password;

//    @NotBlank(message = "区号不能为空")
//    private String code;
}
