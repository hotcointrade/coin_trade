package cn.stylefeng.guns.modular.mining.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 矿机 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-06-07 13:24:41
 */

@Data
@TableName("mining")
@Accessors(chain = true)
public class Mining extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 矿机id
      */
     @TableId(value="id", type= IdType.AUTO)
     private Long id;

     /**
      * 矿机类型（0：一般矿机）
      */
     @TableField("type")
     private String type;

     /**
     * 矿机图片
     */
    @TableField("image")
    private String image;


    /**
     * 矿机图片
     */
    @TableField("image2")
    private String image2;

     /**
      * 矿机标题
      */
     @TableField("title")
     private String title;

     /**
      * 矿机名称
      */
     @TableField("name")
     private String name;

     /**
      * 简介
      */
     @TableField("introduction")
     private String introduction;

     /**
      * 预计产能 150 %
      */
     @TableField("estimated_capacity")
     private java.math.BigDecimal estimatedCapacity;

     /**
      * 预计静态化收益率
      */
     @TableField("estimated_static_yield")
     private java.math.BigDecimal estimatedStaticYield;

     /**
      * 周期类型 1001年 月1002 日1003
      */
     @TableField("cycle_type")
     private String cycleType;

     /**
      * 周期量
      */
     @TableField("cycle_number")
     private Integer cycleNumber;

     /**
      * 燃料能源币种
      */
     @TableField("fuel_energy_coin")
     private String fuelEnergyCoin;

     /**
      * 燃料能源数量
      */
     @TableField("fuel_energy_number")
     private java.math.BigDecimal fuelEnergyNumber;

     /**
      * 能源损耗币种
      */
     @TableField("energy_loss_coin")
     private String energyLossCoin;

     /**
      * 能源损耗数量
      */
     @TableField("energy_loss_number")
     private java.math.BigDecimal energyLossNumber;

     /**
      * 算力币种
      */
     @TableField("calculating_power_coin")
     private String calculatingPowerCoin;

     /**
      * 算力数量
      */
     @TableField("calculating_power_number")
     private java.math.BigDecimal calculatingPowerNumber;

     /**
      * 合约价格币种
      */
     @TableField("price_coin")
     private String priceCoin;

     /**
      * 合约价格数量
      */
     @TableField("price")
     private java.math.BigDecimal price;

     /**
      * 进度：0：未开始  1：进行中  2：派发中  3：已结束
      */
     @TableField("step")
     private String step;

     /**
      * 开始时间
      */
     @TableField("start_time")
     private String startTime;

     /**
      * 结束时间
      */
     @TableField("end_time")
     private String endTime;

     /**
      * 活动类型(0:未知，1：首发抢购，2：首发分摊，3：持仓瓜分，4：自由认购，5：云矿机， 6：锁仓释放）
      */
     @TableField("mining_type")
     private Integer miningType;

     /**
      * 进度值（例98 = 98%）,type=4（认购活动）时有效
      */
     @TableField("progress")
     private Integer progress;

     /**
      * 发行总量
      */
     @TableField("total_supply")
     private java.math.BigDecimal totalSupply;

     /**
      * 已售总量
      */
     @TableField("traded_amount")
     private java.math.BigDecimal tradedAmount;

     /**
      * 价格精度
      */
     @TableField("price_scale")
     private java.math.BigDecimal priceScale;

     /**
      * 数量精度
      */
     @TableField("number_scale")
     private java.math.BigDecimal numberScale;

     /**
      * 限购次数
      */
     @TableField("limit_times")
     private Integer limitTimes;

     /**
      * 产能币种
      */
     @TableField("mining_coin")
     private String miningCoin;

     /**
      * 静态化产能币种
      */
     @TableField("static_coin")
     private String staticCoin;

     /**
      * 释放类型 0：等额释放，1：等比释放
      */
     @TableField("release_type")
     private Integer releaseType;

     /**
      * 产出多少
      */
     @TableField("mining_daysprofit")
     private java.math.BigDecimal miningDaysprofit;

     /**
      * 邀请好友（且购买矿机/锁仓）产能增加百分比（为0则不增加）
      */
     @TableField("mining_invite")
     private java.math.BigDecimal miningInvite;

     /**
      * 产能增加上限百分比（为0则无上限）
      */
     @TableField("mining_invitelimit")
     private java.math.BigDecimal miningInvitelimit;

     /**
      * 启用禁用 yn
      */
     @TableField("status")
     private String status;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;

     /**
      * 备注
      */
     @TableField("remark")
     private String remark;




 }
