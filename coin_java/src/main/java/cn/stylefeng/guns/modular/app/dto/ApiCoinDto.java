package cn.stylefeng.guns.modular.app.dto;

import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.extension.valid.EnumValid;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class ApiCoinDto {
    private Long chainCoinId;
    private String remark;
    private String address;
    private String coin;
    //操作：1：加  0：删
    private int op;
}
