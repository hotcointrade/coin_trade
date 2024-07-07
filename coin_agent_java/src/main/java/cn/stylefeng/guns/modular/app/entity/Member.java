package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p> 用户信息 实体类 </p>
 */

@Data
@TableName("app_member")
@Accessors(chain = true)
@ToString
public class Member extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "member_id", type = IdType.AUTO)
    private Long memberId;

    /**
     * 账号
     */
    @TableField("account")
    private String account;


    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;


    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * uid
     */
    @TableField("uid")
    private String uid;

    /**
     * 用户类型(0:临时用户 1:会员 2:矿主 3：大矿主 4：超级矿主）
     */
    @TableField("type")
    private String type;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号码属于0：国内 1：国外
     */
    @TableField("area")
    private String area;


    @TableField("phone_code")
    private  String phoneCode;


    /**
     * 密码加盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 交易密码
     */
    @TableField("pay_password")
    private String payPassword;

    /**
     * 交易密码加盐
     */
    @TableField("pay_salt")
    private String paySalt;

    /**
     * 邀请码
     */
    @TableField("invite_code")
    private String inviteCode;

    /**
     * 用户关系链
     */
    @TableField("parent_referee_id")
    private String parentRefereeId;

    /**
     * 推荐人
     */
    @TableField("referee_id")
    private Long refereeId;


    /**
     * 助记词（备用）
     */
    @TableField("mnemonic")
    private String mnemonic;

    /**
     * 登录设备（备用）
     */
    @TableField("login_equipment")
    private String loginEquipment;

    /**
     * 累计收益（备用）
     */
    @TableField("mall_points")
    private java.math.BigDecimal mallPoints;

    /**
     * 算力剩余天数
     */
    @TableField("rest_day")
    private Long restDay;

    /**
     * 系统是否修改过用户类型0：否 1：是
     */
    @TableField("state")
    private String state;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 是否实名认证0：否 1：是 2 :审核中
     */
    @TableField("real_status")
    private String realStatus;

    /**
     * 区号 类型（EMAIL 、PHONE)
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 登录失败记录
     */
    @TableField("failure_sign")
    private Long failureSign;

    /**
     * 头像
     */
    @TableField("head")
    private String head;

    /**
     * 是否开通积分划转(0:否 1：是）
     */
    @TableField("points")
    private String points;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 最近一次登录
     */
    @TableField("last_login")
    private java.util.Date lastLogin;

    /**
     * 注册时间
     */
    @TableField("register_time")
    private java.util.Date registerTime;

    /**
     * 直推比例（备用）
     */
    @TableField("direct_rate")
    private String directRate;

    /**
     * 团队比例（备用）
     */
    @TableField("team_rate")
    private String teamRate;

    /**
     * 省
     */
    @TableField("province_id")
    private Long provinceId;

    /**
     * 市
     */
    @TableField("city_id")
    private Long cityId;

    /**
     * 区
     */
    @TableField("area_id")
    private Long areaId;

    /**
     * 是否小boss（0：否 1：是）
     */
    @TableField("small")
    private String small;

    /**
     * 是否大boss（0：否 1：是）
     */
    @TableField("big")
    private String big;


    /**
     * 是否理事（0：否 1：是
     */
    @TableField("director")
    private String director;

    /**
     * 是否暗箱账号(0:否 1：是）
     */
    @TableField("cooperation")
    private String cooperation;


    /**
     * 是否可以挂单（0：否 1：是） （承兑商）
     *
     *
     * 0.需要成为承兑商 1.已获得承兑商权限 2.承兑商申请审核中 3.承兑商审核失败
     */
    @TableField("otc")
    private String otc;

    /**
     * 是否申请过承兑商：0表示否，1表示申请过
     */
    @TableField("has_otc")
    private String hasOtc;

    /**
     * 提币手续费比例（%）
     */
    @TableField("withdraw")
    private String withdraw;

    /**
     * 保证金额度
     */
    @TableField("deposit")
    private String deposit;


    @TableField("btc")
    private String btc;

    @TableField("usdt")
    private String usdt;

    @TableField("eth")
    private String eth;

    @TableField("etc")
    private String etc;


    @TableField("xr")
    private String xr;

    @TableField("eos")
    private String eos;


    @TableField("bch")
    private String bch;


    @TableField("ltc")
    private String ltc;

    //代币
    @TableField("md")
    private String md;

    //代币
    @TableField("mge")
    private String mge;

    //代币
    @TableField("mgm")
    private String mgm;

    //代币
    @TableField("c1")
    private String c1;
    //代币
    @TableField("c2")
    private String c2;

    //代币
    @TableField("c3")
    private String c3;

    //代币
    @TableField("c4")
    private String c4;

    //代币
    @TableField("c5")
    private String c5;


    /**
     * 版本号
     */
    @TableField("version")
    private Long version;

    /**
     * 状态(字典)(启用-Y：禁用-N)
     */
    @TableField("status")
    private String status;

    /**
     * 删除标志(Y:删除 N:未删除）
     */
    @TableField("del")
    private String del;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 开通生活支付
     */
    @TableField("live")
    private String live;

    /**
     * 解锁赎回币种
     */
    @TableField("unlockc")
    private String unlockc;


    /**
     * 修改密码时间
     */
    @TableField("update_pwd_time")
    private Date updatePwdTime;

    /**
     * 是否开启资产密码：Y:是，N表示否
     */
    @TableField("is_open_pay")
    private String isOpenPay;

    /**
     * 登录方式：PHONE表示手机，EMAIL表示邮箱登录
     */
    @TableField("login_method")
    private String loginMethod;

}