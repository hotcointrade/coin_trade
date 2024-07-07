package cn.stylefeng.guns.modular.app.dto.otc;

import cn.stylefeng.guns.modular.otc.entity.Bill;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BillDto extends Bill
{

    private String buyAccount;
    private String sellAccount;
    //下单时间戳
    private Long createTimestamp;
    //付款时间戳
    private Long payTimestamp;
    //当前时间戳
    private Long now;

}
