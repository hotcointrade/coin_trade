package cn.stylefeng.guns.modular.app.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @description: 密码
 **/
@Data
public class ApiPayPwdVo {

    private String newPwd;

    private String confirmPwd;

    private String msgOrOldPwd;

    @NotBlank(message = "type不能为空")
    private String type;
}
