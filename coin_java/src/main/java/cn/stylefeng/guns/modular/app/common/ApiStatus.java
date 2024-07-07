package cn.stylefeng.guns.modular.app.common;

/**
 * API返回状态码
 */
public enum ApiStatus {


    /**
     * 常规状态
     */
    ERROR(100, "操作失败")
    ,OK(200,"成功")
    ,BAD_REQUEST(400, "该信息已处理")
    ,BAD_REQUEST_OPTION(400, "只能使用USDT与期权进行划转")
    ,NOT_FOUND(404,"未找到")
    ,SERVER_ERROR(500,"服务器错误")
    ,OVER_ROLE(1001,"越权操作")

    /**
     * 校验类错误
     */
    ,MSG_ERROR(2001,"验证码有误")
    ,ERROR_PAY_PWD(2002,"交易密码错误")
    ,PAY_PWD_EMPTY(2003,"交易密码为空,请设置交易密码")
    ,NOT_REAL(2004,"未实名认证")
    ,REAL_CHECK(200,"成功")
    ,WALLET_LESS(2005,"钱包余额不足")
    ,OLD_ERROR(2006,"旧密码有误")
    ,NOT_DIRECTOR(2007,"未找到推荐人")
    ,DIFF_PWD(2008,"两次密码不同")
    ,ERROR_LOGIN_PWD(2009,"登录密码有误")
    ,VERIFY(2010,"账号或密码错误")
    ,NOT_FIND_USER(2011,"用户未找到")
    ,EXIST_ACCOUNT(2012,"账号已存在")
    ,EXIST_BIND_ACCOUNT_WITH_EMAIL(2013,"该邮箱号码已被绑定")
    ,EXIST_BIND_ACCOUNT_WITH_PHONE(2014,"该手机号码已被绑定")
    ,NOT_API_OPEN(2015,"暂未开放")
    ,DISABLE_ACCOUNT(2016,"账号被禁用")
    ,VERIFY_ID_CARD(2017,"需要实名认证才可进行法币交易")
    ,PAY_METHOD_ERROR(2018,"只可选择指定收付款方式")
    ,EXIST_NICK_NAME(2019,"昵称已被使用")
    ,NOT_NICK_NAME(2020,"请先设置昵称")
    ,NO_MONEY(2021,"爆仓风险过高，无法下单！！请提前增加保证金")
    ,FEE_LESS(2022,"提币量不足扣除手续费")
    ,MAX_STOP_PROFIT(2023,"止盈价要高于成交价")
    ,MIN_STOP_LOSS(2024,"止损价要低于成交价")
    ,MIN_STOP_PROFIT(2025,"止盈价要低于成交价")
    ,MAX_STOP_LOSS(2026,"止损价要高于成交价")
    ,NOT_TRAND(2027,"转账数量不足扣除手续费")
    ,POS_NOT_CVT(2028,"当前合约存在持仓，无法划转")
    ,NO_ORDER_MONEY(2029,"配资不足")
    ,NO_NULL_OLD_PWD(2030,"旧密码不能为空")
    ,NO_NULL_NEW_PWD(2031,"新密码不能为空")
    ,NO_NULL_CONFIRM_PWD(2032,"确认密码不能为空")
    ,NO_NULL_PAY_PWD(2033,"资产密码不能为空")
    ,NOT_NULL_NICK_NAME(2034,"昵称不能为空")
    ,NOT_EXIT_NICK_NAME(2035,"该昵称已存在")
    ,NOT_EXIT_PAY_ACCOUNT(2036,"该收款账号已存在")
    ,NOT_EXIT_CARD_ACCOUNT(2037,"该银行卡号已存在")
    ,NOT_NULL_BANK(2038,"开户银行不能为空")
    ,NOT_NULL_SMS_CODE(2039,"手机短信验证码不能空")
    ,NOT_NULL_EMAIL_CODE(2040,"邮箱验证码不能为空")
    ,NOT_SETTING_PAYPWD(2041,"请设置资产密码")
    ,NOT_BING_PHONE(2042,"请绑定手机号码")
    ,NOT_BING_EMAIL(2043,"请绑定邮箱号")
    ,ERROR_EMAIL_CODE(2044,"邮箱验证码有误")
    ,ERROR_SMS_CODE(2045,"手机验证码有误")
    ,NO_TO_MYSELF(2046,"输入的提币地址不能自己充币地址")
    ,EXIT_APPEAL_OTC(2047,"您已提交过申请")
    ,NO_TAD_OTC(2048,"您已提交过申请")
    ,ADD_OTC_MONEY(2049,"需要补缴保证金")
    ,BUY_UNIT_MAX(2050,"委托价格不能小于当前行情价")
    ,SELL_UNIT_MAX(2051,"委托价格不能大于当前行情价")
    ,EXIT_NUMBER_NO(2052,"平仓手数不能大于当前持仓剩余手数")
    ,MIN_ZERO_NUMBER(2053,"平仓手数不能小于等于0")
    ,NO_NULL_QRCODE(2054,"区号不能为空")
    ,EXIST_REAL(2055,"用户已认证")
    ,MINING_ERROR(2056,"矿机不存在")
    ,MINING_FUEL_UNENOUGH(2057,"矿机燃料不足")
    ,MINING_PRI_UNENOUGH(2058,"矿机合约价格不足")
    ,MINING_NUM_UNENOUGH(2059,"数量已达限购次数")
    ,MINING_NUM_ERROR(2060,"矿机数量不足")
    ,CHECK_TODAY_ERROR(2061,"今日已签到,无需重复签到")
    ,ERROR_CHAT_PWD(2062,"聊天室密码错误")
    ,ERROR_CHAT_NO(2063,"暂时无法参与群聊")
    ,ERROR_ID_CARD_ONE(2064,"身份证号码重复使用")


