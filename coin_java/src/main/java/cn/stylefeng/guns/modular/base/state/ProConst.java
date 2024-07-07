package cn.stylefeng.guns.modular.base.state;

import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.base.util.Result;

/***
 * 业务常量
 */
public class ProConst extends Result {
	
	
	/**
	 * K线周期  1min, 5min, 30min, 60min, 4hour, 1day, 1week
	 * //       1  5 30 1, 4,   1天, 1周
	 * int[] time = {1, 5, 30, 60, 240, 1440, 10080};
	 */
	public enum PeriodType {
		min_1("1min", 1),
		min_5("5min", 5),
		min_15("15min", 15),
		min_30("30min", 30),
		min_60("60min", 60),
		hour_4("4hour", 240),
		day_1("1day", 1440),
		mon_1("1mon", 1),
		week_1("1week", 10080),
		year_1("1year", 1),
		;
		public String code;
		public int min;
		
		PeriodType(String code, int min) {
			this.code = code;
			this.min = min;
		}
		
	}
	
	/**
	 * 由于redis 中文会乱码
	 */
	public enum FuturesType {
		BOJIN("BOJIN", "铂金期货"),
		PALLADIUM("PALLADIUM", "钯金期货"),
		LONDONGOLD("LONDONGOLD", "伦敦金"),
		
		;
		public String code;
		public String zh;
		
		FuturesType(String code, String zh) {
			this.code = code;
			this.zh = zh;
		}
		
		public static String getCode(String zh) {
			FuturesType[] values = FuturesType.values();
			for (FuturesType value : values) {
				if (value.zh.equals(zh)) {
					return value.code;
				}
			}
			return null;
		}
		
		public static String getZh(String code) {
			FuturesType[] values = FuturesType.values();
			for (FuturesType value : values) {
				if (value.code.equals(code))
					return value.zh;
			}
			return null;
		}
		
		public static String getZhString(String code) {
			FuturesType[] values = FuturesType.values();
			for (FuturesType value : values) {
				if (value.code.equals(code))
					return value.zh + "/" + value.code;
			}
			return null;
		}
		
	}
	
	/**
	 * 轮播图类型
	 */
	public enum CarouselType {
		HOME_TOP("HOME_TOP", "首页"), HOME_DOWN("DAPP", "DAPP");
		private String code;
		private String value;
		
		CarouselType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
		
	}
	
	/**
	 * 账号类型
	 */
	public enum AccountType {
		EMAIL("EMAIL", "邮箱"), PHONE("PHONE", "手机号");
		private String code;
		private String value;
		
		AccountType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String value() {
			return value;
		}
		
		public String code() {
			return code;
		}
		
	}
	
	/**
	 * 多媒体类型
	 */
	public enum MediaTypeEnum {
		VIDEO("VIDEO", "视频"), AUDIO("AUDIO", "语音");
		private String code;
		private String value;
		
		MediaTypeEnum(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
		
	}
	
	/**
	 * 文章类型
	 */
	public enum ArticleTypeEnum {
		POW("POW", "test"), SCHOOL("SCHOOL", "test"), ABOUT_US("ABOUT_US", "关于我们");
		private String code;
		private String value;
		
		ArticleTypeEnum(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
		
	}
	
	/**
	 * 地区层级类型
	 */
	public enum RegionLevelType {
		PROVINCE(1L, "省"), CITY(2L, "市"), AREA(3L, "县");
		private Long code;
		private String value;
		
		RegionLevelType(Long code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public Long getCode() {
			return code;
		}
	}
	
	/**
	 * 用户钱包大类
	 */
	public enum WalletBigType {
		WALLET("WALLET", "钱包"),
		CONTRACT("CONTRACT", "合约"),
		CURRENCY("CURRENCY", "币币"),
		LEGAL("LEGAL", "法币"),
		OPTION("OPTION", "期权"),
		FUTURES("FUTURES", "黄金"),
		MINING("MINING", "矿机"),
		;
		public String code;
		public String value;
		
