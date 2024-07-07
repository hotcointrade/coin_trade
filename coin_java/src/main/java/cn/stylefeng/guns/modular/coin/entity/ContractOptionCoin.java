package cn.stylefeng.guns.modular.coin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ContractOptionCoin {

    private Long id;
    @NotNull(message = "交易对不能为空")
    private String symbol;//交易币种名称，格式：BTC/USDT
    private String name; // 合约名称（如：BTC/USDT永续合约）
    private String coinSymbol;//交易币种符号，如BTC
    private String baseSymbol;//结算币种符号，如USDT

    private Integer maxOptionNo;//最新期号

    private Integer sort;//排序，从小到大
    private Integer coinScale;//交易币小数精度
    private Integer baseCoinScale;//基币小数精度

    private Integer enable;//状态：1启用 0禁止

    private Integer visible;//前台可见状态，1：可见，0：不可见

    private String ctrlUp ;// 控制涨 N 否 Y 是
    private String ctrlDown ;// 控制跌 N 否 Y 是

    private Integer tiedType;//平局处理：1退回资金 0平台通吃

    private Integer enableBuy ;//允许看涨 1允许 0不允许

    private Integer  enableSell ;//允许看跌 1允许 0不允许

    private String amount ;// 允许投注数(默认：10,20,50,100,200,500,1000)

    private BigDecimal oods;// 赔率(默认100)
    private BigDecimal oous;// 赔率(默认100)
    private BigDecimal rods;// 返佣率(默认100)
    private BigDecimal rodsTow;// 二级返佣率(默认100)


    private BigDecimal feePercent;// 开仓手续费(默认0)

    private BigDecimal winFeePercent;// 赢家手续费(默认千分之一)

    private BigDecimal ngnorePercent;// 忽视涨跌幅度（如设置为0.005，则涨跌幅度小于0.5%的会被视为平局）

    private BigDecimal initBuyReward;// 初始买涨奖池金额


    private BigDecimal initSellReward;// 初始买跌奖池金额

    private BigDecimal totalProfit;// 期权合约总盈利

    private Integer openTimeGap;// 开始到开盘时间间隔（单位：秒， 默认5分钟）

    private Integer closeTimeGap;// 开盘到收盘时间间隔（单位：秒， 默认5分钟）

    private String secondsYield; //不同结算周期对赢的比例(30秒赢了就是40%),例如30/0.4,60/0.5,120/0.7,300/1,另外一个版本用到的字段

    // openTimeGap + closeTimeGap就是一期期权合约的总时间周期
    // 默认为：一期开始后，5分钟内下单，然后停止下单，再等待5分钟出结果。出结果期间不允许下单。

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;//创建时间

    /**
     * 服务器当前市价戳
     */
    @TableField(exist = false)
    private Long currentTime;
    /**
     * 当前币币价
     */
    @TableField(exist = false)
    private float rose;
}
