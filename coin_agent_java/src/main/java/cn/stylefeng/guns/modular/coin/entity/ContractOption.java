package cn.stylefeng.guns.modular.coin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 期权合约每一期
 */
@Data
public class ContractOption {

    private Long id;

    private Integer optionNo; // 合约序号（如第1期，第2期，第3期 中的 1，2，3）

    private String symbol;//交易币种名称，格式：BTC/USDT

    private BigDecimal openPrice;// 开盘价格
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long openTime;//开盘时间

    private BigDecimal closePrice;// 收盘价格
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long closeTime;// 收盘时间

    private BigDecimal totalBuy;// 买涨奖池总金额

    private BigDecimal initBuy;// 初始化金额

    private Integer totalBuyCount;// 买涨人数

    private BigDecimal totalSell;// 买跌奖池总金额

    private BigDecimal initSell;// 初始化卖金额

    private Integer totalSellCount;// 买跌人数

    private Integer status; // 0投注中 1开奖中  2开奖结束 3撤销

    private Integer result; // 0待开奖 1涨  2跌  3平 4撤销

    private BigDecimal totalPl;// 平台盈利（如平局、手续费等）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;//创建时间

    //预设收盘价格
    private BigDecimal presetPrice;
}
