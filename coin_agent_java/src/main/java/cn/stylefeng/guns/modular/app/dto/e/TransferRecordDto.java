package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Accessors(chain = true)
public class TransferRecordDto
{
    //类型
    private String type;
    //转账人
    private String from;
    //收款人
    private String to;
    //数量
    private BigDecimal price;
    //时间
    private Date time;
}