    /**
     * 钱包类错误
     */
    ,MIN_WITHDRAW_NUM(3001,"低于最小提币量")
    ,EXIST_APPLY(3002,"您已发起申请，请耐心等待")
    ,COIN_ADDRESS_ERROR(3003,"地址有误")
    ,MIN_LEGAL_NUM(3004,"低于最小购买金额")

    /**
     * 业务类错误
     */
     ,NOT_OP_MINE(4001,"不能指定自己")
     ,MATCHING(4002,"撮合交易中，不可撤单")
     ,CANCEL_FAIL(4003,"订单无法撤销")
     ,NOT_OUT_CONTRACT(4004,"该订单暂时不能平仓")
    ,NOT_UP_LOSS(4005,"无法设置高于当前行情价的多仓止损价")
    ,NOT_DOWN_LOSS(4006,"无法设置低于当前行情价的空仓止损价")
    ,CLOSE_LIVE(4007,"请联系客服处理")
    ,BACK_CHECK(4008,"已有审核单")
    ,EXIST_OTC_ORDER(4009,"尚有订单未处理完毕")
    ,BACK_CHECK_EXIST(4010,"正在退还押金，无法补缴")
    ,LIMIT_DONT(4011,"今日三次取消%s分钟内无法继续购买")
    ,LIMIT_DONT1(4012,"%s分钟内无法继续购买")
    ,MANY_OP(4013,"操作繁忙！请稍后重试")
    ,OPENED_LIVE(4014,"请联系客服处理")
    ,LOCK_USDT_LOW(4015,"请联系客服处理")
    ,MIN_BUY_NUMBER(4016,"不能小于最小委托量")
    ,EXIT_FINISH_ORDER(4017,"该订单已完成，请回退到法币交易")
    ,ERROR_MESSAGE(4018,"发送失败")
    ,LOOK_UP_OR_DOWN(4019,"看涨为0看跌为1")
    ,OPTION_DOWN_NOT_CARE(4020,"该期权已关闭，禁止投注")
    ,THAT_OPTION_EN_END(4021,"本期奖池已满，请稍后")
    ,THAT_COIN_DOWN(4022,"该币种已关闭")
    ,COIN_NOT_MATCHING(4023,"币种不匹配")
    ,BET_AMOUNT_OUT_OF_RANGE(4024,"投注金额超出范围")
    ,OPTION_PARTICIPATED(4025,"已参与过该期")
    ,MAXIMUM_AMOUNT_EXCEEDED(4026,"超出最大金额")
    ,MINIMUM_AMOUNT_NOT_REACHED(4027,"未达到最小金额")
    ,EXCEEDING_THE_MAXIMUM_SUBSCRIPTION_AMOUNT(4028,"超出最大认购金额")
    ,THE_MINIMUM_SUBSCRIPTION_AMOUNT_IS_NOT_REACHED(4029,"未达到最低认购金额")
    ,MINING_ON_START(4030,"矿机已启动或已结束")
    ,MINING_NOT_END(4031,"矿机未结束")
    ,MINING_UNIT_OUT(4032,"已领取过矿机收益")

    /**
     * 业务类提醒
     */
    ,MANY_CANCEL(21001,"您今日已取消2次，如再次取消，%s分钟内无法下单购买，是否继续取消")
    ,MANY_OTC_CANCEL(21002,"您今日已取消2次，如再次取消，将扣除您50%保证金，是否继续取消")


    ,UPDATE_ORDER(5021,"订单发生了更新，请重新下单！")
    ,NOT_WITHDRAW(5022,"修改过密码，24小时无法提币")
    ,NOT_MYSELF(5023,"不可下单自己发布的广告！")

    /**
     * 交易正常状态
     */
    ,OK_CURRENCY(200,"成功")
    ;

    private int code;

    private String msg;


    ApiStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Return the integer value of this status code.
     */
    public int code() {
        return this.code;
    }

    /**
     * Return the integer value of this status code.
     */
    public String msg() {
        return this.msg;
    }

}
