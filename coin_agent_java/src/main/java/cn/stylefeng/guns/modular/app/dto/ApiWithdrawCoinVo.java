package cn.stylefeng.guns.modular.app.dto;

import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.extension.valid.EnumValid;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ApiWithdrawCoinVo {

    //地址
    @NotBlank(message = "地址不能为空")
    private String toAddress;

    //币种 --对应提币表字段
    @NotBlank
    private String type;

    //提币数量
    @NotNull(message = "提币数量不能为空")
    private BigDecimal price;

    //交易密码
    @NotBlank(message = "资产密码不能为空")
    private String payPwd;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 邮箱验证码
     */
    private String emailCode;

    /**
     * EOS币种的memo值或者XRP的Tag值
     */
    private String memoTagValue;

}
