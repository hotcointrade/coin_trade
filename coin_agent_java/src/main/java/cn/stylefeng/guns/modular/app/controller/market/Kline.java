package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Kline
{


    //时间戳
    private long id;
    private float amount;
    private int count;
    private float open;
    private float close;
    private float low;
    private float high;
    private float vol;
    private float rose;
    private Integer number;
    private BigDecimal cny;
    private String symbol;

    //分时
    private String period;
    //配合页面图标
    private String img;

    public Kline(){
        this.open=0;
        this.close=0;
        this.high=0;
        this.low=0;
        this.id=0;
        this.number = 8;
    }

    /**
     * 初始化
     * @param token
     * @return
     */
    public static Kline init(String token)
    {
        Kline entity = new Kline();
        entity.setSymbol(token)
                .setId(System.currentTimeMillis()/1000)
                .setClose(0.01F)
                 .setOpen(0.01F)
                 .setHigh(0.01F)
                .setNumber(8)
                  .setLow(0.01F);
        return entity;
    }

}
