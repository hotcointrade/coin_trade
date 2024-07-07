package cn.stylefeng.guns.modular.app.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class ForgetVo {

    /**
     * 用户名
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "[a-zA-Z0-9]{6,16}",message = "密码格式有误")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Pattern(regexp = "[a-zA-Z0-9]{6,16}",message = "确认密码格式有误")
    private String confirmPwd;

//    @NotBlank(message = "验证码不为空")
    private String msg;

//    @NotBlank(message = "手机区号不为空")
//    private String code;


}
