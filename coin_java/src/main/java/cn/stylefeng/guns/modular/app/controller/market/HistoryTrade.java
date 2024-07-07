package cn.stylefeng.guns.modular.app.controller.market;

import lombok.Data;

@Data
public class HistoryTrade {
    /**
     * id : 17592256642623
     * amount : 0.04
     * price : 1997
     * direction : buy
     * ts : 1502448920106
     */

    private Long id;
    private Long ts;
    private double amount;
    private double price;
    private String direction;
}
