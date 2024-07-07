package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class Merged {

    private long id;
    private long ts;
    private float close;
    private float open;
    private float high;
    private float low;
    private float amount;
    private float count;
    private float vol;
    private List<BigDecimal> ask;
    private List<BigDecimal> bid;

    private float rose;
    //拓展参数 人民币
    private BigDecimal cny;
    //币种
    private String coin;
    private String symbol;
}
