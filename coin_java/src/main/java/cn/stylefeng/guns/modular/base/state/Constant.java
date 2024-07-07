package cn.stylefeng.guns.modular.base.state;

import java.math.BigDecimal;

public class Constant extends ProConst{
	//合约交易-开启
	public static String CONTRACT_OPEN="CONTRACT_OPEN";
	//合约交易-手续费
	public static String CONTRACT_FEE="CONTRACT_FEE";//仓位手续费
	//合约交易-保证金
	public static String CONTRACT_MARKET_PRICE="CONTRACT_MARKET_PRICE";//仓位保证金

	public static String WITHDRAWCOIN="WITHDRAW_COIN";
	public static String WITHDRAWCOINMINNUM="WITHDRAW_COIN_MIN_NUM";
	//通知设置
	public static String SMSSENDOPEN="SMS_SEND_OPEN";
	public static String EMAILSENDOPEN="EMAIL_SEND_OPEN";
	public static String INVITEOPEN="INVITE_OPEN";

	//CHAT_MSG_COUNT 聊天室保存信息记录条数
	public static int CHATCOUNT=500;
	public static String CHATMSGCOUNT="CHAT_MSG_COUNT";
	public static String CHATMSG="CHAT:MSG";
	public static String CHATPWD="CHAT_PWD";
	public static String CHATDESC="CHAT_DESC";
	public static String CHATNAME="CHAT_NAME";
	public static String CHATIMG="CHAT_IMG";
	public static String CHATOPEN = "CHAT_OPEN";

	//系统代理别名
	public static String SYSTEMPROXYNAME = "agnetadmin";
	//系统socket名字
	public static String SYSTEMSOCKETSENDNAME = "admin";
	public static String VERIFYSTRING = "实名认证信息";
	public static String RECHARGESTRING = "充值信息";
	public static String WITHDRAWCOINSTRING = "提币信息";
	public static String DEPOSITSTRING = "缴纳保证金信息";
	public static String BACKDEPOSITSTRING = "退保证金信息";
	public static String ADDCOINAPPLYSTRING ="申请上币信息";
	public static String ADDLOANAPPLYSTRING ="客户申请信息";
	public static String TRADEAPPEALSTRING = "法币申诉信息";
    //常规缓存保存时间
    public static final long EXPIRE_NORMAL = 24*60*60*3;
	//系统平台
	public static final Long SYS_PLATFORM = -1L;
	//机器人账户id
	public static final Long ROBOT_ID = 10L;
	//否
	public static final String N = "N";
	public static final String Y = "Y";

	public static final String SMS_API_KEY = "SMS_API_KEY";
    public static final String SMS_N18_ACCOUNT = "SMS_N18_ACCOUNT";
	public static final String SMS_N18_PASSWORD ="SMS_N18_PASSWORD" ;
	public static final String TRADE_PERPETUAL = "TRADE_PERPETUAL:";
	//小位数资产删除
	public static BigDecimal MIN_DATA=new BigDecimal(0.000001);
	//小位数资产删除 （USDT）
	public static BigDecimal MIN_DATA_USDT=new BigDecimal(0.001);



	//k线分钟段节点列表 第一个%s为交易对 第二个为分钟段
	public static final String KINE_MINUTE = "KINE_MINUTE:%s:%s";

	//缓存k线过期时间 一天
	public static final long KLINE_TIME_OUT = 24*60*60;

	//OTC买方 取消次数
	public static final String CANCEL="CANCEL:%s";
	//超时取消次数
	public static final String TIME_COUNT="TIME_COUNT";

	//OTC挂单买方 取消次数
	public static final String OTC_CANCEL="OTC_CANCEL:%s";
	//限制不可下单（取消3次）
	public static final String LIMIT_DONT="LIMIT_DONT:%s";

	public static final String SMS="SMS:";

	public static final String TOKEN="TOKEN:";
	public static final String SINGLE_ACCOUNT="SINGLE_ACCOUNT:";

	//24小时无法提币
	public static final String NOT_WITHDRAW="NOT_WITHDRAW:";

    // 冻结
    public static final String FROZEN = "FROZEN";
    public static final String WILL = "WILL";
    public static final String WILL_P = "WILL_P";

	public static final String _NEW = "_1day_new";

	//现货-k线
	public static final String KINE = "KINE:";

	//mongodb存储的k线
	public static final String MONGO_KDATA="KDATA_$symbol$_$period$";

	//期货 永续-k线
//	public static final String KINE_PERPETUAL = "KINE_PERPETUAL:";
	public static final String KINE_FUTURES = "KINE_FUTURES:";
	public static final String TRADE = "TRADE:";
	//现货-深度
	public static final String DEPTH = "DEPTH:";
	public static final String JIA = "_JIA";
	//期货 永续-深度
	public static final String DEPTH_PERPETUAL = "DEPTH_PERPETUAL:";
	//期货 永续黄金-深度
	public static final String DEPTH_FUTURES = "DEPTH_FUTURES:";


