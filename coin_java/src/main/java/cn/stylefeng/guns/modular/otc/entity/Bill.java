package cn.stylefeng.guns.modular.otc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
 * <p> 交易结算订单 实体类 </p>
 *
 * @author yaying.liu
 * @since 2020-07-13 15:03:40
 */

@Data
@TableName("otc_bill")
@Accessors(chain = true)
public class Bill extends BaseEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    /**
     * 交易结算订单
     */
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Long billId;

    /**
     * 操作挂单id
     */
    @TableField("opid")
    private Long opid;

    /**
     * 类型（BUY-购买单 SELL-出售单）
     */
    @TableField("type")
    private String type;


    /**
     * 买方
     */
    @TableField("buy_mid")
    private Long buyMid;

    /**
     * 买方昵称
     */
    @TableField("buy_name")
    private String buyName;

    /**
     * 卖方
     */
    @TableField("sell_mid")
    private Long sellMid;

    /**
     * 卖方昵称
     */
    @TableField("sell_name")
    private String sellName;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 参考号
     */
    @TableField("mark_no")
    private String markNo;

    /**
     * 交易币种
     */
    @TableField("coin")
    private String coin;

    /**
     * 交易单价
     */
    @TableField("unit_price")
    private java.math.BigDecimal unitPrice;

    /**
     * 交易数量
     */
    @TableField("number")
    private java.math.BigDecimal number;

    /**
     * 交易金额
     */
    @TableField("cny")
    private java.math.BigDecimal cny;

    /**
     * 交易方式
     */
    @TableField("pay_method")
    private String payMethod;


    /**
     * 收款账号
     */
    @TableField("pay_account")
    private String payAccount;


    @TableField("pay_name")
    private String payName;

    @TableField("pay_img")
    private String payImg;


    @TableField("bank")
    private String bank;

    @TableField("branch")
    private String branch;


    /**
     * 付款时间
     */
    @TableField("pay_time")
    private java.util.Date payTime;
    /**
     * 取消时间
     */
    @TableField("cancel_time")
    private java.util.Date cancelTime;

    /**
     * 版本号
     */
    @TableField("version")
    @Version
    private Long version;

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

    //完成时间
    @TableField("finish_time")
    private java.util.Date finishTime;

    //申诉方（BUY-买方 SELL-卖方）
    @TableField("appeal_type")
    private String appealType;

    //申诉内容
    @TableField("content")
    private String content;
    //申诉凭证
    @TableField("img")
    private String img;


     //申诉内容
    @TableField("content1")
    private String content1;
    //申诉凭证
    @TableField("img1")
    private String img1;



    //    责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
    @TableField("duty")
    private String duty;
    //    申诉状态（待处理，已取消，已放币）
    @TableField("appeal_status")
    private String appealStatus;
//    申诉时间
    @TableField("atime")
    private java.util.Date atime;

  @TableField("atime1")
    private java.util.Date atime1;

  @TableField("upload_status")
    private String uploadStatus;
  @TableField("upload_img")
    private String uploadImg;



}
