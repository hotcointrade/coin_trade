package cn.stylefeng.guns.modular.app.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class ApiTransVo
{
    /**
     * 资产类型
     */
    @NotBlank
    @Pattern(regexp="^RCC|^TICKET$",message = "资产类型有误: ^RCC|^TICKET$ ")
    private String walletType;
    /**
     * 交易类型
     */
    @Pattern(regexp="^BUY|^SELL|^ENTRUST|^ORDER$",message = "交易类型有误： ^BUY|^SELL|^ENTRUST|^ORDER$")
    private String transType;

    private String payPwd;

    private Long sellId;

    @NotBlank
//    @Pattern(regexp = "^TRANS|^FINISH|^CANCEL$",message = "状态有误：^TRANS|^FINISH|^CANCEL$")
    private String status;
}