	//现货-24小时
	public static final String TF_TICKET = "TF_TICKET:";

	//期货 永续-24小时
	public static final String TF_TICKET_PERPETUAL = "TF_TICKET_PERPETUAL:";

	//期货 永续黄金-24小时
	public static final String TF_TICKET_FUTURES = "TF_TICKET_FUTURES:";



	//撮合交易-买单队列
	public static final String BUY="BUY:";
	//统一结算
	public static final String BILL="BILL:";
	//结算集合
	public static final String BILL_DATA="BILL_DATA:";

	//合约账户
	public static final String CONTRACT_CODE="CONTRACT:%s:";
	//黄金账户
	public static final String FINFUTURES_CODE="FINFUTURES:%s:";

	//撮合交易-卖单队列
	public static final String SELL="SELL:";

	//入队买锁
	public static final String BUY_LOCK="BUY_LOCK:";
	//入队卖锁
	public static final String SELL_LOCK="SELL_LOCK:";

	//入队  买单队列 预处理队列
	public static final String BUY_READY="BUY_READY:";
	//入队 卖单队列  预处理队列
	public static final String SELL_READY="SELL_READY:";

	//合约多处理一次的数据队列，以防数据错误
	public static final String OP_CONTRACT_DATA="OP_CONTRACT_DATA";
	//合约多处理一次的数据队列，以防数据错误
	public static final String OP_FINFUTURES_DATA="OP_FINFUTURES_DATA";

	//币币交易锁
	public static final String MATCH_LOCK="MATCH_LOCK:";

	//MGE最新成交价
	public static final String MD_NEW_UNIT="MD_NEW_UNIT";


	//MGE撮合成交列表
	public static final String MD_MATCH_LIST="MD_MATCH_LIST";

	//盈亏监听
	public static final String PL = "PL:";

	//订单总盈亏
	public static final String PL_TOTAL_ = "PL_TOTAL:%s:";

	public static final String LOSS_PL_TOTAL_ = "LOSS_PL_TOTAL:%s:";

	//盈亏监听
	public static final String PLF = "PLF:";

	//订单总盈亏
	public static final String PLF_TOTAL_ = "PLF_TOTAL:%s:";

	public static final String LOSS_PLF_TOTAL_ = "LOSS_PLF_TOTAL:%s:";
	//订单历史总盈亏
	public static final String PLF_TOTAL_HIS = "PLF_TOTAL_HIS:";

	//订单历史总盈亏
	public static final String PL_TOTAL_HIS = "PL_TOTAL_HIS:";
	//止盈止损平仓 锁
	public static final String STOP_LOCK = "STOP_LOCK:";
	//爆仓 锁
	public static final String BOOM_LOCK = "BOOM_LOCK:";
	// 合约划转 锁
	public static final String CONTRACT_TRANSFER_LOCK = "CONTRACT_TRANSFER_LOCK:";
	//手动平仓 锁
	public static final String HANDLE_LOCK = "HANDLE_LOCK:";
	//持仓  锁
	public static final String CONTRACT_LOCK = "CONTRACT_LOCK:";
	//合约数据刷新锁
	public static final String CONTRACT_UPDATE_LOCK = "CONTRACT_UPDATE_LOCK";
	//黄金数据刷新锁
	public static final String FUTURES_UPDATE_LOCK = "FUTURES_UPDATE_LOCK";
	//穿仓锁
	public static final String CONTRACT_CROSS_LOCK = "CONTRACT_CROSS_LOCK";
	//合约买入队列
	public static final String UPDATE_CONTRACT_NO_SQL = "UPDATE_CONTRACT_NO_SQL:";

	//合约数据刷新锁 用户
	public static final String CONTRACT_UPDATE_LOCK_MEMBER = "CONTRACT_UPDATE_LOCK_MEMBER:";

	//平台代币最新成交价
	public static final String PLAT_NEW_UNIT="PLAT_NEW_UNIT:%s";
	//平台代币列表
	public static final String PLAT_TOKEN="PLAT_TOKEN";

	//平台代币撮合成交列表
	public static final String PLAT_MATCH_LIST="PLAT_MATCH_LIST:%s";

	//日线处理
	public static final String PLAT_MATCH_LIST_DAY="PLAT_MATCH_LIST_DAY:%s";

	public static final String PLAT_COIN="PLAT_COIN:%s_%s";
	public static final String PLAT_KLINE="PLAT_KLINE:%s_%s";



	//商户角色id
	public static final String MERCHANT_ROLE_ID="10";



	/**
	 * ============================== 排序参数 ==========================================
	 *
	 * 									start
	 *
	 *
	 *========================================================================
	 */
	//创建时间
	public static final String CREATE_TIME="CREATE_TIME";
	public static final String SORT="sort";
/**


	/**
	 *========================================================================
	 *
	 *
	 * 					  			系统参数 -----start
	 *
	 *========================================================================
	 */

