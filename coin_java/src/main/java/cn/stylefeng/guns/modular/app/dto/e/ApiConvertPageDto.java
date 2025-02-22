package cn.stylefeng.guns.modular.app.dto.e;


import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.extension.valid.EnumValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 划转页面入参
 */
@Data
public class ApiConvertPageDto
{
    //币种
    @NotBlank
    private String type;
    //从
    @Pattern(regexp = "^(WALLET|CONTRACT|CURRENCY|LEGAL|OPTION|FUTURES|MINING)$",message = "钱包类型有误")
    @NotBlank
    private String from;


//    //到
//    @Pattern(regexp = "^(WALLET|CONTRACT|CURRENCY|LEGAL)$",message = "钱包类型有误")
//    @NotBlank
//    private String to;


}
