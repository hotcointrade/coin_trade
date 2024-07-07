package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 收款方式DTO
 *
 */
@Data
public class ApiPayDto {

    private Long paymentId;

    private String name;
    @NotBlank
    private String idcard;
//    @NotBlank
    private String img;

    private String bank;

    private String branch;

    //上传收款方式类型
    @Pattern(regexp = "^(ALI_PAY|WE_CHAT|BANK|PAYPAL)$",message = "上传收款方式类型有误：^(ALI_PAY|WE_CHAT|BANK|PAYPAL)$")
    private String type;


}