	//累计签到奖励内容
	public static final String CHECK_IN_AWARD="CHECK_IN_AWARD";

	//累计签到奖励内容
	public static final String TEAM_BONUS_COIN="BONUS_COIN";
    public static final String TEAM_NOT_BONUS_RECORD="TEAM_NOT_BONUS_RECORD";

	//本站URL
	public static final String URL="URL";


	//短信账号
	public static final String SMS_ACCOUNT="SMS_ACCOUNT";
	//短信密码
	public static final String SMS_PASSWORD="SMS_PASSWORD";
	//短信模板
	public static final String SMS_CONTENT="SMS_CONTENT";
	//短信服务地址
	public static final String SMS_URL="SMS_URL";
	//国际短信服务地址
	public static final String SMS_URL_I18="SMS_URL_I18";
	//token 失效时间 s
	public static final String TOKEN_EXPIRE = "TOKEN_EXPIRE";
	//短信开关
	public static final String SMS_OPEN = "SMS_OPEN";
	public static final String EMAIL_OPEN = "EMAIL_OPEN";
	public static final String SMS_COUNT = "SMS_COUNT";
	public static final String EMAIL_COUNT = "EMAIL_COUNT";

	public static final long SMS_TIMEOUT = 60*2;
	public static final long EMAIL_TIMEOUT = 60*5;



	//转账手续费
	public static final String _TRANSFER_FEE = "_TRANSFER_FEE";
	//转账最低量
	public static final String _TRANSFER_NUM = "_TRANSFER_NUM";



	//钱包开关
	public static final String CHAIN_OPEN = "CHAIN_OPEN";

	//CNY:USDT
	public static final String CNY_USDT = "CNY_USDT";




	//合约交易-保证金率
	public static final String CONTRACT_MARGIN = "CONTRACT_MARGIN";



	//法币交易- USDT单价
	public static final String LEGAL_USDT = "LEGAL_USDT";
	//法币交易- 最小交易量
	public static final String LEGAL_MIN = "LEGAL_MIN";
	//法币交易币种
	public static final String OTC_COIN="OTC_COIN";

	//锁仓最低USDT价值
	public static final String LOCK_MIN_USDT = "LOCK_MIN_USDT";


	//锁仓MGE行情价
	public static final String LOCK_UNIT = "LOCK_UNIT";

	//BTC、USDT、ETH、ETC、XR、EOS、BCH、LTC
	public static final String BTC_USDT = "BTC/USDT";

	/**
	 *========================================================================
	 * 	行情控制 -----start
	 *========================================================================
	 */
	// 一口价 70000.0
	public static final String MARKET_ONE = "MARKET:ONE:$symbol$";
	// 价格涨跌 -200
	public static final String MARKET_PRICE = "MARKET:PRICE:$symbol$";
	// 按比例涨跌 99.9
	public static final String MARKET_RATE = "MARKET:RATE:$symbol$";

	/**
	 *  区间比例涨跌,带时间参数,暂时不用
	 * [
	 * 		{"date":"2023-03-06 9:00:00","rate":"-0.01"},
	 *  	{"date":"2023-03-06 9:15:00","rate":"0.01"}
	 * 	]
	 */
	public static final String  MARKET_RATE_DATE = "MARKET:RATE_DATE:$symbol$";



	//提币手续费
	public static final String _WITHDRAW_FEE = "_WITHDRAW_FEE";

	//H5  URL
	public static final String H5_URL="H5_URL";

	//最小提币量
//	public static final String _MIN_WITHDRAW_NUM = "_MIN_WITHDRAW_NUM";
	//	//CNY:MD
//	public static final String CNY_MD = "CNY_MD";
	//币币交易-手续费比例
	public static final String _MATCH_FEE = "_MATCH_FEE";
	public static final String ETH_USDT = "ETH/USDT";
	public static final String EOS_USDT = "EOS/USDT";
	public static final String ETC_USDT = "ETC/USDT";
	public static final String XRP_USDT = "XRP/USDT";
	public static final String BCH_USDT = "BCH/USDT";
	public static final String LTC_USDT = "LTC/USDT";
	public static String configPrx = "configPrx:";

	//黄金交易-保证金率
	public static final String FUTURES_MARGIN = "FUTURES_MARGIN";

	//合约交易-手续费比例
	//public static final String CONTRACT_FEE = "CONTRACT_FEE";

	//合约交易-点差
	public static final String _SPREAD = "_SPREAD";

	//合约交易-配资手续费比例
	public static final String GIVE_FEE = "GIVE_FEE";
	//合约交易-平仓手续费比例
	public static final String OUT_FEE = "OUT_FEE";

	/**
	 *========================================================================
	 * 	验证发送 -----start
	 *========================================================================
	 */
	public static final String VERIFICATION_CODE_SMS = "VERIFICATION_CODE:SMS:";

	public static final String VERIFICATION_CODE_EMAIl = "VERIFICATION_CODE:EMAIL:";

}
