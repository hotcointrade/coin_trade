package cn.stylefeng.guns.modular.app.controller.market.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * k线请求dto
 */
@Data
@Accessors(chain = true)
public class KlineDto
{
    /**
     * 	币种
     */
    @NotBlank
    private String symbol;
    /**
     * 分时
     */
    @NotBlank
    private String period;
    /**
     * 记录数
     */
    @NotNull
    private Integer size;

    //类型 现货-0  期货（永续-1  当周-2 次周-3 季度-4）
    private int type;

}
