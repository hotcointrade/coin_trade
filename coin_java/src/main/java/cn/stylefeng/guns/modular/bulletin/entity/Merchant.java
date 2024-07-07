package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户表
 */
@TableName("app_merchant")
@Data
@Accessors(chain = true)
public class Merchant extends BaseEntity {
    @TableId(value = "merchant_id",  type = IdType.ID_WORKER)
    private Long merchantId;

    @TableField("total_price")
    private BigDecimal totalPrice;
    @TableField("rate")
    private Double rate;

    @TableField("openid")
    private String openid;

    @TableField("head")
    private String head;

    @TableField("account")
    private String account;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("name")
    private String name;
    @TableField("merchant_name")
    private String merchantName;
    @TableField("birthday")
    private Date birthday;
    @TableField("sex")
    private String sex;
    @TableField("email")
    private String email;
    @TableField("phone")
    private String phone;

    @TableField("country")
    private String country;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("language")
    private String language;

    @TableField("level")
    private String level;
    @TableField("register_time")
    private Date registerTime;

    @TableField("last_login")
    private Date lastLogin;

    @TableField("status")
    private String status;
    @TableField("del_flag")
    private String delFlag;
    @TableField("remark")
    private String remark;
    @TableField("invite")
    private String invite;
    @TableField("msg")
    private String msg;
    //个人推广码
    @TableField("promotion")
    private String promotion;
    //提现密码
    @TableField("withdraw")
    private String withdraw;

}