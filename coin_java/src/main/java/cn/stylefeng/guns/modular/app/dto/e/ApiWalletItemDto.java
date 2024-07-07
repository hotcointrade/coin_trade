package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 账户明细
 */
@Data
@Accessors(chain = true)
public class ApiWalletItemDto
{
    /**
     * 参数1
     */
    private String p1;
    private String p2;
    private String p3;
    private String p4;
    private String p5;
    private String p6;
    private String p7="--";

    private String page;
    private String type;


}