		WalletBigType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
	}

//    /**
//     * 用户合约账户类型
//     * USDT、MGE
//     */
//    public enum ContractType
//    {
//        USDT("USDT", "USDT", "ETH_USDT"),
//        MGE("MGE", "MGE", "MGE"),
//        ;
//        private String code;
//        private String value;
//        private String coin;
//
//        ContractType(String code, String value, String coin)
//        {
//            this.code = code;
//            this.value = value;
//            this.coin = coin;
//        }
//
//        public String coin()
//        {
//            return coin;
//        }
//
//        public String getValue()
//        {
//            return value;
//        }
//
//        public String getCode()
//        {
//            return code;
//        }
//    }
//    /**
//     * 用户法币账户类型
//     * USDT、MGE
//     */
//    public enum LegalType
//    {
//        USDT("USDT", "USDT", "ETH_USDT"),
////        MGE("MGE", "MGE", "MGE"),
//        ;
//        private String code;
//        private String value;
//        private String coin;
//
//        LegalType(String code, String value, String coin)
//        {
//            this.code = code;
//            this.value = value;
//            this.coin = coin;
//        }
//
//        public String coin()
//        {
//            return coin;
//        }
//
//        public String getValue()
//        {
//            return value;
//        }
//
//        public String getCode()
//        {
//            return code;
//        }
//    }
//  /**
//     * 用户钱包账户类型
//     * BTC、USDT、ETH、ETC、XR、EOS、BCH、LTC
//     */
//    public enum WalletType
//    {
//        BTC("BTC", "BTC", "BTC"),
//        USDT("USDT", "USDT", "ETH_USDT"),
//        ETH("ETH", "ETH", "ETH"),
//        ETC("ETC", "ETC", "ETC"),
//        XRP("XRP", "XRP", "XRP"),
//        EOS("EOS", "EOS", "EOS"),
//        BCH("BCH", "BCH", "BCH"),
//        LTC("LTC", "LTC", "LTC"),
//        FOT("FOT", "FOT", "FOT"),
//        MGE("MGE", "MGE", "MGE"),
//        MGM("MGM", "MGM", "MGM"),
//        ;
//        private String code;
//        private String value;
//        private String coin;
//
//        WalletType(String code, String value, String coin)
//        {
//            this.code = code;
//            this.value = value;
//            this.coin = coin;
//        }
//
//        public String coin()
//        {
//            return coin;
//        }
//
//        public String getValue()
//        {
//            return value;
//        }
//
//        public String getCode()
//        {
//            return code;
//        }
//    }
//
//    /**
//     * 币种类型
//     */
//    public enum CoinType
//    {
//        BTC("BTC", "BTC", "BTC"),
//        USDT("USDT", "USDT", "ETH_USDT"),
//        ETH("ETH", "ETH", "ETH"),
//        ETC("ETC", "ETC", "ETC"),
//        XRP("XRP", "XRP", "XRP"),
//        EOS("EOS", "EOS", "EOS"),
//        BCH("BCH", "BCH", "BCH"),
//        LTC("LTC", "LTC", "LTC"),
//        FOT("FOT", "FOT", "FOT"),
//        MGE("MGE", "MGE", "MGE"),
//        MGM("MGM", "MGM", "MGM"),
//
//        ;
//        private String code;
//        private String value;
//        /**
//         * 第三方币种规范
//         */
//        private String chainCoin;
//
//        CoinType(String code, String value, String chainCoin)
//        {
//            this.code = code;
//            this.value = value;
//            this.chainCoin = chainCoin;
//        }
//
//        public String getValue()
//        {
//            return value;
//        }
//
//        public String getCode()
//        {
//            return code;
//        }
//
//        /**
//         * 第三方币种规范
//         */
//        public String getChainCoin()
//        {
//            return chainCoin;
//        }
//    }

//    /**
//     * 平台代币
//     */
//    public enum PlatCoinType
//    {
//        MGE_USDT("MGE/USDT","mgeusdt","MGE"),
//
//        ;
//        public String code;
//        public String value;
//        public String coin;
//
//        PlatCoinType(String code,String value,String coin)
//        {
//            this.code = code;this.value=value;this.coin=coin;
//        }
//
//    }
//
//
	
