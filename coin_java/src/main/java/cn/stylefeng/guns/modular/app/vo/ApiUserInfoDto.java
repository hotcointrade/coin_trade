package cn.stylefeng.guns.modular.app.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiUserInfoDto {

    /**
     * 账号
     */
    private String account;

    private String uid;

    /**
     * 会员身份
     */
    private String type;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请链接
     */
    private String inviteLink;

    /**
     * 头像
     */
    private String head;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

//    /**
//     * usdt
//     */
//    private String usdt;
//
//    /**
//     * RCC
//     */
//    private String rcc;
//
//    /**
//     * ETH
//     */
//    private String eth;
//   /**
//     * rt
//     */
//    private String rt;
//
//    /**
//     * 总资产
//     */
//    private BigDecimal totalPrice;
    /**
     * 手机号码属于0：国内 1：国外
     */
    private String area;


    private  String phoneCode;

    /**
     * 服务热线
     */
    private String contactPhone;

    /**
     * 客服邮箱
     */
    private String contactEmail;

    private String realStatus;

    //是否开通生活付费
    private String live;

    /**
     * 是否开启资产密码
     */
    private String isOpenPay;

    /**
     * 资产密码验证码方式
     */
    private String paySmsType;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    private String loginMethod;

}
