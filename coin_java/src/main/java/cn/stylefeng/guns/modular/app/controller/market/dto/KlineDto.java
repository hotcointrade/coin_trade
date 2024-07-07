package cn.stylefeng.guns.modular.app.controller.market.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
     * 每页大小
     */
    private Integer size;
    
    /**
     * 当前时间戳，毫秒值，例：1711786380
     */
    private Long ts;

    //类型 现货-0  永续-1 期货（ 当周-2 次周-3 季度-4）
    private int type;

}