	/**
	 * 提现类型
	 */
	public enum WithdrawStatusEnum {
		CHECK("CHECK", "审核中"),
		PASS("PASS", "审核通过"),
		COIN("COIN", "等待上块"),
		REJECT("REJECT", "审核不通过"),
		IN_TRANFER("IN_TRANFER", "内部转账"),
		TEST_PASS("TEST_PASS", "线下通过");
		private String code;
		private String value;
		
		WithdrawStatusEnum(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 常规审核
	 */
	public enum Status {
		CHECK("CHECK", "审核中"), PASS("PASS", "审核通过"), REJECT("REJECT", "审核不通过");
		public String code;
		public String value;
		
		Status(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String value() {
			return value;
		}
		
		public String code() {
			return code;
		}
	}
	
	/**
	 * 用户类型
	 */
	public enum MemberType {
		V0(0, "V0"), V1(1, "V1"), V2(2, "V2"), V3(3, "V3"), V4(4, "V4"), V5(5, "V5"), V6(6, "V6"), V7(7, "V7"), V8(8, "V8"), V9(9, "V9"), V10(10, "V10"), V11(11, "V11"), V12(12, "V12"), V13(13, "V13"), V14(14, "V14"), V15(15, "V15"), V16(16, "V16"), V17(17, "V17"), V18(18, "V18"), V19(19, "V19");
		private int code;
		private String value;
		
		MemberType(int code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String value() {
			return value;
		}
		
		public String code() {
			return code + "";
		}
	}
	
	/**
	 * 流水类型
	 */
	
	public enum CashFlowTypeEnum {
		WALLET_REWART("WALLET_REWART", "签到奖励"),
		MINING_ENERGY_RETURN("MINING_ENERGY_RETURN", "燃料返还"),
		MINING_ENERGY_LOSS("MINING_ENERGY_LOSS", "挖矿能源损耗"),
		MINING_FUEL("MINING_FUEL", "矿机启动燃料"),
		MINING_PRI("MINING_PRI", "矿机价格"),
		MINING_GET("MINING_GET", "矿机收益"),
		TE_MANAGEMENT_COIN_DO("TE_MANAGEMENT_COIN_DO", "新币认购"),
		TE_MANAGEMENT_COIN("TE_MANAGEMENT_COIN", "认购收益"),
		SAVECOIN("SAVECOIN", "理财生息收益"),
		SAVECOINDO("SAVECOINDO", "理财生息"),
		WITHDRAW("WITHDRAW", "提币", "提幣",
				"철수", "撤退", "Withdrawal"),
		WITHDRAW_OUT("WITHDRAW_OUT", "提币到外盘", "提幣到外盤",
				"외부 디스크로 돈 인출", "外部ディスクにお金を引き出す", "Withdraw money to external disk"),
		RECHARGE("RECHARGE", "充币", "充幣",
				"재충전", "充電する", "Recharge"),
		WITHDRAW_REJECT("WITHDRAW_REJECT", "提币未通过",
				"提幣未通過", "출금 실패"
				, "引き出しに失敗しました", "Withdrawal failed"),
		PLATFORM_ADD("PLATFORM_ADD", "后台充值", "後臺充值",
				"백그라운드 충전", "バックグラウンドリチャージ", "Background recharge"),
		PLATFORM_SUB("PLATFORM_SUB", "后台减少", "後臺減少",
				"배경 감소", "バックグラウンド削減", "Background reduction"),
		TRANSFER_IN("TRANSFER_IN", "内部转账", "內部轉賬",
				"내부 거래", "内部転送", "Internal transfer"),
		CONVERT_OUT("CONVERT_OUT", "划转转出", "劃轉轉出",
				"송금", "転出", "Transfer out"),
		CONVERT_IN("CONVERT_IN", "划转转入", "劃轉轉入",
				"환승", "転送", "Transfer in"),
		CURRENCY_ENTRUST("CURRENCY_ENTRUST", "币币委托", "幣幣委托",
				"코인 커미션", "コインコミッション", "Coin commission"),
		CURRENCY_ENTRUST_REST("CURRENCY_ENTRUST_REST", "币币委托结余返还", "幣幣委托結余返還",
				"통화 수수료 잔액 반환", "通貨コミッションバランスリターン", "Currency commission balance return"),
		CURRENCY_ENTRUST_FEE("CURRENCY_ENTRUST_FEE", "币币委托-结算手续费", "幣幣委托-結算手續費",
				"통화 수수료 정산 수수료",
				"通貨手数料-決済手数料", "Currency commission-settlement fee"),
		CURRENCY_CANCEL("CURRENCY_CANCEL", "币币委托-撤销", "幣幣委托-撤銷",
				"코인 위탁 취소", "コイン委託-キャンセル", "Coin entrust-cancellation"),
		CONTRACT("CONTRACT", "合约交易", "合約交易",
				"계약 거래", "契約取引", "Contract transaction"),
		FUTURES("FUTURES", "黄金交易", "黃金交易",
				"", "", ""),
		CONTRACT_CANCEL("CONTRACT_CANCEL", "合约交易-撤销", "合約交易-撤銷",
				"계약 거래 취소", "契約トランザクション-キャンセル", "Contract transaction-cancellation"),
		FUTURES_CANCEL("FUTURES_CANCEL", "黄金交易-撤销"),
		HANDLE("HANDLE", "手动平仓", "手動平倉",
				"수동 폐쇄", "手動清算", "Manual closing"),
		FIXED("FIXED", "强制平仓", "強制平倉",
				"강제 청산", "強制清算", "Forced liquidation"),
		PROFIT("PROFIT", "止盈平仓", "止盈平倉",
				"이익을 얻고 포지션을 청산하십시오"
				, "利益を上げてポジションを閉じる", "Take profit and close position"),
		LOSS("LOSS", "止损平仓", "止損平倉",
				"손실 중지", "ストップロス", "Stop loss"),
		BOUGHT("BOUGHT", "法币交易-购买USDT", "法幣交易-購買USDT",
				"피아트 통화 거래-USDT 구매", "フィアット通貨取引-USDTを購入する",
				"Fiat currency transaction-buy USDT"),
		DEPOSIT("DEPOSIT", "挂单押金缴纳", "掛單押金繳納",
				"보류 주문에 대한 입금 지불", "保留中の注文に対するデポジットの支払い", "Deposit payment for pending orders"),
		DEPOSIT_FAIL("DEPOSIT_FAIL", "承兑商申请驳回",
				"承兌商申請駁回",
				"수락 자 신청이 거부되었습니다.", "アクセプター申請が却下されました", "Acceptor application rejected"),
		MAKE_UP_PRICE("MAKE_UP_PRICE", "补缴押金", "補繳押金",
				"보증금 지불", "デポジットを支払う", "Pay the deposit"),
		PLAT_ADD_MAKE("PLAT_ADD_MAKE", "后台添加押金", "後臺添加押金",
				"백그라운드에서 보증금 추가", "バックグラウンドでデポジットを追加", "Add deposit in the background"),
		PLAT_SUB_MAKE("PLAT_SUB_MAKE", "后台减少押金", "後臺減少押金",
				"예금의 백 스테이지 감소", "預金の舞台裏削減", "Backstage reduction of deposit"),
		BACK("BACK", "退还押金", "退還押金",
				"보증금 환불", "返金デポジット", "Refund deposit"),
		OTC_SELL("OTC_SELL", "挂单出售", "掛單出售",
				"주문 판매 대기 중", "保留中の注文販売", "Pending order sale"),
		OTC_SELL_CANCEL("OTC_SELL_CANCEL", "挂单撤单", "掛單撤單",
				"보류중인 주문 취소", "保留中の注文キャンセル", "Pending order cancellation"),
		I_SELL("I_SELL", "我要出售", "我要出售",
				"나는 팔고 싶다", "売りたい", "I want to sell"),
		OTC_FINISH("OTC_FINISH", "放币", "放幣",
				"돈을 넣어", "お金を入れる", "Put money"),
		CANCEL_PUNISH("CANCEL_PUNISH", "撤单惩罚", "撤單懲罰",
				"취소 위약금", "キャンセルペナルティ", "Cancellation penalty"),
		BONUS_AWARD("BONUS_AWARD", "佣金", "傭金",
				"커미션", "プレミアム", "commission"),
		OPTIONBACK("OPTIONBACK", "返还", "", "", "", ""),
		OPTIONRECORD("OPTIONRECORD", "返佣"),
		OPTIONFEE("OPTIONFEE", "期权交易 手续费", "", "", "", ""),
		OPTIONKZ("OPTIONKZ", "期权交易 看涨", "", "", "", ""),
		OPTIONKD("OPTIONKD", "期权交易 看跌", "", "", "", ""),
		OPTIONY("OPTIONY", "期权交易 盈利", "", "", "", ""),
		OPTIONK("OPTIONK", "期权交易 亏损", "", "", "", ""),
		OPTIONTONGCHI("OPTIONTONGCHI", "期权 平局通吃"),
		OPTIONTUIHUI("OPTIONTUIHUI", "期权 平局退回"),
		OPTIONCHOUSHUI("OPTIONCHOUSHUI", "期权交易 抽水"),
		TEAMEARNING("TEAMEARNING", "团队收益 划转"),
		TEAM_RECORD_BACK("TEAM_RECORD_BACK", "社区奖励"),
		;
		private String code;
		private String value;
		private String hkValue;
		private String hgValue;
		private String jpValue;
		private String usValue;
		
		CashFlowTypeEnum(String code, String value, String hkValue, String hgValue, String jpValue, String usValue) {
			this.code = code;
			this.value = value;
			this.hgValue = hgValue;
			this.jpValue = jpValue;
			this.hkValue = hkValue;
			this.usValue = usValue;
		}
		
		CashFlowTypeEnum(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
		
		public String getHkValue() {
			return hkValue;
		}
		
		public String getHgValue() {
			return hgValue;
		}
		
		public String getJpValue() {
			return jpValue;
		}
		
		public String getUsValue() {
			return usValue;
		}
	}
	
	/**
	 * 钱包账户具体操作参数项
	 */
	public enum ItemCode {
		USED("USED", "可用"),
		LOCKED("LOCKED", "冻结"),
		POSITION("POSITION", "持仓保证金"),
		ENTRUST("ENTRUST", "配资资产"),
		GIVE("GIVE", "持仓配资"),
		;
		
		public String code;
		public String value;
		
		ItemCode(String code, String value) {
			this.code = code;
			this.value = value;
		}
	}
	
	/**
	 * 资金流向
	 */
	public enum CashFlowOpEnum {
		FLOW_IN(1L, "流入"),
		FLOW_OUT(0L, "流出");
		public Long code;
		public String value;
		
		CashFlowOpEnum(Long code, String value) {
			this.code = code;
			this.value = value;
		}
		
	}
	
	/**
	 * 支付方式
	 */
	public enum PayTypeEnum {
		ALI_PAY("ALI_PAY", "支付宝"), WE_CHAT("WE_CHAT", "微信"), BANK("BANK", "银行卡"), PAYPAL("PAYPAL", "PAYPAL");
		public String code;
		public String value;
		
		PayTypeEnum(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 支付
	 */
	public enum PayEnum {
		PAID("PAID", "已支付"),
		WAIT("WAIT", "待支付"),
		UNPAID("UNPAID", "未支付"),
		NO_NEED("NO_NEED", "无需支付");
		private String code;
		private String value;
		
		PayEnum(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 网关状态
	 */
	public enum GatewayStatusEnum {
		OPEN("OPEN", "开放"),
		OPEN_LOG("OPEN_LOG", "开放且日志"),
		CLOSE("CLOSE", "关闭");
		private String code;
		private String value;
		
		GatewayStatusEnum(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 币币 交易方式
	 */
	public enum DealWay {
		MARKET("MARKET", "市价"),
		LIMIT("LIMIT", "限价");
		public String code;
		public String value;
		
		DealWay(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
	}
	
	/**
	 * 币币 交易方式
	 */
	public enum MatchType {
		BUY("BUY", "买单"),
		SELL("SELL", "卖单");
		public String code;
		public String value;
		
		MatchType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		
	}
	
	/**
	 * 合约 交易类型
	 */
	public enum CompactType {
		BUY("BUY", "买入开多"),
		SELL("SELL", "卖出开空");
		public String code;
		public String value;
		
		CompactType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		
	}
	
	/**
	 * 合约 交易类型
	 */
	public enum CompactStatus {
		N("N", "持仓中"),
		Y("Y", "已平仓"),
		IN("IN", "委托中"),
		CANCEL("CANCEL", "撤单");
		public String code;
		public String value;
		
		CompactStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		
	}
	
	/**
	 * 平仓类型(手动平仓、止盈平仓、止损平仓、爆仓）
	 */
	public enum ExitType {
		HANDLE("HANDLE", "手动平仓"),
		FIXED("FIXED", "强制平仓"),
		PROFIT("PROFIT", "止盈平仓"),
		LOSS("LOSS", "止损平仓"),
		;
		public String code;
		public String value;
		
		ExitType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		
	}
	
	/**
	 * 币币委托
	 */
	public enum MatchStatus {
		NO_PAY("NO_PAY", "未支付"),
		PAID("PAID", "已支付"),
		PART_MATCH("PART_MATCH", "部分撮合"),
		ALL_MATCH("ALL_MATCH", "全部撮合"),
		CANCEL("CANCEL", "已经撤销"),
		FINISH("FINISH", "完成结算"),
		CANCEL_FINISH("CANCEL_FINISH", "撤销结算"),
		;
		public String code;
		public String value;
		
		MatchStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		
	}
	
	/**
	 * 法币交易状态
	 */
	public enum LegalStatus {
		N("N", "未完成"),
		Y("Y", "已完成"),
		;
		public String code;
		public String value;
		
		LegalStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		
	}
	
	/**
	 * 交易订单状态
	 */
	public enum BillType {
		BUY("BUY", "购买单"), SELL("SELL", "出售单");
		public String code;
		public String value;
		
		BillType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 判责  责任方（BUY-买方责任 SELL-卖方责任 NO-双方无责）
	 */
	public enum DutyType {
		BUY("BUY", "买方责任"), SELL("SELL", "卖方责任"), NO("NO", "双方无责");
		public String code;
		public String value;
		
		DutyType(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 交易订单状态   已完成，买家待付款，卖家待放币，已取消，申述中
	 */
	public enum BillStatus {
		WAIT("WAIT", "买家待付款"), WAIT_COIN("WAIT_COIN", "卖家待放币"), CANCEL("CANCEL", "已取消"), FINISH("FINISH", "已完成"), APPEAL("APPEAL", "申述中");
		public String code;
		public String value;
		
		BillStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 交易订单申诉状态   已完成，买家待付款，卖家待放币，已取消，申述中
	 */
	public enum AppealStatus {
		CHECK("CHECK", "待处理"), PASS("PASS", "已放币"), CANCEL("CANCEL", "已取消");
		public String code;
		public String value;
		
		AppealStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * 锁仓记录状态
	 */
	public enum LockRecordStatus {
		LOCKING("LOCKING", "锁仓中"), WAIT("WAIT", "待赎回"), FINISH("FINISH", "已结束");
		public String code;
		public String value;
		
		LockRecordStatus(String code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getCode() {
			return code;
		}
	}


	/**
	 * 操作类型
	 */
	public enum HandlerTypes {
		COMPACT("CONTRACT", "合约交易"), CURRENT("CURRENT", "现货交易");
		public String code;
		public String value;

		HandlerTypes(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getCode() {
			return code;
		}
	}

	
}
