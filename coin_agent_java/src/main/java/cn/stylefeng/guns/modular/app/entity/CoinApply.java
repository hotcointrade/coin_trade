package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 上币申请 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-02-20 20:37:01
 */

@Data
@TableName("app_coin_apply")
@Accessors(chain = true)
public class CoinApply extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 申请id
      */
     @TableId(value="coin_apply_id", type= IdType.AUTO)
     private Long coinApplyId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 账户
      */
     @TableField("account")
     private String account;

     /**
      * 币种全称
      */
     @TableField("currency_zh_name")
     private String currencyZhName;

     /**
      * 英文全称
      */
     @TableField("currency_en_name")
     private String currencyEnName;

     /**
      * 货币英文缩写
      */
     @TableField("currency_abbr_name")
     private String currencyAbbrName;

     /**
      * 市场流通量
      */
     @TableField("market_float")
     private String marketFloat;

     /**
      * 官方持有量
      */
     @TableField("official_holdings")
     private String officialHoldings;

     /**
      * 官方地址
      */
     @TableField("official_address")
     private String officialAddress;

     /**
      * 区块浏览器
      */
     @TableField("block_browser")
     private String blockBrowser;

     /**
      * 活动区域
      */
     @TableField("movement_area")
     private String movementArea;

     /**
      * 白皮书地址
      */
     @TableField("White_paper_address")
     private String whitePaperAddress;

     /**
      * 已上线交易所
      */
     @TableField("listed_exchange")
     private String listedExchange;

     /**
      * 项目领域
      */
     @TableField("project_areas")
     private String projectAreas;

     /**
      * 功能解释
      */
     @TableField("functional_interpretation")
     private String functionalInterpretation;

     /**
      * 程序地址
      */
     @TableField("project_address")
     private String projectAddress;

     /**
      * 地址方式
      */
     @TableField("address_way")
     private String addressWay;

     /**
      * 状态(字典)
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