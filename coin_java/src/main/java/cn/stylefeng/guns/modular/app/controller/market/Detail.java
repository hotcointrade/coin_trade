package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
public class Detail
{
    private long id;
    //以基础币种计量的交易量（以滚动24小时计
    private float amount;
    //交易次数（以滚动24小时计
    private float count;
//    本阶段开盘价（以滚动24小时计）
    private float open;
    //本阶段最高价（以滚动24小时计）
    private float high;
    //本阶段最低价（以滚动24小时计）
    private float low;
    //本阶段最新价（以滚动24小时计）
    private float close;
    //以报价币种计量的交易量（以滚动24小时计
    private float vol;

    //拓展参数 交易对
    private String symbol;
    //拓展参数 涨跌幅
    private float rose;
    //拓展参数 人民币
    private BigDecimal cny;
    //
    private long version;
    
    private String img;
}
