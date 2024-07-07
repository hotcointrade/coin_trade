package cn.stylefeng.guns.modular.mining.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 矿机订单 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-06-08 21:02:12
 */

@Data
@TableName("mining_order")
@Accessors(chain = true)
public class MiningOrder extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 矿机订单
      */
     @TableId(value="mining_order_id", type= IdType.AUTO)
     private Long miningOrderId;

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
      * 活动人
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 矿机
      */
     @TableField("mining_id")
     private Long miningId;

     /**
      * 矿机名称
      */
     @TableField("name")
     private String name;

     /**
      * 周期量
      */
     @TableField("cycle_number")
     private Integer cycleNumber;

     /**
      * 周期类型 1001年 月1002 日1003
      */
     @TableField("cycle_type")
     private String cycleType;

     /**
      * 挖矿状态（0：待启动，1：挖矿中，2：已结束,3,带启动，）3是启动过的
      */
     @TableField("mining_status")
     private String miningStatus;

     /**
      * 产能币种
      */
     @TableField("mining_coin")
     private String miningCoin;

     /**
      * 静态收益币种
      */
     @TableField("static_coin")
     private String staticCoin;

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
      * 持续天数 持续天数+已挖矿天数 >= 周期量
      */
     @TableField("mining_days")
     private Integer miningDays;

     /**
      * 已挖矿天数
      */
     @TableField("mininged_days")
     private Integer miningedDays;

     /**
      * 原始日产出
      */
     @TableField("mining_daysprofit")
     private java.math.BigDecimal miningDaysprofit;

     /**
      * 当前日产出
      */
     @TableField("current_daysprofit")
     private java.math.BigDecimal currentDaysprofit;

     /**
      * 总产出 已领
      */
     @TableField("total_profit")
     private java.math.BigDecimal totalProfit;

     /**
      * 产能收益
      */
     @TableField("received")
     private java.math.BigDecimal received;

     /**
      * 静态化收益
      */
     @TableField("available")
     private java.math.BigDecimal available;

     //带扣能源
     @TableField("energy_to_be_consumed")
     private java.math.BigDecimal energyToBeConsumed;

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
      * 结束时间
      */
     @TableField("end_time")
     private String endTime;

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

     /**
      * 只用于前端页面 不用于任何逻辑 0609
      * 表示当前属性不是数据库的字段，但在项目中必须使用
      */
     @TableField(exist = false)
     private List<MiningOrderDetail> miningOrderDetail;




 }
