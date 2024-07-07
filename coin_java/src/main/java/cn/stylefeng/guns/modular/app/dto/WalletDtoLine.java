package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class WalletDtoLine {
    private BigDecimal price;
    private Date time;
    private Long stamp;
}
