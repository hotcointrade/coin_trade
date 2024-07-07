package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.bipay.constant.UdunCoinType;
import cn.stylefeng.guns.core.bipay.constant.WaasCoinType;
import cn.stylefeng.guns.core.bipay.entity.Address;
import cn.stylefeng.guns.core.bipay.entity.Trade;
import cn.stylefeng.guns.core.bipay.http.client.WaasApiClient;
import cn.stylefeng.guns.core.common.constant.state.ManagerStatus;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.util.SmsUtil;
import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import cn.stylefeng.guns.core.util.UUIDUtil;

import cn.stylefeng.guns.core.websocket.WebSocketService;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.app.dto.*;
import cn.stylefeng.guns.modular.app.dto.e.*;
import cn.stylefeng.guns.modular.app.entity.*;
import cn.stylefeng.guns.modular.app.vo.*;
import cn.stylefeng.guns.modular.app.vo.e.ApiWalletVo;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.HttpUtils;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.base.util.SampleMail;
import cn.stylefeng.guns.modular.bulletin.entity.AppVersion;
import cn.stylefeng.guns.modular.bulletin.entity.Contact;
import cn.stylefeng.guns.modular.bulletin.service.AppVersionService;
import cn.stylefeng.guns.modular.bulletin.service.ContactService;
import cn.stylefeng.guns.modular.bulletin.service.SendSMSExtService;
import cn.stylefeng.guns.modular.chain.entity.*;
import cn.stylefeng.guns.modular.chain.service.*;
import cn.stylefeng.guns.modular.coin.constant.ContractOptionOrderDirection;
import cn.stylefeng.guns.modular.coin.constant.ContractOptionOrderResult;
import cn.stylefeng.guns.modular.coin.constant.ContractOptionOrderStatus;
import cn.stylefeng.guns.modular.coin.constant.ContractOptionStatus;
import cn.stylefeng.guns.modular.coin.entity.*;
import cn.stylefeng.guns.modular.coin.service.*;
import cn.stylefeng.guns.modular.com.entity.*;
import cn.stylefeng.guns.modular.com.service.*;
import cn.stylefeng.guns.modular.e.entity.*;
import cn.stylefeng.guns.modular.e.service.*;
import cn.stylefeng.guns.modular.extension.email.EmailUtils;
import cn.stylefeng.guns.modular.extension.ida.CashDto;
import cn.stylefeng.guns.modular.extension.ida.IdaUtils;
import cn.stylefeng.guns.modular.fin.entity.*;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.service.*;
import cn.stylefeng.guns.modular.metaData.service.ConfigService;
import cn.stylefeng.guns.modular.mining.entity.Mining;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.entity.MiningOrderDetail;
import cn.stylefeng.guns.modular.mining.service.MiningOrderDetailService;
import cn.stylefeng.guns.modular.mining.service.MiningOrderService;
import cn.stylefeng.guns.modular.mining.service.MiningService;
import cn.stylefeng.guns.modular.otc.entity.Deposit;
import cn.stylefeng.guns.modular.otc.service.DepositService;
import cn.stylefeng.guns.modular.promotion.entity.Declares;
import cn.stylefeng.guns.modular.promotion.entity.Problem;
import cn.stylefeng.guns.modular.promotion.entity.WhiteBook;
import cn.stylefeng.guns.modular.promotion.service.DeclaresService;
import cn.stylefeng.guns.modular.promotion.service.ProblemService;
import cn.stylefeng.guns.modular.promotion.service.WhiteBookService;
import cn.stylefeng.guns.modular.system.entity.Role;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.service.RoleService;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.guns.modular.websocket.ChatMsgDTO;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.sinohope.response.ApiWaasWalletInfoVO;
import com.sinohope.response.WaasAddressVo;
import com.sinohope.response.common.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeService extends Constant {

	//状态：是
	public static final String YES = "1";
	//状态：否
	public static final String NO = "0";

	//状态：审核中
	public static final String CHECK = "2";

	//添加
	public static final int ADD = 1;
	//删除
	public static final int DEL = 0;

	public static final String emailMatch = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

	@Value("${h5.regApi}")
	private String regApi;

//	@Value("${eth.key}")
//	private String key;
//	@Value("${eth.secret}")
//	private String secret;
//	@Value("${eth.memberCode}")
//	private String memberCode;
//	//创建钱包地址
//	@Value("${eth.createUrl}")
//	private String createUrl;
//	//提幣地址
//	@Value("${eth.mentionUrl}")
//	private String mentionUrl;
//	//地址校验
//	@Value("${eth.checkAddressUrl}")
//	private String checkAddressUrl;

	@Resource
	private SpotService spotService;
	@Autowired
	Gson gson;
	@Resource
	MemberService memberService;
	@Resource
	ConfigService configService;
	@Resource
	TeamEarningService teamEarningService;
	@Resource
	TeamEarningsService teamEarningsService;
	@Resource
	RedisUtil redisUtil;
	@Autowired
	private TeManagementService teManagementService;
	@Autowired
	private TeManagementLogService teManagementLogService;
	@Autowired
	private TeManagementReleaseService teManagementReleaseService;

	@Autowired
	private CoinApplyService coinApplyService;
	@Resource
	private MiningOrderService miningOrderService;
	@Resource
	private MiningOrderDetailService miningOrderDetailService;
	@Resource
	DeclaresService declaresService;
	@Autowired
	ContractOptionService optionService;
	@Resource
	ContractOptionCoinService optionCoinService;
	@Resource
	ContractOptionOrderService optionOrderService;
	@Resource
	AppVersionService versionService;

	@Autowired
	RoleService roleService;
	@Autowired
	TeamLevelService teamLevelService;
	@Resource
	WalletService walletService;
	@Autowired
	ContractGoldService goldService;
	@Resource
	ContractGoldCoinService goldCoinService;
	@Resource
	ContractGoldOrderService goldOrderService;

	@Resource
	JobService jobService;

	@Resource
	CashflowService cashflowService;

	@Resource
	RegionService regionService;

	@Resource
	SendSMSExtService sendSMSExtService;
	@Resource
	SmsUtil smsUtil;

	@Resource
	ContactService contactService;

	@Resource
	CheckInService checkInService;

	@Resource
	MediaService mediaService;

	@Resource
	WithdrawService withdrawService;

	@Resource
	CoinService coinService;

	@Resource
	ShippingService shippingService;

	@Autowired
	PhoneCodeService phoneCodeService;

	@Autowired
	RechargeService rechargeService;

	@Autowired
	TransferService transferService;

	@Autowired
	ConvertService convertService;

	@Autowired
	ProblemService problemService;


//    @Autowired
//    HuoBiService huoBiService;

	@Autowired
	VerifyService verifyService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	ContractService contractService;
	@Autowired
	FinFuturesService finFuturesService;

	@Autowired
	CurrencyService currencyService;

	@Autowired
	FinMiningService finMiningService;
	@Autowired
	FinOptionService finOptionService;

	@Autowired
	LegalService legalService;

	@Autowired
	PlatformAddressService platformAddressService;

	@Autowired
	MatchService matchService;

	@Autowired
	LeverageService leverageService;
	@Autowired
	FuturesLeverageService futuresLeverageService;

	@Autowired
	CompactService compactService;

	@Autowired
	FuturesCompactService futuresCompactService;

	@Autowired
	BoughtService boughtService;

	@Autowired
	SystemMatchService systemMatchService;

	@Autowired
	MatchDetailService matchDetailService;
	@Autowired
	CommonService commonService;

	@Resource
	private MemberRechargeAddressService memberRechargeAddressService;

	@Resource
	private SwapService swapService;

	@Resource
	private FuturesService futuresService;

	@Value("${ida.key}")
	private String idaKey;

	@Value("${ida.url}")
	private String idaUrl;

	@Value("${ida.appid}")
	private String idaAppid;

	@Value("${ida.pickupUrl}")
	private String pickupUrl;

	@Value("${ida.receiveUrl}")
	private String receiveUrl;

	@Autowired
	LiveService liveService;

	@Autowired
	WalletTransferService walletTransferService;

	@Autowired
	LiveRecordService liveRecordService;

	@Autowired
	DepositService depositService;

	@Resource
	BiPayService biPayService;

	@Autowired
	private UserService userService;
	@Autowired
	private FinPeriodicService finPeriodicService;
	@Autowired
	private FinPeriodicOrderService finPeriodicOrderService;

	@Resource
	private WhiteBookService whiteBookService;
	@Resource
	private MiningService miningService;
	@Resource
	private LoanService loanService;

	@Resource
	private WaasApiClient waasApiClient;

	@Autowired
	private ContractOptionService contractOptionService;

	@Autowired
	private ContractOptionCoinService contractOptionCoinService;

	@Autowired
	private ContractOptionOrderService contractOptionOrderService;

	/**
	 * 注册
	 *
	 * @param regVo regVo
	 * @return Result
	 */
	@Transactional
	public Result register(RegVo regVo) {
		Member member = new Member();
		member.setAccount(regVo.getAccount());
		//判断该用户账号是否存在
		if (memberService.getBaseMapper().getUserForOtherType(regVo.getAccount()) != null) {
			return fail(ApiStatus.EXIST_ACCOUNT);
		}
		// redisUtil.set(SMS + account, content, EMAIL_TIMEOUT);

		if (!StrUtil.equals("123456", regVo.getMsg())) {
			if (!regVo.getAccount().contains("@")) {
				//sms
				if (!F.me().cfg(SMSSENDOPEN).equals("N")) {
					if (!smsUtil.vaildCode(regVo.getAccount(), regVo.getMsg())) {
						return fail(ApiStatus.MSG_ERROR);
					}
				}
			} else {
				//email
				String msg = (String) redisUtil.get(SMS + regVo.getAccount());
				if (!F.me().cfg(EMAILSENDOPEN).equals("N")) {
					if (!StrUtil.equals(msg, regVo.getMsg())) {
						return fail(ApiStatus.MSG_ERROR);
					}
				}
			}
		}


		if (F.me().cfg(INVITEOPEN).equals("N")) {
			regVo.setInviteCode(F.me().cfg("REGISTER_INVITE_CODE"));
		} else {
			if (StringUtils.isBlank(regVo.getInviteCode())) {
				return fail(ApiStatus.NOT_DIRECTOR);
			}
		}

		BeanUtil.copyProperties(regVo, member);
		// 完善账号信息
		String salt = ShiroKit.getRandomSalt(5);
		String password = ShiroKit.md5(regVo.getPassword(), salt);

		String uid = RandomUtil.randomNumbers(6);
		while (true) {
			Member uidM = new Member();
			uidM.setUid(uid);
			if (memberService.getOne(new QueryWrapper<>(uidM)) == null) {
				break;
			}
			uid = RandomUtil.randomString(6);
		}
		String inviteCode = RandomUtil.randomStringUpper(8);
		while (true) {
			Member uidM = new Member();
			uidM.setInviteCode(inviteCode);
			if (memberService.getOne(new QueryWrapper<>(uidM)) == null) {
				break;
			}
			inviteCode = RandomUtil.randomStringUpper(8);
		}
		if (!regVo.getAccount().contains("@")) {
			if (StringUtils.isBlank(regVo.getCode())) {
				return Result.fail(ApiStatus.NO_NULL_QRCODE);
			}
			PhoneCode phoneCode = new PhoneCode();
			phoneCode.setDel("N");
			phoneCode.setCode(regVo.getCode());
//            List<PhoneCode> phoneCodeList =   phoneCodeService.list(new QueryWrapper<>(phoneCode).orderByDesc("CREATE_TIME"));
//            if(phoneCodeList == null || phoneCodeList.size()<=0 ){
//                return Result.fail(ApiStatus.NO_NULL_QRCODE);
//            }

			member.setArea(regVo.getCode().equals("86") ? "0" : "1");

			member.setAreaCode(AccountType.PHONE.code()).setPhone(regVo.getAccount()).setPhoneCode(regVo.getCode());
		} else {
			member.setAreaCode(AccountType.EMAIL.code()).setEmail(regVo.getAccount());
		}

		StringBuffer pids = new StringBuffer();
		pids.append("/");
		if (StringUtils.isNotBlank(regVo.getInviteCode())) {
			String registerInviteCode = F.me().getSysConfigValueByCode("REGISTER_INVITE_CODE");
			if (StringUtils.isBlank(registerInviteCode) || !registerInviteCode.equals(regVo.getInviteCode())) {
				Member director = F.me().getMember(null, regVo.getInviteCode());
				if (director == null) {
					return Result.fail(ApiStatus.NOT_DIRECTOR);
				}
				pids.append(director.getMemberId()).append(director.getParentRefereeId());
				member.setRefereeId(director.getMemberId());
			}
		}
		member.setPassword(password)
				.setSalt(salt).setValidStatus("N")
				.setParentRefereeId(pids.toString())
				.setType(MemberType.V0.code())
				.setUid(uid)
				.setIsOpenPay("N")
				.setInviteCode(inviteCode)
				.setRegisterTime(new Date())
				.setCreateUser(SYS_PLATFORM);


		memberService.save(member);
		Member resultMemberQ_ = new Member();
		resultMemberQ_.setUid(uid);
		Member resultMember = memberService.getOne(new QueryWrapper<>(resultMemberQ_));

		//后台创建账号
		createUserAccount(resultMember, regVo.getPassword());

		/**
		 * 是否有钱包，没有则创建一个钱包
		 */
		generateWallet(resultMember);
		//计算推荐佣金
		//calculateReferralCommission(member);
		//删除验证码
		redisUtil.del(SMS + regVo.getAccount());
		return success();
	}

	//计算推荐佣金
	public void calculateReferralCommission(Member member) {
		String coin = F.me().cfg(TEAM_BONUS_COIN);
		String teamNotBonusRecord = F.me().cfg(TEAM_NOT_BONUS_RECORD);
		//关系链
		String parentRefereeId = member.getParentRefereeId(); //默认一个/
		if (parentRefereeId.length() > 1) { //有上级
			//List<Config> configs = configService.selectByConditionTeamNoPage();

			List<TeamLevel> list = teamLevelService.list();
			if (list.isEmpty()) return;
			Map<Integer, String> studentMap = list.stream().collect(Collectors.toMap(TeamLevel::getLevel, TeamLevel::getNumber));

			//Map<Integer,Config> conMap = new HashedMap(10);
//            for (int i = 0; i < configs.size(); i++) {
//                Config config = configs.get(i);
//                if(config.getCode().equals("TEN_BONUS_RATIO")){ conMap.put(10, config); }
//                if(config.getCode().equals("NINE_BONUS_RATIO")){ conMap.put(9, config); }
//                if(config.getCode().equals("EIGHT_BONUS_RATIO")){ conMap.put(8, config); }
//                if(config.getCode().equals("SEVEN_BONUS_RATIO")){ conMap.put(7, config); }
//                if(config.getCode().equals("SIX_BONUS_RATIO")){ conMap.put(6, config); }
//                if(config.getCode().equals("FIVE_BONUS_RATIO")){ conMap.put(5, config); }
//                if(config.getCode().equals("FOUR_BONUS_RATIO")){ conMap.put(4, config); }
//                if(config.getCode().equals("THREE_BONUS_RATIO")){ conMap.put(3, config); }
//                if(config.getCode().equals("TWO_BONUS_RATIO")){ conMap.put(2, config); }
//                if(config.getCode().equals("ONE_BONUS_RATIO")){conMap.put(1, config);}
//
//            }
			// 按照/分割
			String[] split = parentRefereeId.split("/");
			//十级内计算一级佣金
			if (split.length <= list.size() && split.length != 0) {
				for (String s : split) {
					if (StringUtils.isNotBlank(s)) {
						long mId = Long.valueOf(s);
						Member m = memberService.getById(mId);
						String record = null;
						//保存邀请佣金记录
						TeamEarnings teamEarnings = new TeamEarnings();
						teamEarnings.setType(coin);
						if (member.getRefereeId().equals(mId)) {
							record = studentMap.get(split.length - 1);
							teamEarnings.setDirect(Constant.Y);
						} else {
							record = teamNotBonusRecord;
							teamEarnings.setDirect(Constant.N);
						}
						QueryWrapper<TeamEarning> teamEarningQueryWrapper = new QueryWrapper<>();
						teamEarningQueryWrapper.eq("member_id", m.getMemberId());
						TeamEarning one = teamEarningService.getOne(teamEarningQueryWrapper);
						BigDecimal re = new BigDecimal(record);
						if (one == null) {
							one = new TeamEarning();
							one.setMemberId(m.getMemberId());
							one.setAccount(m.getAccount());
							one.setUsedPrice(re)
									.setDel(Constant.N)
									.setBlockedPrice(BigDecimal.ZERO)
									.setWithdrawPrice(BigDecimal.ZERO).setCreateUser(1L);
						} else {
							BigDecimal usedPrice = one.getUsedPrice();
							BigDecimal add = usedPrice.add(re);
							one.setUsedPrice(add);
						}
						one.setType(coin);
						teamEarnings.setDel(Constant.N).setPrice(re)
								.setRegistId(member.getMemberId())
								.setRegistAccount(member.getAccount())
								.setEarningsId(m.getMemberId()).setEarningsAccount(m.getAccount()).setCreateUser(1L);
						one.setUpdateUser(1L);
						teamEarningsService.saveOrUpdate(teamEarnings);
						teamEarningService.saveOrUpdate(one);
					}
				}
			}

//            Config config = conMap.get(split.length);
//
//            split = reverse(split);
//            for (int i=0;i>split.length-1;i--){
//                if(i>9){
//                    break;
//                }
//                Long memberId = Long.parseLong(split[i]);
//                Member byId = memberService.getById(memberId);
//                Config config = conMap.get(i+1);
//                //查询邀请钱包
//                QueryWrapper<TeamEarning> teamEarningQueryWrapper = new QueryWrapper<>();
//                teamEarningQueryWrapper.eq("member_id", member);
//                TeamEarning one = teamEarningService.getOne(teamEarningQueryWrapper);
//                BigDecimal s = new BigDecimal(config.getValue());
//                if(one==null){
//                    one = new TeamEarning();
//                    one.setMemberId(memberId);
//                    one.setAccount(byId.getAccount());
//                    one.setUsedPrice(s)
//                            .setDel(Constant.N)
//                            .setBlockedPrice(BigDecimal.ZERO)
//                            .setWithdrawPrice(BigDecimal.ZERO);
//                }else{
//                    BigDecimal usedPrice = one.getUsedPrice();
//                    BigDecimal add = usedPrice.add(s);
//                    one.setUsedPrice(add);
//                }
//                //保存邀请佣金记录
//                TeamEarnings teamEarnings = new TeamEarnings();
//                if(i == 0){
//                    teamEarnings  .setDirect(Constant.Y);
//                }
//                teamEarnings.setDel(Constant.N).setPrice(s)
//                        .setRegistId(member.getMemberId())
//                        .setRegistAccount(member.getAccount())
//                .setEarningsId(memberId).setEarningsAccount(byId.getAccount());
//                teamEarningsService.save(teamEarnings);
//                teamEarningService.saveOrUpdate(one);
//            }
		}
	}

	private String[] reverse(String[] arr) {
		//遍历数组
		for (int i = 0; i < arr.length / 2; i++) {
			//交换元素 因为i从0开始所以这里一定要再减去1
			String temp = arr[arr.length - i - 1];
			arr[arr.length - i - 1] = arr[i];
			arr[i] = temp;
		}//返回反转后的结果
		return arr;
	}

	private void createUserAccount(Member member, String password) {
		//创建后台账号
		User theUser = this.userService.getByAccount(member.getAccount());
		if (theUser != null) {
			throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
		}

		// 完善账号信息
		String userSalt = ShiroKit.getRandomSalt(5);
		String userpassword = ShiroKit.md5(password, userSalt);

		User user = new User();
		user.setCreateTime(new Date());
		user.setStatus(ManagerStatus.OK.getCode());
		user.setPassword(userpassword);
		user.setSalt(userSalt);
		user.setMemberId(member.getMemberId());
		user.setAccount(member.getAccount());
		user.setName(member.getAccount());
		user.setCreateUser(1L);
		user.setDeptId(0L);
		this.userService.save(user);
		Role role = new Role();
		role.setDescription(SYSTEMPROXYNAME);
		Role one = roleService.getOne(new QueryWrapper<>(role));
		if (one != null) {
			String roleIds = one.getRoleId().toString();
			this.userService.setRoles(user.getUserId(), roleIds);
		}

	}

	public Result checkInviteCode(String inviteCode) {
		if (StringUtils.isNotBlank(inviteCode)) {
			String registerInviteCode = F.me().getSysConfigValueByCode("REGISTER_INVITE_CODE");
			if (StringUtils.isBlank(registerInviteCode) || !registerInviteCode.equals(inviteCode)) {
				if (registerInviteCode.equals(inviteCode)) {
					return Result.success();
				}
				Member director = F.me().getMember(null, inviteCode);
				if (director != null) {
					return Result.success();
				}
			} else if (registerInviteCode.equals(inviteCode)) {
				return Result.success();
			}

		}
		return Result.fail(ApiStatus.NOT_DIRECTOR);
	}

	@Transactional(rollbackFor = Exception.class)
	public Result login(LoginVo loginVo) {
		//先判断账号是否存在
		Member accountQ = new Member();
		if (!loginVo.getAccount().matches(emailMatch)) {
			accountQ.setPhone(loginVo.getAccount());
		} else {
			accountQ.setEmail(loginVo.getAccount());
		}
		accountQ.setDel("N");

		Member resultMember = memberService.getOne(new QueryWrapper(accountQ));
		if (resultMember == null) return fail(ApiStatus.NOT_FIND_USER);
		if (resultMember.getStatus().equals("N")) return fail(ApiStatus.DISABLE_ACCOUNT);

		//验证密码
		String salt = resultMember.getSalt();
		String checkPwd = ShiroKit.md5(loginVo.getPassword(), salt);
		if (!StrUtil.equals(checkPwd, resultMember.getPassword())) return fail(ApiStatus.VERIFY);

		/**
		 * 生成钱包地址
		 */
		ThreadPoolUtil.execute(
				() -> {
//					generateRechargeAddress(resultMember);
					generateChainAddress(resultMember);
				}
		);
		//单账号登录
		if (redisUtil.get(SINGLE_ACCOUNT + resultMember.getAccount()) != null) {
			//清除之前的token
			String oldToken = (String) redisUtil.get(SINGLE_ACCOUNT + resultMember.getAccount());
			redisUtil.del(oldToken);
		}
		String token = TOKEN + IdUtil.simpleUUID();

		resultMember.setLastLogin(new Date()).setUpdateUser(resultMember.getMemberId());
		resultMember.setLoginMethod("PHONE");
		if (loginVo.getAccount().contains("@")) {
			resultMember.setLoginMethod("EMAIL");
		}
		memberService.updateById(resultMember);
		redisUtil.set(token, resultMember, Long.parseLong(F.me().cfg(TOKEN_EXPIRE)));
		redisUtil.set(SINGLE_ACCOUNT + resultMember.getAccount(), token, Long.parseLong(F.me().cfg(TOKEN_EXPIRE)));

		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
//        List<Swap> swapList = F.me().getSwaps("Y");
//        for (Swap swap : swapList){
//            //刷新合约用户账户
		jobService.refreshContractInfo(resultMember.getMemberId(), "USDT");
//        }
		updateWalletAndCurrency(resultMember);

		return success("登录成功", map);
	}

	private void updateWalletAndCurrency(Member m) {
		Spot spot = new Spot();
		spot.setDel("N");
		spot.setStatus("Y");
		List<Spot> spotList = this.spotService.list(new QueryWrapper<>(spot));
		for (Spot spot1 : spotList) {
			String coin = spot1.getSymbol();
			coin = coin.split("/")[0];
			if ("USDT-ERC20".equals(coin)) continue;
			Wallet wallet = F.me().getWallet(m.getMemberId(), coin);
			if (wallet == null) {
				wallet = new Wallet();
				wallet.setMemberId(m.getMemberId());
				wallet.setType(coin);
				wallet.setUsedPrice(BigDecimal.ZERO);
				wallet.setLockedPrice(BigDecimal.ZERO);
				wallet.setCreateUser(m.getMemberId());
				wallet.setCreateTime(new Date());
				wallet.setUpdateUser(m.getMemberId());
				walletService.save(wallet);
			}
			Currency currency = F.me().getCurrency(m.getMemberId(), coin);
			if (currency == null) {
				currency = new Currency();
				currency.setMemberId(m.getMemberId())
						.setType(coin)
						.setCreateUser(m.getMemberId());
				currency.setCreateTime(new Date());
				currency.setUsedPrice(BigDecimal.ZERO);
				currency.setLockedPrice(BigDecimal.ZERO);
				currency.setUpdateUser(m.getMemberId());
				currencyService.save(currency);
			}
			FinMining mining = F.me().getMining(m.getMemberId(), coin);
			if (mining == null) {
				mining = new FinMining();
				mining.setMemberId(m.getMemberId())
						.setType(coin)
						.setCreateUser(m.getMemberId());
				mining.setCreateTime(new Date());
				mining.setUsedPrice(BigDecimal.ZERO);
				mining.setLockedPrice(BigDecimal.ZERO);
				mining.setUpdateUser(m.getMemberId());
				finMiningService.save(mining);
			}
		}
	}

	/**
	 * 生成用户钱包充值地址
	 *
	 * @param member
	 */
	private Member generateChainAddress(Member member) {
		boolean open = false;

		// 判断是否开启钱包开关
		if (StrUtil.equals(F.me().cfg(CHAIN_OPEN), "Y")) {
			open = true;
		}
		if (open) {
			LinkedHashMap<String, WaasCoinType> enumMap = EnumUtil.getEnumMap(WaasCoinType.class);
			enumMap.forEach((key, value) -> {
				MemberRechargeAddress memberRechargeAddressOne = this.memberRechargeAddressService.getOne(Wrappers.<MemberRechargeAddress>lambdaQuery().eq(MemberRechargeAddress::getMemberId, member.getMemberId()).last("limit 1"));
				MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
				memberRechargeAddress = this.memberRechargeAddressService.getOne(Wrappers.<MemberRechargeAddress>lambdaQuery().eq(MemberRechargeAddress::getMemberId, member.getMemberId()).eq(MemberRechargeAddress::getCoin,value.getChainCoin()));
				String walletId = null;
				String walletName = null;
				// 判断用户有没有生成过钱包
				if (memberRechargeAddressOne == null) {
					// 生成钱包
					ResultData<List<ApiWaasWalletInfoVO>> batchWallet = waasApiClient.getBatchWallet(member.getMemberId().toString());
					if (batchWallet.getCode() == 200) {
						walletId = batchWallet.getData().get(0).getWalletId();
						walletName = batchWallet.getData().get(0).getWalletName();
					}
				}else {
					walletId = memberRechargeAddressOne.getWalletId();
					walletName = memberRechargeAddressOne.getWalletName();
				}
				// 判断用户有没有生成过地址
				if (memberRechargeAddress == null && walletId != null) {
					log.info("用户:{}，没有生成过{}地址，开始生成", member.getAccount(), value.getChainName());
					// 为钱包生成不同链上的地址
					ResultData<List<WaasAddressVo>> addressResultData = waasApiClient.generateChainAddresses(walletId, value.getChainName());
					if (addressResultData.getCode() != 200) {
						return;
					}
					String address = addressResultData.getData().get(0).getAddress();
					log.info("用户：{}，生成结果：walletId:{},walletName{},address{}",member.getMemberId(), walletId, walletName, address);
					memberRechargeAddress = new MemberRechargeAddress();
					memberRechargeAddress.setMemberId(member.getMemberId());
					memberRechargeAddress.setCoin(value.getChainCoin());
					memberRechargeAddress.setWalletId(walletId);
					memberRechargeAddress.setWalletName(walletName);
					memberRechargeAddress.setAddress(address);
					memberRechargeAddress.setCreateTime(new Date());
					this.memberRechargeAddressService.save(memberRechargeAddress);
				}
			});
			// EOS和XRP用不上 https://support.uduncloud.com/#/article-detail?groupId=103&id=14
			/*for (String coin : coinSet) {
				MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
				memberRechargeAddress.setMemberId(member.getMemberId());
				memberRechargeAddress.setCoin(symbols[0]);
				memberRechargeAddress = this.memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
				if (memberRechargeAddress == null) {
					if (StringUtils.isNotBlank(spot1.getCode()) && (!"EOS".equals(spot1.getSymbol()) || !"XRP".equals(spot1.getSymbol()))) {
						Address address = biPayService.createCoinAddress(spot1.getCode(), "", spot1.getWalletCode());
						System.out.println(JSONObject.toJSON(address));
						if (address != null) {
							memberRechargeAddress = new MemberRechargeAddress();
							memberRechargeAddress.setMemberId(member.getMemberId());
							memberRechargeAddress.setCoin(symbols[0]);
							memberRechargeAddress.setAddress(address.getAddress());
							memberRechargeAddress.setCreateTime(new Date());
							this.memberRechargeAddressService.save(memberRechargeAddress);
						}
					}
				}
			}*/
		}
		return member;
	}

	/**
	 * 生成充币地址
	 */
	/*private void generateRechargeAddress(Member member) {
		boolean open = false;
		if (StrUtil.equals(F.me().cfg(CHAIN_OPEN), "Y")) {
			open = true;
		}
		if (open) {
//			Spot spot = new Spot();
//			spot.setDel("N");
//			spot.setStatus("Y");
//			spot.setType("0");
//			spot.setSymbol("USDT-ERC20/USDT");
//			List<Spot> spotList = spotService.list(new QueryWrapper<>(spot));

			HashSet<String> hashSet = new HashSet<>();
			hashSet.add("")



			for (String coin : hashSet) {
//				String[] symbols = spot1.getSymbol().split("/");
				MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
				memberRechargeAddress.setMemberId(member.getMemberId());
				memberRechargeAddress.setCoin(coin);
				memberRechargeAddress = this.memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
				if (memberRechargeAddress == null) {


					//创建钱包地址
					JSONObject httpPost = HttpUtils.setTransactionByTxid(memberCode,
							key,
							secret,
							"ETH_USDT",
							member.getMemberId(),
							createUrl);
					log.info("创建钱包地址返回信息：" + httpPost);
					String status = httpPost.getString("status");
					if ("200".equals(status)) {
						String data = httpPost.getString("data");
						memberRechargeAddress = new MemberRechargeAddress();
						memberRechargeAddress.setMemberId(member.getMemberId());
						memberRechargeAddress.setCoin(symbols[0]);
						memberRechargeAddress.setAddress(data);
						memberRechargeAddress.setCreateTime(new Date());
						this.memberRechargeAddressService.save(memberRechargeAddress);
					} else {
						log.info("创建USDT钱包出错：" + httpPost);
					}
				}
			}

            spot = new Spot();
            spot.setDel("N");
            spot.setStatus("Y");
            spot.setType("0");
            spot.setSymbol("AT/USDT");
            spot = spotService.getOne(new QueryWrapper<>(spot));
            if (spot != null) {
                String[] symbols = spot.getSymbol().split("/");
                MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
                memberRechargeAddress.setMemberId(member.getMemberId());
                memberRechargeAddress.setCoin(symbols[0].toUpperCase());
                memberRechargeAddress = this.memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
                if (memberRechargeAddress == null) {
                    //创建钱包地址
                    JSONObject httpPost = HttpUtils.setTransactionByTxid(memberCode,
                            key,
                            secret,
                            symbols[0].toUpperCase(),
                            member.getMemberId(),
                            createUrl);
                    log.info("创建AT代币地址返回信息：" + httpPost);
                    if (httpPost != null) {
                        String status = httpPost.getString("status");
                        if ("200".equals(status)) {
                            String data = httpPost.getString("data");
                            memberRechargeAddress = new MemberRechargeAddress();
                            memberRechargeAddress.setMemberId(member.getMemberId());
                            memberRechargeAddress.setCoin(symbols[0].toUpperCase());
                            memberRechargeAddress.setAddress(data);
                            memberRechargeAddress.setCreateTime(new Date());
                            this.memberRechargeAddressService.save(memberRechargeAddress);
                        } else {
                            log.info("创建AT代币地址出错：" + httpPost);
                        }
                    }
                }
            }
		}
	}*/
	private static String getFixLenthString(int strLength) {
		Random rm = new Random();
		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);
		// 返回固定的长度的随机数
		return fixLenthString.substring(1, strLength + 1);
	}

	/**
	 * 是否有钱包，没有则创建一个钱包
	 */
	private void generateWallet(Member m) {
		//钱包资产生成
		genWallet(m);
		//合约资产生成
		genContract(m);
		//合约资产生成
		genFinFutures(m);
		//币币资产生成
		genCurrency(m);
		//法币资产生成
		genLegal(m);
		//期权资产生成
		genOption(m);
		genMining(m);
		jobService.refreshContractInfo(m.getMemberId(), "USDT");
	}

	public void genOption(Member m) {
		List<ContractOptionCoin> spotList = F.me().getOptions(null);
		//币种类型
		for (ContractOptionCoin spot : spotList) {
			String coin = spot.getSymbol().split("/")[1];
			if ("USDT-TRC20".equals(coin) || "USDT-ERC20".equals(coin) || "USDT-OMNI".equals(coin)) {
				coin = "USDT";
			}
			FinOption query = F.me().getFinOption(m.getMemberId(), coin);
			if (query != null) {
				finOptionService.removeById(query.getCurrencyId());
			}
			FinOption wallet = new FinOption();
			wallet.setMemberId(m.getMemberId())
					.setType(coin)
					.setCreateUser(m.getMemberId());
			wallet.setUpdateUser(m.getMemberId());
			wallet.setLockedPrice(BigDecimal.ZERO);
			wallet.setUsedPrice(BigDecimal.ZERO);
			finOptionService.save(wallet);
		}
	}

	//法币资产生成
	private void genLegal(Member m) {
		String sysConfigValueByCode = F.me().getSysConfigValueByCode(Constant.OTC_COIN);
		String[] coins = sysConfigValueByCode.split(",");
		//List<Spot> spotList = F.me().getSpots(null);
		for (String coin : coins) {
			if ("USDT-TRC20".equals(coin) || "USDT-ERC20".equals(coin) || "USDT-OMNI".equals(coin)) {
				coin = "USDT";
			}
			Legal query = F.me().getLegal(m.getMemberId(), coin);
			if (query != null) {
				legalService.removeById(query.getLegalId());
			}
			Legal contract = new Legal();
			contract.setMemberId(m.getMemberId())
					.setType(coin)
					.setCreateUser(m.getMemberId());
			contract.setUpdateUser(m.getMemberId());
			contract.setUsedPrice(BigDecimal.ZERO);
			contract.setLockedPrice(BigDecimal.ZERO);
			legalService.save(contract);
		}
	}

	//币币资产生成
	private void genCurrency(Member m) {
		List<Spot> spotList = F.me().getSpots(null);
		//币种类型
		for (Spot spot : spotList) {
			String coin = spot.getSymbol().split("/")[0];
			if ("USDT-TRC20".equals(coin) || "USDT-ERC20".equals(coin) || "USDT-OMNI".equals(coin)) {
				coin = "USDT";
			}
			Currency query = F.me().getCurrency(m.getMemberId(), coin);
			if (query != null) {
				currencyService.removeById(query.getCurrencyId());
			}
			Currency wallet = new Currency();
			wallet.setMemberId(m.getMemberId())
					.setType(coin)
					.setCreateUser(m.getMemberId());
			wallet.setUpdateUser(m.getMemberId());
			wallet.setLockedPrice(BigDecimal.ZERO);
			wallet.setUsedPrice(BigDecimal.ZERO);
			currencyService.save(wallet);
		}
	}

	//币币资产生成
	private void genMining(Member m) {
		List<Spot> spotList = F.me().getSpots(null);
		//币种类型
		for (Spot spot : spotList) {
			String coin = spot.getSymbol().split("/")[0];
			if ("USDT-TRC20".equals(coin) || "USDT-ERC20".equals(coin) || "USDT-OMNI".equals(coin)) {
				coin = "USDT";
			}
			FinMining query = F.me().getMining(m.getMemberId(), coin);
			if (query != null) {
				finMiningService.removeById(query.getMiningId());
			}
			FinMining wallet = new FinMining();
			wallet.setMemberId(m.getMemberId())
					.setType(coin)
					.setCreateUser(m.getMemberId());
			wallet.setUpdateUser(m.getMemberId());
			wallet.setLockedPrice(BigDecimal.ZERO);
			wallet.setUsedPrice(BigDecimal.ZERO);
			finMiningService.save(wallet);
		}
	}

	//合约资产生成
	private void genContract(Member m) {
		List<Swap> swapList = F.me().getSwaps(null);
		for (Swap swap : swapList) {
			String coin = swap.getSymbol().split("/")[0];
			if ("USDT-TRC20".equals(coin) || "USDT-ERC20".equals(coin) || "USDT-OMNI".equals(coin)) {
				coin = "USDT";
			}
			Contract query = F.me().getContract(m.getMemberId(), coin);
			if (query != null) {
				contractService.removeById(query.getContractId());
			}
			Contract contract = new Contract();
			contract.setMemberId(m.getMemberId());
			contract.setType(coin);
			contract.setUsedPrice(BigDecimal.ZERO);
			contract.setEntrustPrice(BigDecimal.ZERO);
			contract.setPositionPrice(BigDecimal.ZERO);
			contract.setCreateUser(m.getMemberId());
			contract.setUpdateUser(m.getMemberId());
			contractService.save(contract);
		}


	}

	//黄金资产生成
	private void genFinFutures(Member m) {
		List<Futures> swapList = F.me().getFuturess(null);
		for (Futures swap : swapList) {
			//String coin = swap.getSymbol().split("/")[0];
			String coin = swap.getSymbol().split("/")[1];
			if ("USDT-TRC20".equals(coin) || "USDT-ERC20".equals(coin) || "USDT-OMNI".equals(coin)) {
				coin = "USDT";
			}
			FinFutures query = F.me().getFinFutures(m.getMemberId(), coin);
			if (query != null)
				continue;
			FinFutures contract = new FinFutures();
			contract.setMemberId(m.getMemberId());
			contract.setType(coin);
			contract.setUsedPrice(BigDecimal.ZERO);
			contract.setEntrustPrice(BigDecimal.ZERO);
			contract.setPositionPrice(BigDecimal.ZERO);
			contract.setCreateUser(m.getMemberId());
			contract.setUpdateUser(m.getMemberId());
			finFuturesService.save(contract);
		}
	}

	//钱包资产生成
	private void genWallet(Member m) {
		List<Spot> spotList = F.me().getSpots(null);
		//币种类型
		for (Spot spot : spotList) {
			String coin = spot.getSymbol().split("/")[0];
			if ("USDT-TRC20".equals(coin) || "USDT-ERC20".equals(coin) || "USDT-OMNI".equals(coin)) {
				coin = "USDT";
			}
			Wallet query = F.me().getWallet(m.getMemberId(), coin);
			if (query != null) {
				walletService.removeById(query.getWalletId());
			}
			Wallet wallet = new Wallet();
			wallet.setMemberId(m.getMemberId());
			wallet.setType(coin);
			wallet.setUsedPrice(BigDecimal.ZERO);
			wallet.setLockedPrice(BigDecimal.ZERO);
			wallet.setMortgagePrice(BigDecimal.ZERO);
			wallet.setFinancesPrice(BigDecimal.ZERO);
			wallet.setCreateUser(m.getMemberId());
			wallet.setCreateTime(new Date());
			wallet.setUpdateUser(m.getMemberId());
			walletService.save(wallet);
		}
	}

	public Result forgetPwd(ForgetVo forgetVo) {
		Member queryMember = new Member();
		queryMember.setAccount(forgetVo.getAccount())
				.setStatus("Y")
				.setDel("N");
		Member resultMember = memberService.getOne(new QueryWrapper<>(queryMember));
		if (resultMember == null) {
			return fail(ApiStatus.NOT_FIND_USER);
		}
		//判断两次密码是否一致
		if (!StrUtil.equals(forgetVo.getPassword(), forgetVo.getConfirmPwd())) {
			return fail(ApiStatus.DIFF_PWD);
		}
		//校验验证码是否正确
		if (!forgetVo.getAccount().contains("@")) {
			//sms
			if (!F.me().cfg(SMSSENDOPEN).equals("N")) {
				if (!checkMsg(forgetVo.getMsg(), forgetVo.getAccount())) {
					return fail(ApiStatus.MSG_ERROR);
				}
			}
		} else {
			//email
			if (!F.me().cfg(EMAILSENDOPEN).equals("N")) {
				if (!checkMsg(forgetVo.getMsg(), forgetVo.getAccount())) {
					return fail(ApiStatus.MSG_ERROR);
				}
			}
		}


		// 完善账号信息
		String salt = ShiroKit.getRandomSalt(5);
		String password = ShiroKit.md5(forgetVo.getPassword(), salt);
		resultMember.setSalt(salt)
				.setPassword(password)
				.setUpdateTime(new Date());
		resultMember.setUpdatePwdTime(new Date());
		resultMember.setUpdateUser(-1L);
		return memberService.updateById(resultMember) ? success("重置成功", null) : fail(ApiStatus.ERROR);
	}

	/**
	 * 手机验证码校验
	 *
	 * @param msg   手机验证码
	 * @param phone 手机号码
	 * @return boolean
	 */
	private boolean checkMsg(String msg, String phone) {
		if (redisUtil.get(SMS + phone) == null) {
			return false;
		}
		String msgCode = (String) redisUtil.get(SMS + phone);
		return StrUtil.equals(msg, msgCode);
	}

	/**
	 * 用户协议
	 *
	 * @return Result
	 */
	public Result declares(String language) {
		Declares declares = new Declares();
		declares.setLanguage(language);

		List<Declares> list = declaresService.list(new QueryWrapper<>(declares));
		if (list.size() > 0) {
			return success("用户协议", list.get(0).getContent());
		}
		return success("暂无用户协议，请联系管理员", null);
	}

	public Result version(@Valid VersionVo versionVo) {

		Map<String, Object> map = new HashMap<>();
		AppVersion query = new AppVersion();
		query.setType(versionVo.getType());
		AppVersion versionR = versionService.getOne(new QueryWrapper<>(query));
		if (versionR == null) {
			return fail(ApiStatus.BAD_REQUEST);
		}
		if (versionR.getVersion().equals(versionVo.getVersion())) {
			map.put("update", "N");
		} else {
			map.put("update", "Y");
		}
		map.put("content", versionR.getContent());
		map.put("address", versionR.getAddress());
		map.put("version", versionR.getVersion());
		return success("返回结果", map);
	}

	public Result logout(String token) {
		redisUtil.del(token);
		return success();
	}

	public Result areaList_() {
		Region region = new Region();
		region.setDel("N");
		List<Region> list = regionService.list(new QueryWrapper<>(region));
		return success(list);
	}

	/**
	 * 获取手机验证码
	 * has getMsg2()
	 *
	 * @param code
	 * @param phone_
	 * @return
	 */
	public Result getMsg(String token, String code, String phone_, Long type, String account) {
		int number = (int) ((Math.random() * 9 + 1) * 100000);
		String content = F.me().cfg(SMS_CONTENT);
		content = String.format(content, number);
		if (StrUtil.equals(token, "-1") && StrUtil.equals(account, "1")) {
			return fail(ApiStatus.BAD_REQUEST);
		}
		boolean phoneType = false;
		//查账号
		if (StrUtil.equals(token, "-1")) {
			if (!account.contains("@")) {
				phoneType = true;
				PhoneCode phoneCode = new PhoneCode();
				phoneCode.setCode(code);
				phoneCode.setDel("N");
				phoneCode = this.phoneCodeService.getOne(new QueryWrapper<>(phoneCode));
				if (phoneCode != null) {
					type = Long.valueOf(phoneCode.getType());
				}
			} else {
				phoneType = false;
			}
		} else if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(account)) {
			if (!account.contains("@")) {
				phoneType = true;
			} else {
				phoneType = false;
			}
			if (redisUtil.get(token) != null) {
				Member member = (Member) redisUtil.get(token);
				if ("PHONE".equals(member.getLoginMethod())) {
					code = member.getPhoneCode();
					type = Long.valueOf(member.getArea());
				}
			}
			if (phoneType) {
				PhoneCode phoneCode = new PhoneCode();
				phoneCode.setCode(code);
				phoneCode.setDel("N");
				phoneCode = this.phoneCodeService.getOne(new QueryWrapper<>(phoneCode));
				if (phoneCode != null) {
					type = Long.valueOf(phoneCode.getType());
				}
			}
		} else {//查token
			Member member = (Member) redisUtil.get(token);
			account = member.getEmail();
			phoneType = false;
			if ("PHONE".equals(member.getLoginMethod())) {
				account = member.getPhone();
				code = member.getPhoneCode();
				type = Long.valueOf(StrUtil.isBlank(member.getArea()) ? "0" : member.getArea());
				phoneType = true;
			}
		}

		/**
		 * 手机短信发送
		 */
		if (phoneType) {
			return sendSms(account, code, type, content, number);
		} else {
			content = F.me().cfg("SMS_EMAIL_CONTENT");
			content = String.format(content, number);
			log.info(content);
			return sendEmailAlipay(account, content, number);
		}
	}

	public Result sysMsg() {
		Object obj = redisUtil.get("SYS_PHONE");
		if (obj == null) {
			return fail(404, "系统安全电话未配置，请联系管理员");
		}
		int number = (int) ((Math.random() * 9 + 1) * 100000);
		String content = F.me().cfg(SMS_CONTENT);
		content = String.format(content, number);
		String account = obj.toString();

		return sendSms(account, "86", 1L, content, number);
	}

	public Result getMsg(String code, String phone, Long type, String email) {
		Result result;
		/**
		 * 手机短信发送
		 */
		if ((!phone.contains("@")) && (!phone.equals("1"))) {
			boolean isSend = smsUtil.sendSms(code, phone);
			result = isSend ? success("发送成功", null) : fail(ApiStatus.ERROR_MESSAGE);
		} else {
			int number = (int) ((Math.random() * 9 + 1) * 100000);
			String content = F.me().cfg("SMS_EMAIL_CONTENT");
			content = String.format(content, number);
			return sendEmailAlipay(email, content, number);
		}
		return result;
	}

	/**
	 * 邮箱发送
	 */
	private Result sendEmail(String account, String content, int number) {
		//String emailKey = F.me().cfg("EMAIL_KEY");
		//String emailSecret = F.me().cfg("EMAIL_SECRET");
		//String emailFromAlias = F.me().cfg("EMAIL_FROM_ALIAS");
		//主题
		String emailSubject = F.me().cfg("EMAIL_SUBJECT");
		//协议
		String emailProtocol = F.me().cfg("EMAIL_PROTOCOL");

		//发送账号
		String emailAccount = F.me().cfg("EMAIL_ACCOUNT");
		//授权码
		String emailKey = F.me().cfg("EMAIL_KEY");
		//服务器
		String emailHost = F.me().cfg("EMAIL_HOST");
		if (F.me().cfg(SMS_OPEN).equals("Y")) {
			int ccc = EmailUtils.sendEmail(emailHost, emailAccount, emailKey, emailProtocol, account, emailSubject, number);
			if (ccc != 200) {
				return fail(ApiStatus.ERROR_MESSAGE);
			}
			redisUtil.set(SMS + account, String.valueOf(number), EMAIL_TIMEOUT);
			return success();
		} else {
			redisUtil.set(SMS + account, String.valueOf(123456), EMAIL_TIMEOUT);
			return success("本地测试，发送成功", null);
		}
	}

	/**
	 * 阿里云邮箱
	 *
	 * @param account 目标账号
	 * @param content 账号内容
	 * @return
	 */
	private Result sendEmailAlipay(String account, String content, Integer num) {
		//主题
		String emailSubject = F.me().cfg("EMAIL_SUBJECT");

		//发送账号
		String emailAccount = F.me().cfg("EMAIL_ACCOUNT");
		//授权码
		String emailKey = F.me().cfg("EMAIL_KEY");

		//昵称
		String emailNickname = F.me().cfg("EMAIL_FROM_ALIAS");
		if (F.me().cfg(EMAIL_OPEN).equals("Y")) {
			String s = SampleMail.sendMail(emailAccount, emailKey, account, emailSubject, content, emailNickname);
			redisUtil.incr(Constant.EMAIL_COUNT, 1);
			System.out.println(s);
			if (s != null) {
//                return Result.fail(400,"发送失败！！！请检查邮箱是否正确!");
				return Result.fail(ApiStatus.ERROR_MESSAGE);
			}
			redisUtil.set(SMS + account, num.toString(), EMAIL_TIMEOUT);
			return success("发送成功", null);
		} else {
			redisUtil.set(SMS + account, String.valueOf(123456), EMAIL_TIMEOUT);
			return success("本地测试，发送成功", null);
		}
	}

	/**
	 * 阿里云邮箱
	 *
	 * @param account 目标账号
	 * @param content 账号内容
	 * @return
	 */
	private Result sendEmailAlipay(String account, String content) {
		//主题
		String emailSubject = F.me().cfg("EMAIL_SUBJECT");

		//发送账号
		String emailAccount = F.me().cfg("EMAIL_ACCOUNT");
		//授权码
		String emailKey = F.me().cfg("EMAIL_KEY");

		//昵称
		String emailNickname = F.me().cfg("EMAIL_FROM_ALIAS");

		String s = SampleMail.sendMail(emailAccount, emailKey, account, emailSubject, content, emailNickname);
		redisUtil.incr(Constant.EMAIL_COUNT, 1);
		System.out.println(s);
		if (s != null) {
			return Result.fail(ApiStatus.ERROR_MESSAGE);
		}
		return success();

	}

	/**
	 * 邮箱发送
	 */
	public void sendEmail(String account, String content) {
		String emailKey = F.me().cfg("EMAIL_KEY");
		String emailSecret = F.me().cfg("EMAIL_SECRET");
		String emailFromAlias = F.me().cfg("EMAIL_FROM_ALIAS");
		String emailSubject = F.me().cfg("EMAIL_SUBJECT");
		EmailUtils.sendEmailUser(account, content, emailKey, emailSecret, emailFromAlias, emailSubject);
	}

	private Result sendSms(String account, String code, Long type, String content, int number) {
		if (F.me().cfg(SMS_OPEN).equals("Y")) {
			String smsAccount = account;
			if (type > 0) {
				smsAccount = "+" + code + account;
			}
			if (sendSMSExtService.sendSmsDXB(content, smsAccount, type)) {
				redisUtil.set(SMS + account, String.valueOf(number), SMS_TIMEOUT);
				return success("发送成功", null);
			}
		} else {
			redisUtil.set(SMS + account, String.valueOf(123456), SMS_TIMEOUT);
			return success("本地测试，发送成功", null);
		}
		return fail(ApiStatus.ERROR_MESSAGE);
	}

	/**
	 * 发送短信/邮件：用户有手机就先发送手机短信。没有则发送邮件
	 *
	 * @param member  用户
	 * @param content 内容
	 */
	public void sendMsgWithPhoneOrEmail(Member member, String content) {
		if (StrUtil.isNotBlank(member.getPhone())) {
			sendSMSExtService.sendSms(content, member.getPhone(), Long.valueOf(member.getArea()));
		} else {
			sendEmailAlipay(member.getEmail(), content);
		}
	}

	/**
	 * 刷新token
	 *
	 * @param token
	 * @return
	 */
	public Result refreshToken(String token) {
		Member member = (Member) redisUtil.get(token);
		redisUtil.del(token);
		String newToken = TOKEN + IdUtil.simpleUUID();
		redisUtil.set(newToken, member, Long.parseLong(F.me().cfg(TOKEN_EXPIRE)));
		Map<String, Object> map = new HashMap<>();
		map.put("newToken", newToken);
		return success("新token", map);
	}

	/**
	 * 安全中心
	 *
	 * @param token
	 * @param apiPayPwdVo
	 * @return
	 */
	public Result pwd(String token, ApiPayPwdVo apiPayPwdVo) {
		Member member = (Member) redisUtil.get(token);
		if (StrUtil.equals(apiPayPwdVo.getType(), "LOGIN")) {
			if (StringUtils.isBlank(apiPayPwdVo.getNewPwd())) {
				return Result.fail(ApiStatus.NO_NULL_NEW_PWD);
			}
			if (StringUtils.isBlank(apiPayPwdVo.getConfirmPwd())) {
				return Result.fail(ApiStatus.NO_NULL_CONFIRM_PWD);
			}
			if (StringUtils.isBlank(apiPayPwdVo.getMsgOrOldPwd())) {
				return Result.fail(ApiStatus.NO_NULL_OLD_PWD);
			}
			if (!StrUtil.equals(apiPayPwdVo.getNewPwd(), apiPayPwdVo.getConfirmPwd())) {
				return Result.fail(ApiStatus.DIFF_PWD);
			}
			//修改登录密码
			return updatePassword(member, apiPayPwdVo, token);

		}
		if (StrUtil.equals(apiPayPwdVo.getType(), "PAY")) {
			if (StringUtils.isBlank(apiPayPwdVo.getNewPwd())) {
				return Result.fail(ApiStatus.NO_NULL_PAY_PWD);
			}
			if (StringUtils.isBlank(apiPayPwdVo.getConfirmPwd())) {
				return Result.fail(ApiStatus.NO_NULL_CONFIRM_PWD);
			}
			if (!StrUtil.equals(apiPayPwdVo.getNewPwd(), apiPayPwdVo.getConfirmPwd())) {
				return Result.fail(ApiStatus.DIFF_PWD);
			}
			//修改交易密码
			return updatePay(member, apiPayPwdVo);
		}
		return fail(ApiStatus.BAD_REQUEST.code());
	}

	/**
	 * 修改登录密码
	 *
	 * @param member
	 * @param apiPayPwdVo
	 * @return
	 */
	private Result updatePassword(Member member, ApiPayPwdVo apiPayPwdVo, String token) {
		String oldPwdmd5 = ShiroKit.md5(apiPayPwdVo.getMsgOrOldPwd(), member.getSalt());
		if (!StrUtil.equals(oldPwdmd5, member.getPassword())) {
			return fail(ApiStatus.OLD_ERROR);
		}
		String newSalt = ShiroKit.getRandomSalt(5);
		String newPwdmd5 = ShiroKit.md5(apiPayPwdVo.getNewPwd(), newSalt);
		member.setPassword(newPwdmd5).setSalt(newSalt).setUpdateUser(member.getMemberId());
		//修改密码后24小时无法提币
		member.setUpdatePwdTime(new Date());
		memberService.updateById(member);
		//  setNotWithDraw(member);
		redisUtil.del(token);
		return success("Modification successful", null);
	}

	//修改密码后24小时无法提币
	private void setNotWithDraw(Member member) {
		redisUtil.set(NOT_WITHDRAW + member.getMemberId(), "1", 24 * 60 * 60);
	}

	/**
	 * 修改交易密码
	 *
	 * @param member
	 * @param apiPayPwdVo
	 * @return
	 */
	private Result updatePay(Member member, ApiPayPwdVo apiPayPwdVo) {
		/**
		 * 验证码校验
		 */
//		boolean equals = "N".equals(F.me().cfg(SMSSENDOPEN));
//		boolean equals2 = "N".equals(F.me().cfg(EMAILSENDOPEN));
//
//		if (!equals && !equals2) {
//			String pMsg = (String) redisUtil.get(SMS + member.getPhone());
//			if (pMsg == null) {
//				pMsg = (String) redisUtil.get(SMS + member.getEmail());
//			}
//			// String eMsg = (String) redisUtil.get(SMS + member.getEmail());
//			if (!StrUtil.equals(pMsg, apiPayPwdVo.getMsgOrOldPwd())) {
//				return fail(ApiStatus.MSG_ERROR);
//			}
//		}

		String newSalt = ShiroKit.getRandomSalt(5);
		String newPwdmd5 = ShiroKit.md5(apiPayPwdVo.getNewPwd(), newSalt);
		member.setPayPassword(newPwdmd5).setPaySalt(newSalt).setUpdateUser(member.getMemberId());
		member.setIsOpenPay("Y");
		memberService.updateById(member);
		Object o = redisUtil.get(Constant.SINGLE_ACCOUNT + member.getAccount());
		if (o != null) {
			String token = (String) o;
			redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
		}
		redisUtil.del(SMS + member.getAccount());
		return success("Successfully changed trading password", null);
	}

	/**
	 * 签到信息
	 *
	 * @param token
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Result checkIn(String token) {

		Member member = (Member) redisUtil.get(token);

		if (isTodayCheck(member.getMemberId())) {
			return fail(ApiStatus.CHECK_TODAY_ERROR);
		}

		CheckIn checkIn = new CheckIn();
		checkIn.setMemberId(member.getMemberId())
				.setCreateUser(member.getMemberId());

		//TODO:签到奖励逻辑
		//获取用户本周签到信息
		int count = 0;
		Calendar calendar = Calendar.getInstance();
		List<CheckIn> checkInList = checkInService.getBaseMapper().getCheckListByMemberId(member.getMemberId());
		boolean isToday = false;
		if (checkInList.size() > 0) {
			for (CheckIn entity : checkInList) {
				//获取时间实例
				Instant instant = entity.getCreateTime().toInstant();
				//获取时间地区ID
				ZoneId zoneId = ZoneId.systemDefault();
				//转换为LocalDate
				LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
				//获得LocalDateTime时间戳(东八区)
				localDateTime.toEpochSecond(ZoneOffset.of("+8"));
				LocalDate localDate = localDateTime.toLocalDate();
				LocalDate now = LocalDate.now();
				if (localDate.getDayOfMonth() == now.getDayOfMonth()) {
					isToday = true;
				}
			}


			if (isToday) {
				for (CheckIn checkIn2 : checkInList) {
					if (calendar.getTime().getDate() == checkIn2.getCreateTime().getDate()) {
						count++;
						calendar.add(Calendar.DAY_OF_MONTH, -1);
					} else {
						break;
					}
				}
			} else {//没有签到
				for (CheckIn checkIn2 : checkInList) {
					if (calendar.getTime().getDate() == checkIn2.getCreateTime().getDate() + 1) {
						count++;
						calendar.add(Calendar.DAY_OF_MONTH, -1);
					} else {
						break;
					}
				}
			}

		}
		BigDecimal jiangli = BigDecimal.ZERO;
		String query = null;
		count++;
		switch (count) {
			case 1:
				query = "ONE_DAY_REWARD";
				break;
			case 2:
				query = "TWO_DAY_REWARD";
				break;
			case 3:
				query = "THREE_DAY_REWARD";
				break;
			case 4:
				query = "FOUR_DAY_REWARD";
				break;
			case 5:
				query = "FIVE_DAY_REWARD";
				break;
			case 6:
				query = "SIX_DAY_REWARD";
				break;
			case 7:
				query = "SEVEN_DAY_REWARD";
				break;
		}
		String reward = F.me().cfg(query);
		if (StringUtils.isNotBlank(reward)) {
			jiangli = new BigDecimal(reward);
		}
		String reward_coin = F.me().cfg("REWARD_COIN");
		checkIn.setPrice(jiangli).setCoin(reward_coin).setNum(checkIn.getNum() != null ? checkIn.getNum() + 1 : 1);

		Wallet wallet = F.me().getWallet(member.getMemberId(), reward_coin);
		if (wallet != null) {
			BigDecimal usedPrice = wallet.getUsedPrice();
			wallet.setUsedPrice(usedPrice.add(jiangli));
			F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.WALLET_REWART,
					jiangli, reward_coin, jiangli, reward_coin, BigDecimal.ZERO, reward_coin,
					ItemCode.USED, reward_coin, null, null,
					usedPrice, wallet.getUsedPrice(), member.getMemberId(), member.getMemberId());
			wallet.setUpdateTime(calendar.getTime());
			wallet.setUpdateUser(SYS_PLATFORM);
			walletService.updateById(wallet);
		}
		checkInService.save(checkIn);
		return success("签到成功", null);

	}

	/**
	 * 今日是否签到
	 *
	 * @param memberId
	 * @return
	 */
	private boolean isTodayCheck(Long memberId) {
		int count = checkInService.getBaseMapper().isTodayCheck(memberId);
		return count > 0 ? true : false;
	}

	/**
	 * 签到信息
	 *
	 * @param token
	 * @return
	 */
	public Result checkInfo(String token) {

		Calendar calendar = Calendar.getInstance();
		Member member = (Member) redisUtil.get(token);
		//获取用户本周签到信息
		List<CheckIn> checkInList = checkInService.getBaseMapper().getCheckListByMemberId(member.getMemberId());

		Map<String, Object> map = new HashMap<>();

		List<Integer> days = new ArrayList<>();


		map.put("isToday", "N");
		int count = 0;
		if (checkInList.size() > 0) {
			checkInList.forEach(entity ->
			{
				//获取时间实例
				Instant instant = entity.getCreateTime().toInstant();
				//获取时间地区ID
				ZoneId zoneId = ZoneId.systemDefault();
				//转换为LocalDate
				LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
				//获得LocalDateTime时间戳(东八区)
				localDateTime.toEpochSecond(ZoneOffset.of("+8"));
				LocalDate localDate = localDateTime.toLocalDate();
				days.add(localDate.getDayOfMonth());

				LocalDate now = LocalDate.now();
				if (localDate.getDayOfMonth() == now.getDayOfMonth()) {
					map.put("isToday", "Y");
				}
			});

			boolean flag = map.get("isToday").equals("Y");
			if (flag) {
				for (CheckIn checkIn : checkInList) {
					if (calendar.getTime().getDate() == checkIn.getCreateTime().getDate()) {
						count++;
						calendar.add(Calendar.DAY_OF_MONTH, -1);
					} else {
						break;
					}
				}
			} else {//没有签到
				for (CheckIn checkIn : checkInList) {
					if (calendar.getTime().getDate() == checkIn.getCreateTime().getDate() + 1) {
						count++;
						calendar.add(Calendar.DAY_OF_MONTH, -1);
					} else {
						break;
					}
				}
			}

		}
		map.put("count", count);
		map.put("days", days);
		map.put("awardInfo", F.me().cfg(CHECK_IN_AWARD));
		return success("签到信息", map);
	}

	public Result media(Page page, String type) {

		Media media = new Media();
		media.setType(type).setDel("N");

		return success(mediaService.page(page, new QueryWrapper<>(media).orderByDesc(CREATE_TIME)));
	}

	public Result cashflow(String token, Page page, String type, String symbols, String flowType, String languageType) {
		Member member = (Member) redisUtil.get(token);
		Cashflow cashflow = new Cashflow();
		cashflow.setDel("N")
				.setMemberId(member.getMemberId())
				.setWalletType(type).setItemCode(ItemCode.USED.code);
		if (StringUtils.isNotBlank(flowType)) {
			cashflow.setFlowType(flowType);
		}
		if (StringUtils.isNotBlank(symbols)) {
			cashflow.setFlowCoin(symbols);
		}
		String msg = "流水记录";
		Map<String, Object> map = new HashMap<>();
		ApiRecordDto<IPage> apiRecordDto = new ApiRecordDto<>();
		IPage dtoList = geCashList(cashflow, page, type, msg, languageType);
		apiRecordDto.setRecords(dtoList).setPageType(type);
		return success(msg, apiRecordDto);
	}

	private IPage geCashList(Cashflow cashflow, Page page, String type, String msg, String languageType) {
		IPage<Cashflow> list = cashflowService.page(page, new QueryWrapper<>(cashflow).notIn("flow_type", CashFlowTypeEnum.PLATFORM_ADD.getCode(), CashFlowTypeEnum.PLATFORM_SUB.getCode()).orderByDesc(CREATE_TIME));
		List<Cashflow> tmpList = list.getRecords();
		IPage<ApiCashflowDto> dtoList = new Page<>();
		dtoList.setRecords(new ArrayList<>(tmpList.size()));
		tmpList.stream().forEach(entity ->
		{
			ApiCashflowDto dto = new ApiCashflowDto();
			BeanUtil.copyProperties(entity, dto);
			dto.setCreateTimeStamp(entity.getCreateTime().getTime())
					.setPageType(type)
					.setSymbol(entity.getFlowOp().intValue() == CashFlowOpEnum.FLOW_IN.code.intValue() ? "+" : "-");
			for (CashFlowTypeEnum e : CashFlowTypeEnum.values()) {
				if (StrUtil.equals(e.getCode(), entity.getFlowType())) {
					switch (languageType) {
						case "1":
							dto.setFlowType(e.getValue());
							break;
						case "2":
							dto.setFlowType(e.getHkValue());
							break;
						case "3":
							dto.setFlowType(e.getHgValue());
							break;
						case "4":
							dto.setFlowType(e.getJpValue());
							break;
						default:
							dto.setFlowType(e.getUsValue());
							break;
					}
					dto.setFType(e.getCode());
				}
			}
//            if (type.equals(CashFlowTypeEnum.CONVERT_POINT.getCode()))
//            {
//                dto.setName(entity.getItemName());
//            }
			if (entity.getItemCode() != null && entity.getItemCode().equals(FROZEN)) {

			} else {
				dtoList.getRecords().add(dto);
			}

		});
		dtoList.setTotal(list.getTotal()).setCurrent(list.getCurrent()).setPages(list.getPages()).setSize(list.getSize());
		return dtoList;
	}

	/**
	 * 用户钱包地址
	 *
	 * @param token
	 * @return
	 */
	public Result coin(String token, ApiCoinDto dto) {
		String msg = "";
		Member member = (Member) redisUtil.get(token);
		Coin chainCoin = new Coin();
		switch (dto.getOp()) {
			case ADD:
				BeanUtil.copyProperties(dto, chainCoin);
				chainCoin.setMemberId(member.getMemberId())
						.setStatus(dto.getCoin())
						.setCreateUser(member.getMemberId());
				coinService.save(chainCoin);
				msg = "添加成功";
				break;
			case DEL:
				chainCoin = coinService.getById(dto.getChainCoinId());
				chainCoin.setDel("Y").setUpdateUser(member.getMemberId());
				coinService.updateById(chainCoin);
				msg = "删除成功";
				break;
		}
		return success();
	}

	/**
	 * 币种列表
	 *
	 * @param token
	 * @param page
	 * @return
	 */
	public Result coinList(String token, String coin, Page page) {
		Member member = (Member) redisUtil.get(token);
		Coin chainCoin = new Coin();
		chainCoin.setDel("N").setStatus(coin).setMemberId(member.getMemberId());
		IPage<Coin> list = coinService.page(page, new QueryWrapper<>(chainCoin).orderByDesc(CREATE_TIME));
		List<Coin> tmpList = list.getRecords();
		IPage<ApiCoinDto> dtoList = new Page<>();
		dtoList.setRecords(new ArrayList<>(tmpList.size()));
		tmpList.stream().forEach(entity ->
		{
			ApiCoinDto dto = new ApiCoinDto();
			BeanUtil.copyProperties(entity, dto);
			dtoList.getRecords().add(dto);
		});
		dtoList.setTotal(list.getTotal()).setCurrent(list.getCurrent()).setPages(list.getPages()).setSize(list.getSize());
		return success("列表", dtoList);
	}

	/**
	 * 充币/提幣 记录
	 *
	 * @param token
	 * @param type
	 * @param page
	 * @return
	 */
	public Result coinRecord(String token, String type, Page page, String symbol) {
		Member member = (Member) redisUtil.get(token);
		Result result = null;
		switch (type) {
			case "R":
				result = rechargeRecord(member, page, symbol);
				break;
			case "W":
				result = withdrawRecord(member, page, symbol);
				break;
			case "T":
				result = transferRecord(member, page);
				break;
			case "C":
				result = convertRecord(member, page);
				break;
			default:
				result = fail(ApiStatus.BAD_REQUEST);
		}

		return result;
	}

	private Result convertRecord(Member member, Page page) {
		Convert convertQ = new Convert();
		convertQ.setMemberId(member.getMemberId()).setDel("N");
		//拉取列表
		IPage<Convert> list = convertService.page(page, new QueryWrapper<>(convertQ).orderByDesc(CREATE_TIME));
		List<Convert> tmpList = list.getRecords();
		IPage<ApiChainDto> dtoList = new Page<>();
		dtoList.setRecords(new ArrayList<>(tmpList.size()));
		tmpList.stream().forEach(entity ->
		{
			ApiChainDto dto = new ApiChainDto();
			BeanUtil.copyProperties(entity, dto);
			dto.setId(entity.getConvertId())
					.setType("兑换RCC冻结资产")
					.setCoin(entity.getType())
					.setRemark(entity.getRemark())
					.setAddress(entity.getToAddress())
					.setTime(entity.getCreateTime())
					.setActualPrice(entity.getToPrice())
					.setFee(entity.getFee().multiply(new BigDecimal(entity.getRemark())))
					.setTimeStamp(entity.getCreateTime().getTime())
					.setStatus("已完成")
			;
			dtoList.getRecords().add(dto);
		});
		dtoList.setTotal(list.getTotal()).setCurrent(list.getCurrent()).setPages(list.getPages()).setSize(list.getSize());
		return success("兑换记录", dtoList);
	}

	private Result transferRecord(Member member, Page page) {
		Transfer chainWithdrawQ = new Transfer();
		chainWithdrawQ.setMemberId(member.getMemberId()).setDel("N");
		//拉取列表
		IPage<Transfer> list = transferService.page(page, new QueryWrapper<>(chainWithdrawQ).orderByDesc(CREATE_TIME));
		List<Transfer> tmpList = list.getRecords();
		IPage<ApiChainDto> dtoList = new Page<>();
		dtoList.setRecords(new ArrayList<>(tmpList.size()));
		tmpList.stream().forEach(entity ->
		{
			ApiChainDto dto = new ApiChainDto();
			BeanUtil.copyProperties(entity, dto);
			dto.setId(entity.getTransferId())
					.setType("转账")
					.setCoin(entity.getType())
					.setRemark(entity.getRemark())
					.setAddress(entity.getToAddress())
					.setTime(entity.getCreateTime())
					.setTimeStamp(entity.getCreateTime().getTime());
			for (WithdrawStatusEnum e : WithdrawStatusEnum.values()) {
				if (StrUtil.equals(entity.getStatus(), e.getCode())) {
					dto.setStatus(e.getValue());
				}
			}
			dtoList.getRecords().add(dto);
		});
		dtoList.setTotal(list.getTotal()).setCurrent(list.getCurrent()).setPages(list.getPages()).setSize(list.getSize());
		return success("转账记录", dtoList);
	}

	private Result withdrawRecord(Member member, Page page, String symbol) {
		Withdraw chainWithdrawQ = new Withdraw();
		chainWithdrawQ.setMemberId(member.getMemberId()).setDel("N");
		if (StringUtils.isNotBlank(symbol)) {
			chainWithdrawQ.setType(symbol);
		}
		//拉取列表
		IPage<Withdraw> list = withdrawService.page(page, new QueryWrapper<>(chainWithdrawQ).orderByDesc(CREATE_TIME));
		List<Withdraw> tmpList = list.getRecords();
		IPage<ApiChainDto> dtoList = new Page<>();
		dtoList.setRecords(new ArrayList<>(tmpList.size()));
		tmpList.stream().forEach(entity -> {
			ApiChainDto dto = new ApiChainDto();
			BeanUtil.copyProperties(entity, dto);
			dto.setId(entity.getWithdrawId())
					.setType("提币")
					.setTxHash(entity.getTxHash())
					.setCoin(entity.getType())
					.setRemark(entity.getRemark())
					.setAddress(entity.getToAddress())
					.setTime(entity.getCreateTime())
					.setTimeStamp(entity.getCreateTime().getTime());
			for (WithdrawStatusEnum e : WithdrawStatusEnum.values()) {
				if (StrUtil.equals(entity.getStatus(), e.getCode())) {
					dto.setStatus(e.getValue());
				}
			}
			dtoList.getRecords().add(dto);
		});
		dtoList.setTotal(list.getTotal()).setCurrent(list.getCurrent()).setPages(list.getPages()).setSize(list.getSize());
		return success("提币记录", dtoList);
	}

	private Result rechargeRecord(Member member, Page page, String symbols) {
		Recharge chainRechargeQ = new Recharge();
		chainRechargeQ.setMemberId(member.getMemberId()).setDel("N");
		if (StringUtils.isNotBlank(symbols)) {
			chainRechargeQ.setType(symbols);
		}
		//拉取列表
		IPage<Recharge> list = rechargeService.page(page, new QueryWrapper<>(chainRechargeQ).orderByDesc(CREATE_TIME));
		List<Recharge> tmpList = list.getRecords();
		IPage<ApiChainDto> dtoList = new Page<>();
		dtoList.setRecords(new ArrayList<>(tmpList.size()));
		tmpList.stream().forEach(entity -> {
			ApiChainDto dto = new ApiChainDto();
			dto.setId(entity.getRechargeId())
					.setTime(entity.getCreateTime())
					.setRemark(entity.getRemark())
					.setType("充币")
					.setTimeStamp(entity.getCreateTime().getTime())
					.setPrice(entity.getPrice())
					.setCoin(entity.getType())
					.setActualPrice(entity.getActualPrice())
					.setAddress(entity.getTxHash())
					.setStatus(entity.getStatus())
			;
			if ("USDT-ERC20".equals(entity.getType()) || "USDT-TRC20".equals(entity.getType()) || "USDT-OMNI".equals(entity.getType())) {
				dto.setCoin("USDT");
			}
			for (WithdrawStatusEnum e : WithdrawStatusEnum.values()) {
				if (StrUtil.equals(entity.getStatus(), e.getCode())) {
					dto.setStatus(e.getValue());
				}
			}
			dtoList.getRecords().add(dto);
		});
		dtoList.setTotal(list.getTotal()).setCurrent(list.getCurrent()).setPages(list.getPages()).setSize(list.getSize());
		return success("充币记录", dtoList);
	}

	/**
	 * 充币/提幣 详情
	 *
	 * @param token
	 * @param type
	 * @param id
	 * @return
	 */
	public Result coinRecordDetail(String token, String type, Long id) {
		Member member = (Member) redisUtil.get(token);
		if (StrUtil.equals(type, "W")) {
			Withdraw entity = withdrawService.getById(id);
			if (entity == null) {
				return fail(ApiStatus.NOT_FOUND.code(), "未找到");
			}
			if (entity != null && entity.getMemberId().longValue() != member.getMemberId().longValue()) {
				return fail(ApiStatus.NOT_FOUND.code(), "未找到");
			}
			ApiChainDto dto = new ApiChainDto();
			dto.setId(entity.getWithdrawId())
					.setTime(entity.getCreateTime())
					.setType("提币")
					.setTimeStamp(entity.getCreateTime().getTime())
					.setPrice(entity.getPrice())
					.setFee(entity.getFee())
					.setRemark(entity.getRemark())
					.setActualPrice(entity.getActualPrice())
					.setAddress(entity.getToAddress());
			if (StrUtil.equals(entity.getStatus(), WithdrawStatusEnum.PASS.getCode())) {
				dto.setStatus("提币成功");
			} else if (StrUtil.equals(entity.getStatus(), WithdrawStatusEnum.COIN.getCode())) {
				dto.setStatus("提币中");
			} else if (StrUtil.equals(entity.getStatus(), WithdrawStatusEnum.CHECK.getCode())) {
				dto.setStatus("审核中");
			} else {
				dto.setStatus("提币失败");
			}
			return success("提币详情", dto);
		}
		return fail(ApiStatus.BAD_REQUEST.code(), ApiStatus.BAD_REQUEST.msg());
	}

	/**
	 * 提币
	 *
	 * @param token
	 * @param apiWithdrawCoinVo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Result withdrawCoin(String token, ApiWithdrawCoinVo apiWithdrawCoinVo) {
		//提币币种
		String coin = apiWithdrawCoinVo.getType();
		Member member = (Member) redisUtil.get(token);
		if (!("1").equals(member.getRealStatus())) {
			return fail(ApiStatus.NOT_REAL);
		}
		if ("N".equals(member.getIsOpenPay())) {
			return Result.fail(ApiStatus.NOT_SETTING_PAYPWD);
		}
//        if (StringUtils.isBlank(member.getPhone())){
//            return  Result.fail(ApiStatus.NOT_BING_PHONE);
//        }
//        if (StringUtils.isBlank(member.getEmail())){
//            return  Result.fail(ApiStatus.NOT_BING_EMAIL);
//        }
		if (member.getUpdatePwdTime() != null && (new Date().getTime() - member.getUpdatePwdTime().getTime()) < 24 * 60 * 60 * 1000)
			return fail(ApiStatus.NOT_WITHDRAW);

		Spot spot = F.me().getSpot(coin + "/USDT");
		BigDecimal feeRatio = spot != null ? spot.getWithdrawFee().divide(BigDecimal.valueOf(100), 8, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
		BigDecimal fee = apiWithdrawCoinVo.getPrice().multiply(feeRatio);
		//>= min >= max,在区间就要手机验证码
		if (apiWithdrawCoinVo.getPrice().compareTo(spot.getMinWithdrawValue()) >= 0
				&& apiWithdrawCoinVo.getPrice().compareTo(spot.getMaxWithdrawValue()) <= 0) {
			if (!F.me().cfg(SMSSENDOPEN).equals("N")) {
				if (StringUtils.isBlank(apiWithdrawCoinVo.getSmsCode()))
					return Result.fail(ApiStatus.NOT_NULL_SMS_CODE);
				if (redisUtil.get(SMS + member.getPhone()) == null) {
					return Result.fail(ApiStatus.ERROR_SMS_CODE);
				}
				String oldSmsCode = (String) redisUtil.get(SMS + member.getPhone());
				if (!oldSmsCode.equals(apiWithdrawCoinVo.getSmsCode())) {
					return Result.fail(ApiStatus.ERROR_SMS_CODE);
				}
			}


		}
		// > max  就要手机邮箱验证码
		if (apiWithdrawCoinVo.getPrice().compareTo(spot.getMaxWithdrawValue()) > 0) {
			if (!F.me().cfg(SMSSENDOPEN).equals("N")) {
				if (StringUtils.isBlank(apiWithdrawCoinVo.getSmsCode())) {
					return Result.fail(ApiStatus.NOT_NULL_SMS_CODE);
				}

				if (redisUtil.get(SMS + member.getPhone()) == null) {
					return Result.fail(ApiStatus.ERROR_SMS_CODE);
				}
				String oldSmsCode = (String) redisUtil.get(SMS + member.getPhone());
				if (!oldSmsCode.equals(apiWithdrawCoinVo.getSmsCode())) {
					return Result.fail(ApiStatus.ERROR_SMS_CODE);
				}
			}
			if (!F.me().cfg(EMAILSENDOPEN).equals("N")) {
				if (StringUtils.isBlank(apiWithdrawCoinVo.getEmailCode())) {
					return Result.fail(ApiStatus.NOT_NULL_EMAIL_CODE);
				}
				if (redisUtil.get(SMS + member.getEmail()) == null) {
					return Result.fail(ApiStatus.ERROR_EMAIL_CODE);
				}
				String oldEmail = (String) redisUtil.get(SMS + member.getEmail());
				if (!oldEmail.equals(apiWithdrawCoinVo.getEmailCode())) {
					return Result.fail(ApiStatus.ERROR_EMAIL_CODE);
				}
			}
		}
		String coinValue = coin;
		if ("USDT-ERC20".equals(coin) || "USDT-TRC20".equals(coin) || "USDT-OMNI".equals(coin)) {
			coinValue = "USDT";
		}
		Wallet wallet = F.me().getWallet(member, coinValue);
		//判断是否内部转账的
		boolean isInclude = false;
		MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
		memberRechargeAddress.setCoin(apiWithdrawCoinVo.getType());
		memberRechargeAddress.setAddress(apiWithdrawCoinVo.getToAddress());
		if ("EOS".equals(apiWithdrawCoinVo.getType()) || "XRP".equals(apiWithdrawCoinVo.getType())) {
			memberRechargeAddress.setRemark(apiWithdrawCoinVo.getMemoTagValue());
		}
		memberRechargeAddress = memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
		if (memberRechargeAddress != null) {
			if (memberRechargeAddress.getMemberId().equals(member.getMemberId())) {
				return Result.fail(ApiStatus.NO_TO_MYSELF);
			}
			isInclude = true;
			fee = BigDecimal.ZERO;
		}
		if (!isInclude && apiWithdrawCoinVo.getPrice().compareTo(fee) < 0)
			return fail(ApiStatus.FEE_LESS);

		if (wallet.getUsedPrice().compareTo(apiWithdrawCoinVo.getPrice()) == -1)
			return fail(ApiStatus.WALLET_LESS);

		if (!isTruePayPwd(member, apiWithdrawCoinVo.getPayPwd()))
			return fail(ApiStatus.ERROR_PAY_PWD);

		BigDecimal minWithdrawNum = spot.getMinWithdraw();
		if (apiWithdrawCoinVo.getPrice().compareTo(minWithdrawNum) == -1)
			return fail(ApiStatus.MIN_WITHDRAW_NUM);

		Withdraw chainWithdraw = new Withdraw();
		String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("CW");
		//实际提币
		BigDecimal actualPrice = apiWithdrawCoinVo.getPrice().subtract(fee);
		chainWithdraw.setMemberId(member.getMemberId())
				.setOrderNo(orderNo)
				.setPrice(apiWithdrawCoinVo.getPrice())
				.setActualPrice(actualPrice)
				.setFee(fee)
				.setStatus(WithdrawStatusEnum.CHECK.getCode())
				.setCreateUser(member.getMemberId());
		chainWithdraw.setType(apiWithdrawCoinVo.getType());
		chainWithdraw.setToAddress(apiWithdrawCoinVo.getToAddress());
		chainWithdraw.setRemark(apiWithdrawCoinVo.getMemoTagValue());
		if (isInclude) {//内部转账
			chainWithdraw.setStatus(WithdrawStatusEnum.IN_TRANFER.getCode());
			withdrawService.save(chainWithdraw);
			//扣除转成钱包余额
			BigDecimal beforePrice = wallet.getUsedPrice();
			wallet.setUsedPrice(wallet.getUsedPrice().subtract(apiWithdrawCoinVo.getPrice()))
					.setUpdateUser(member.getMemberId());
			walletService.updateById(wallet);
			//转入他人钱包的可用余额中
			Wallet toInWallet = new Wallet();
			toInWallet.setMemberId(memberRechargeAddress.getMemberId());
			toInWallet.setType(apiWithdrawCoinVo.getType());
			toInWallet = this.walletService.getOne(new QueryWrapper<>(toInWallet));
			BigDecimal toBeforePrice = toInWallet.getUsedPrice();
			toInWallet.setUsedPrice(toInWallet.getUsedPrice().add(apiWithdrawCoinVo.getPrice()))
					.setUpdateUser(member.getMemberId());
			walletService.updateById(toInWallet);
			//保存转成流水记录
			F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.TRANSFER_IN
					, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
					, ItemCode.USED, coin, null, null,
					beforePrice, wallet.getUsedPrice()
					, member.getMemberId(), member.getMemberId());
			//保存转入流水记录
			F.me().saveCashflow(toInWallet.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.TRANSFER_IN
					, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
					, ItemCode.USED, coin, null, null,
					toBeforePrice, toInWallet.getUsedPrice()
					, toInWallet.getMemberId(), toInWallet.getMemberId());


			return success();
		}
		try {
			WebSocketService.sendInfo(WITHDRAWCOINSTRING, SYSTEMSOCKETSENDNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		withdrawService.save(chainWithdraw);

		wallet.setUsedPrice(wallet.getUsedPrice().subtract(apiWithdrawCoinVo.getPrice()))
				.setUpdateUser(member.getMemberId());
		wallet.setLockedPrice(wallet.getLockedPrice().add(apiWithdrawCoinVo.getPrice()));
		walletService.updateById(wallet);

		//流水记录
		F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.WITHDRAW
				, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
				, ItemCode.USED, coin, null, null,
				wallet.getUsedPrice(), wallet.getUsedPrice().subtract(apiWithdrawCoinVo.getPrice())
				, member.getMemberId(), member.getMemberId());

		F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.WITHDRAW
				, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
				, ItemCode.LOCKED, coin, null, null,
				wallet.getLockedPrice(), wallet.getLockedPrice().add(apiWithdrawCoinVo.getPrice())
				, member.getMemberId(), member.getMemberId());

		return success("提币审核中", null);
	}

	/**
	 * 提币
	 *
	 * @param token
	 * @param apiWithdrawCoinVo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Result withdrawCoinApp(String token, ApiWithdrawCoinVo apiWithdrawCoinVo) {
		//提币币种
		String coin = apiWithdrawCoinVo.getType();
		Member member = (Member) redisUtil.get(token);
		if ("N".equals(member.getIsOpenPay())) {
			return Result.fail(ApiStatus.NOT_SETTING_PAYPWD);
		}
//        if (StringUtils.isBlank(member.getPhone())){
//            return  Result.fail(ApiStatus.NOT_BING_PHONE);
//        }
//        if (StringUtils.isBlank(member.getEmail())){
//            return  Result.fail(ApiStatus.NOT_BING_EMAIL);
//        }
		if (member.getUpdatePwdTime() != null && (new Date().getTime() - member.getUpdatePwdTime().getTime()) < 24 * 60 * 60 * 1000)
			return fail(ApiStatus.NOT_WITHDRAW);

		Spot spot = F.me().getSpot(coin + "/USDT");
		BigDecimal feeRatio = spot != null ? spot.getWithdrawFee().divide(BigDecimal.valueOf(100), 8, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
		BigDecimal fee = apiWithdrawCoinVo.getPrice().multiply(feeRatio);

		String coinValue = coin;
		if ("USDT-ERC20".equals(coin) || "USDT-TRC20".equals(coin) || "USDT-OMNI".equals(coin)) {
			coinValue = "USDT";
		}
		Wallet wallet = F.me().getWallet(member, coinValue);
		//判断是否内部转账的
		boolean isInclude = false;
		MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
		memberRechargeAddress.setCoin(apiWithdrawCoinVo.getType());
		memberRechargeAddress.setAddress(apiWithdrawCoinVo.getToAddress());
		if ("EOS".equals(apiWithdrawCoinVo.getType()) || "XRP".equals(apiWithdrawCoinVo.getType())) {
			memberRechargeAddress.setRemark(apiWithdrawCoinVo.getMemoTagValue());
		}
		memberRechargeAddress = memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
		if (memberRechargeAddress != null) {
			if (memberRechargeAddress.getMemberId().equals(member.getMemberId())) {
				return Result.fail(ApiStatus.NO_TO_MYSELF);
			}
			isInclude = true;
			fee = BigDecimal.ZERO;
		}
		if (!isInclude && apiWithdrawCoinVo.getPrice().compareTo(fee) < 0)
			return fail(ApiStatus.FEE_LESS);

		if (wallet.getUsedPrice().compareTo(apiWithdrawCoinVo.getPrice()) == -1)
			return fail(ApiStatus.WALLET_LESS);

		if (!isTruePayPwd(member, apiWithdrawCoinVo.getPayPwd()))
			return fail(ApiStatus.ERROR_PAY_PWD);

		BigDecimal minWithdrawNum = spot.getMinWithdraw();
		if (apiWithdrawCoinVo.getPrice().compareTo(minWithdrawNum) == -1)
			return fail(ApiStatus.MIN_WITHDRAW_NUM);

		Withdraw chainWithdraw = new Withdraw();
		String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("CW");
		//实际提币
		BigDecimal actualPrice = apiWithdrawCoinVo.getPrice().subtract(fee);
		chainWithdraw.setMemberId(member.getMemberId())
				.setOrderNo(orderNo)
				.setPrice(apiWithdrawCoinVo.getPrice())
				.setActualPrice(actualPrice)
				.setFee(fee)
				.setStatus(WithdrawStatusEnum.CHECK.getCode())
				.setCreateUser(member.getMemberId());
		chainWithdraw.setType(apiWithdrawCoinVo.getType());
		chainWithdraw.setToAddress(apiWithdrawCoinVo.getToAddress());
		chainWithdraw.setRemark(apiWithdrawCoinVo.getMemoTagValue());
		if (isInclude) {//内部转账
			chainWithdraw.setStatus(WithdrawStatusEnum.IN_TRANFER.getCode());
			withdrawService.save(chainWithdraw);
			//扣除转成钱包余额
			BigDecimal beforePrice = wallet.getUsedPrice();
			wallet.setUsedPrice(wallet.getUsedPrice().subtract(apiWithdrawCoinVo.getPrice()))
					.setUpdateUser(member.getMemberId());
			walletService.updateById(wallet);
			//转入他人钱包的可用余额中
			Wallet toInWallet = new Wallet();
			toInWallet.setMemberId(memberRechargeAddress.getMemberId());
			toInWallet.setType(apiWithdrawCoinVo.getType());
			toInWallet = this.walletService.getOne(new QueryWrapper<>(toInWallet));
			BigDecimal toBeforePrice = toInWallet.getUsedPrice();
			toInWallet.setUsedPrice(toInWallet.getUsedPrice().add(apiWithdrawCoinVo.getPrice()))
					.setUpdateUser(member.getMemberId());
			walletService.updateById(toInWallet);
			//保存转成流水记录
			F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.TRANSFER_IN
					, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
					, ItemCode.USED, coin, null, null,
					beforePrice, wallet.getUsedPrice()
					, member.getMemberId(), member.getMemberId());
			//保存转入流水记录
			F.me().saveCashflow(toInWallet.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.TRANSFER_IN
					, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
					, ItemCode.USED, coin, null, null,
					toBeforePrice, toInWallet.getUsedPrice()
					, toInWallet.getMemberId(), toInWallet.getMemberId());


			return success();
		}
		try {
			WebSocketService.sendInfo(WITHDRAWCOINSTRING, SYSTEMSOCKETSENDNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		withdrawService.save(chainWithdraw);

		wallet.setUsedPrice(wallet.getUsedPrice().subtract(apiWithdrawCoinVo.getPrice()))
				.setUpdateUser(member.getMemberId());
		wallet.setLockedPrice(wallet.getLockedPrice().add(apiWithdrawCoinVo.getPrice()));
		walletService.updateById(wallet);

		//流水记录
		F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.WITHDRAW
				, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
				, ItemCode.USED, coin, null, null,
				wallet.getUsedPrice(), wallet.getUsedPrice().subtract(apiWithdrawCoinVo.getPrice())
				, member.getMemberId(), member.getMemberId());

		F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.WITHDRAW
				, apiWithdrawCoinVo.getPrice(), coin, actualPrice, coin, fee, coin
				, ItemCode.LOCKED, coin, null, null,
				wallet.getLockedPrice(), wallet.getLockedPrice().add(apiWithdrawCoinVo.getPrice())
				, member.getMemberId(), member.getMemberId());

		return success("提币审核中", null);
	}

	/**
	 * 交易密码正确
	 *
	 * @param member      用户obj
	 * @param payPassword 交易密码
	 * @return
	 */
	public boolean isTruePayPwd(Member member, String payPassword) {
		String md5Pwd = ShiroKit.md5(payPassword, member.getPaySalt());
		return StrUtil.equals(member.getPayPassword(), md5Pwd) ? true : false;
	}

	/**
	 * （查询）收货地址
	 *
	 * @param token
	 * @return
	 */
	public Result getShipping(String token) {
		Member member = (Member) redisUtil.get(token);
		Shipping shippingQ = new Shipping();
		shippingQ.setMemberId(member.getMemberId()).setDel("N");
		Shipping shippingR = shippingService.getOne(new QueryWrapper<>(shippingQ));
		ApiShippingVo vo = new ApiShippingVo();
		BeanUtil.copyProperties(shippingR, vo);
		return success("收货信息", vo);
	}

	/**
	 * (增删改)收货地址
	 *
	 * @param token
	 * @param shippingVo
	 * @return
	 */
	public Result shipping(String token, ApiShippingVo shippingVo, String type) {
		Member member = (Member) redisUtil.get(token);
		if (StrUtil.equals(type, "ADD")) {
			Shipping shippingQ = new Shipping();
			shippingQ.setMemberId(member.getMemberId()).setDel("N");
			List shippingR = shippingService.list(new QueryWrapper<>(shippingQ));
			if (shippingR.size() > 0) {
				return fail(ApiStatus.ERROR);
			}
			Shipping shipping = new Shipping();
			BeanUtil.copyProperties(shippingVo, shipping);
			shipping.setMemberId(member.getMemberId()).setCreateUser(member.getMemberId());
			shippingService.save(shipping);
			return success("已保存收货地址", null);
		} else if (StrUtil.equals(type, "EDIT")) {

			Shipping shipping = shippingService.getById(shippingVo.getShippingId());

			BeanUtil.copyProperties(shippingVo, shipping);
			shipping.setMemberId(member.getMemberId()).setUpdateUser(member.getMemberId());
			shippingService.updateById(shipping);
			return success("已修改收货地址", null);
		}
		if (StrUtil.equals(type, "DEL")) {
//            Shipping shipping = shippingService.getById(shippingVo.getShippingId());
//            shipping.setDel("Y").setUpdateUser(member.getMemberId());
//            shippingService.updateById(shipping);
			shippingService.getBaseMapper().deleteById(shippingVo.getShippingId());
			return success("已删除收货地址", null);
		}
		return fail(ApiStatus.BAD_REQUEST);
	}

	public Result withdrawPage(String token, String coin) {
		Member member = (Member) redisUtil.get(token);
		Map<String, Object> map = new HashMap<>();
		String coinValue = coin;
		if ("USDT-ERC20".equals(coin) || "USDT-TRC20".equals(coin) || "USDT-OMNI".equals(coin)) {
			coinValue = "USDT";
		}
		Wallet wallet = F.me().getWallet(member.getMemberId(), coinValue);
		if (wallet == null)
			return fail(ApiStatus.BAD_REQUEST);
		map.put("price", wallet.getUsedPrice().toPlainString());
		Spot spot = F.me().getSpot(coin + "/USDT");
		map.put("fee", spot == null ? 9999 : spot.getWithdrawFee().toPlainString());
		map.put("min", spot == null ? 9999 : spot.getMinWithdraw().toPlainString());
		map.put("minSection", spot == null ? 0 : spot.getMinWithdrawValue().toPlainString());
		map.put("maxSection", spot == null ? 0 : spot.getMaxWithdrawValue().toPlainString());
		return success("提币页面信息", map);
	}

	/**
	 * 用户信息
	 *
	 * @param token
	 * @return Result
	 */
	public Result info(String token) {
		Member member = (Member) redisUtil.get(token);
		ApiUserInfoDto dto = new ApiUserInfoDto();
		BeanUtil.copyProperties(member, dto);
		String h5Url = F.me().getSysConfigValueByCode("H5_URL");
		dto.setHead(F.me().cfg(URL) + dto.getHead())
				.setInviteLink(h5Url + regApi + member.getInviteCode())
				.setUid(member.getUid())
		;
		//http://localhost:8001/#/register?code=123456
		dto.setInviteLink(h5Url + "/#/register?code=" + member.getInviteCode());
		//联系方式
		Contact contact = contactService.getOne(new QueryWrapper<>());
		dto.setContactPhone(contact.getContactName());
		dto.setPaySmsType("PHONE");
		if (member.getAccount().matches(emailMatch)) {
			dto.setPaySmsType("EMAIL");
		}
		dto.setIsOpenPay(member.getIsOpenPay());

		String account = dto.getAccount();
		if (account.contains("@")) {
			dto.setAccount(account.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4"));
		} else {
			//第一个参数是正则表达式，$1匹配第一个括号，$2匹配第二个
			dto.setAccount(account.replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1******$2"));
		}
		return success("用户信息", dto);
	}

	public Result editInfo(String token, ApiUserInfoDto dto) {
		Member member = (Member) redisUtil.get(token);

//        if (StrUtil.isNotBlank(dto.getNickName())) {
//            member.setNickName(dto.getNickName()).setUpdateUser(member.getMemberId());
//            memberService.updateById(member);
//            return success("昵称已更新");
//        }
		if (StrUtil.isNotBlank(dto.getHead())) {
			member.setHead(dto.getHead()).setUpdateUser(member.getMemberId());
			memberService.updateById(member);

			redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));

			return success("头像已更新", null);
		}
		return fail(ApiStatus.BAD_REQUEST.code());
	}

	/**
	 * 获取团队总人数
	 *
	 * @param memberId
	 * @return
	 */
	private int getTeamNumbers(Long memberId) {

		//获取团队总人数
		return memberService.getBaseMapper().getTeamNumbers(memberId);
	}

	private String getMemberTypeValue(String type) {
		for (MemberType m : MemberType.values()) {
			if (StrUtil.equals(type, m.code())) {
				return m.value();
			}
		}
		return "未知等级";
	}

	public Result problem(String type, Page page, String language) {
		Problem problemQ = new Problem();
		problemQ.setLanguage(language);
		problemQ.setRemark(type).setDel(N);
		return success(type, problemService.page(page, new QueryWrapper<>(problemQ).orderByDesc(CREATE_TIME)));
	}

	public Result bindAccount(String token, ApiBindAccountDto dto) {
		Member member = (Member) redisUtil.get(token);
//        if (!isTrueLoginPwd(member, dto.getPwd()))
//            return fail(ApiStatus.ERROR_LOGIN_PWD);
		if (memberService.getBaseMapper().getUserForOtherType(dto.getBindAccount().trim()) != null) {
			return !dto.getBindAccount().matches(emailMatch) ?
					fail(ApiStatus.EXIST_BIND_ACCOUNT_WITH_PHONE) :
					fail(ApiStatus.EXIST_BIND_ACCOUNT_WITH_EMAIL);
		}
		//是否绑定手机
		boolean bindPhone = true;
		//判断绑定账号类型
		if (!dto.getBindAccount().contains("@")) {
			if (!F.me().cfg(SMSSENDOPEN).equals("N")) {
				//绑定手机
				if (StrUtil.isNotBlank(member.getPhone()) || StrUtil.equals(member.getPhone(), dto.getBindAccount()) || StrUtil.equals(member.getAccount(), dto.getBindAccount()))
					return fail(ApiStatus.BAD_REQUEST);
				Object smsCode = redisUtil.get(SMS + dto.getBindAccount());
				if (smsCode == null)
					return fail(ApiStatus.MSG_ERROR);
				if (!StrUtil.equals(dto.getMsg(), smsCode.toString()))
					return fail(ApiStatus.MSG_ERROR);
			}

		} else {
			if (!F.me().cfg(EMAILSENDOPEN).equals("N")) {
				// 绑定的邮箱
				if (StrUtil.isNotBlank(member.getEmail()) || StrUtil.equals(member.getEmail(), dto.getBindAccount()) || StrUtil.equals(member.getAccount(), dto.getBindAccount()))
					return fail(ApiStatus.BAD_REQUEST);
				Object emailCode = redisUtil.get(SMS + dto.getBindAccount());
				if (emailCode == null)
					return fail(ApiStatus.MSG_ERROR);
				if (!StrUtil.equals(dto.getMsg(), emailCode.toString()))
					return fail(ApiStatus.MSG_ERROR);
			}
			bindPhone = false;

		}
		if (bindPhone) {
			member.setPhone(dto.getBindAccount());
		} else {
			member.setEmail(dto.getBindAccount());
		}
		member.setUpdateTime(new Date());
		member.setUpdateUser(member.getMemberId());
		memberService.updateById(member);
		redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
		return success();
	}

	/**
	 * 登录密码是否正确
	 *
	 * @param member 用户
	 * @param pwd    密码
	 * @return
	 */
	private boolean isTrueLoginPwd(Member member, String pwd) {
		return StrUtil.equals(ShiroKit.md5(pwd, member.getSalt()), member.getPassword()) ? true : false;
	}

	/**
	 * @param token
	 * @param dto
	 * @return
	 */
	public Result verify(String token, ApiVerifyDto dto) {
		Member member = (Member) redisUtil.get(token);
		//用户是否实名已认证
		if (StrUtil.equals(member.getRealStatus(), YES))
			return fail(ApiStatus.EXIST_REAL);
		if (StrUtil.equals(member.getRealStatus(), CHECK))
			return fail(ApiStatus.REAL_CHECK);

		Verify verify = new Verify();
		String idCard = dto.getIdCard();
		verify.setIdCard(idCard);
		verify.setStatus(Status.PASS.code);
		int count = verifyService.count(new QueryWrapper<>(verify));
		if (count > 0) {
			return Result.fail(ApiStatus.ERROR_ID_CARD_ONE);
		}
		BeanUtil.copyProperties(dto, verify);
		verify.setMemberId(member.getMemberId())
				.setStatus(Status.CHECK.code)
				.setCreateUser(member.getMemberId());
		verifyService.save(verify);

		member.setRealStatus(CHECK).setUpdateUser(member.getMemberId());
		memberService.updateById(member);


		redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));

		try {
			WebSocketService.sendInfo(VERIFYSTRING, SYSTEMSOCKETSENDNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Result.success(ApiStatus.REAL_CHECK);

	}

	public Result cnyPayList(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		Payment payment = new Payment();
		payment.setMemberId(member.getMemberId()).setDel("N");
		return success("收款方式列表", paymentService.page(page, new QueryWrapper<>(payment).orderByDesc(CREATE_TIME)));
	}

	public Result cnyPayAdd(String token, ApiPayDto dto) {
		Member member = (Member) redisUtil.get(token);
		if ("0".equals(member.getRealStatus())) {
			return fail(ApiStatus.NOT_REAL);
		}
		if ("2".equals(member.getRealStatus())) {
			return fail(ApiStatus.REAL_CHECK);
		}
		if ("CARD".equals(dto.getType()) && StringUtils.isBlank(dto.getBank())) {
			return Result.fail(ApiStatus.NOT_NULL_BANK);
		}
		//查询该账号是否已存在
		Payment oldPayment = new Payment();
		oldPayment.setIdcard(dto.getIdcard());
		oldPayment.setType(dto.getType());
		int count = paymentService.count(new QueryWrapper<>(oldPayment));
		if (count > 0) {
			if ("CARD".equals(dto.getType())) {
				return Result.fail(ApiStatus.NOT_EXIT_CARD_ACCOUNT);
			}
			return Result.fail(ApiStatus.NOT_EXIT_PAY_ACCOUNT);
		}
		Payment payment = new Payment();
		BeanUtil.copyProperties(dto, payment);
		payment.setName(member.getRealName());
		payment.setMemberId(member.getMemberId()).setCreateUser(member.getMemberId());
		paymentService.save(payment);
		return Result.success(ApiStatus.OK);
	}

	public Result cnyPayDel(String token, Long paymentId) {
		Member member = (Member) redisUtil.get(token);
		Payment payment = new Payment();
		payment.setMemberId(member.getMemberId()).setPaymentId(paymentId).setDel("N");
		return paymentService.getOne(new QueryWrapper<>(payment)) == null
				? fail(ApiStatus.NOT_FOUND)
				: paymentService.removeById(paymentId)
				? success() : fail(ApiStatus.ERROR);

	}

	public Result wallet(String token, ApiWalletVo vo) {
		Member member = (Member) redisUtil.get(token);
		Wallet walletQ = new Wallet();
		walletQ.setDel("N");
		walletQ.setMemberId(member.getMemberId());
		//估值总额
		BigDecimal valuationTotalPrice = BigDecimal.ZERO;
		//账户总额
		BigDecimal accountTotalPrice = BigDecimal.ZERO;
		//钱包折合
		BigDecimal walletConvert = BigDecimal.ZERO;
		//币币折合
		BigDecimal currencyConvert = BigDecimal.ZERO;
		//期权折合
		BigDecimal optionConvert = BigDecimal.ZERO;
		//合约折合
		BigDecimal contractConvert = BigDecimal.ZERO;
		//黄金折合
		BigDecimal futuresConvert = BigDecimal.ZERO;
		//法币折合
		BigDecimal legalConvert = BigDecimal.ZERO;
		//矿机折合
		BigDecimal miningConvert = BigDecimal.ZERO;

		List<ApiWalletDto> list = new ArrayList<>();
		List<Spot> spots = F.me().getSpots(null);
		List<Swap> swaps = F.me().getSwaps(null);

		//钱包账户
		List<Wallet> walletList = walletService.list(new QueryWrapper<>(walletQ));
		for (Wallet entity : walletList) {
			Kline map = redisUtil.getBean(KINE + entity.getType() + "/USDT" + _NEW, Kline.class);
			if (map == null) {
				map = new Kline();
				map.setClose(1); //默认比例1:1
			}

			BigDecimal close = new BigDecimal(map.getClose());
			BigDecimal total = entity.getUsedPrice()
					.add(entity.getMortgagePrice() == null ? BigDecimal.ZERO : entity.getMortgagePrice())
					.add(entity.getFinancesPrice() == null ? BigDecimal.ZERO : entity.getFinancesPrice())
					.add(entity.getLockedPrice() == null ? BigDecimal.ZERO : entity.getLockedPrice());
			walletConvert = walletConvert.add(close.multiply(total));
			valuationTotalPrice = valuationTotalPrice.add(close.multiply(total));
			if (vo.getType().equals("WALLET")) {


				ApiWalletDto dto = new ApiWalletDto();
				for (Spot spot : spots) {
					if (spot.getSymbol().equals(entity.getType() + "/USDT")) {
						dto.setImg(spot.getImg());
					}
				}
				dto.setType(entity.getType());
				dto.setUsedPrice(entity.getUsedPrice().toPlainString());
				dto.setId(entity.getWalletId()).setTotalPrice(total.toPlainString());
				if (StrUtil.equals(vo.getHide(), "Y") && new BigDecimal(dto.getTotalPrice()).compareTo(BigDecimal.ZERO) == 0)
					continue;
				list.add(dto);
			}

		}
		Currency currencyQ = new Currency();
		currencyQ.setDel("N");
		currencyQ.setMemberId(member.getMemberId());
		//币币账户
		List<Currency> currencyList = currencyService.list(new QueryWrapper<>(currencyQ));
		for (Currency entity : currencyList) {
			Kline map = redisUtil.getBean(KINE + entity.getType() + "/USDT" + _NEW, Kline.class);
			if (map == null) {
				map = new Kline();
				map.setClose(1);
			}
			BigDecimal close = new BigDecimal(map.getClose());
			BigDecimal total = (entity.getUsedPrice() == null ? BigDecimal.ZERO : entity.getUsedPrice()).add(entity.getLockedPrice() == null ? BigDecimal.ZERO : entity.getLockedPrice());
			currencyConvert = currencyConvert.add(close.multiply(total));
			valuationTotalPrice = valuationTotalPrice.add(close.multiply(total));

			if (vo.getType().equals("CURRENCY")) {
				ApiWalletDto dto = new ApiWalletDto();
				for (Spot spot : spots) {
					if (spot.getSymbol().equals(entity.getType() + "/USDT")) {
						dto.setImg(spot.getImg());
					}
				}
				dto.setType(entity.getType());
				dto.setUsedPrice((entity.getUsedPrice() == null ? BigDecimal.ZERO : entity.getUsedPrice()).toPlainString());
				dto.setId(entity.getCurrencyId()).setTotalPrice(total.toPlainString());
				if (StrUtil.equals(vo.getHide(), "Y") && new BigDecimal(dto.getTotalPrice()).compareTo(BigDecimal.ZERO) == 0)
					continue;
				list.add(dto);
			}
		}

		FinMining miningQ = new FinMining();
		miningQ.setDel("N");
		miningQ.setMemberId(member.getMemberId());
		//矿机账户
		List<FinMining> miningList = finMiningService.list(new QueryWrapper<>(miningQ));
		for (FinMining entity : miningList) {
			Kline map = redisUtil.getBean(KINE + entity.getType() + "/USDT" + _NEW, Kline.class);
			if (map == null) {
				map = new Kline();
				map.setClose(1);
			}
			BigDecimal close = new BigDecimal(map.getClose());
			BigDecimal total = (entity.getUsedPrice() == null ? BigDecimal.ZERO : entity.getUsedPrice()).add(entity.getLockedPrice() == null ? BigDecimal.ZERO : entity.getLockedPrice());
			miningConvert = miningConvert.add(close.multiply(total));
			valuationTotalPrice = valuationTotalPrice.add(close.multiply(total));

			if (vo.getType().equals("MINING")) {
				ApiWalletDto dto = new ApiWalletDto();
				for (Spot spot : spots) {
					if (spot.getSymbol().equals(entity.getType() + "/USDT")) {
						dto.setImg(spot.getImg());
					}
				}
				dto.setType(entity.getType());
				dto.setUsedPrice((entity.getUsedPrice() == null ? BigDecimal.ZERO : entity.getUsedPrice()).toPlainString());
				dto.setId(entity.getMiningId()).setTotalPrice(total.toPlainString());
				if (StrUtil.equals(vo.getHide(), "Y") && new BigDecimal(dto.getTotalPrice()).compareTo(BigDecimal.ZERO) == 0)
					continue;
				list.add(dto);
			}
		}


		FinOption finOptionQ = new FinOption();
		finOptionQ.setMemberId(member.getMemberId());
		finOptionQ.setDel("N");
		//期权账户
		List<FinOption> optionList = finOptionService.list(new QueryWrapper<>(finOptionQ));
		for (FinOption entity : optionList) {
			Kline map = redisUtil.getBean(KINE + entity.getType() + "/USDT" + _NEW, Kline.class);
			if (map == null) {
				map = new Kline();
				map.setClose(1);
			}
			BigDecimal close = new BigDecimal(map.getClose());
			BigDecimal total = entity.getUsedPrice().add(entity.getLockedPrice());
			currencyConvert = currencyConvert.add(close.multiply(total));
			valuationTotalPrice = valuationTotalPrice.add(close.multiply(total));

			if (vo.getType().equals("OPTION")) {
				ApiWalletDto dto = new ApiWalletDto();
				for (Spot spot : spots) {
					if (spot.getSymbol().equals(entity.getType() + "/USDT")) {
						dto.setImg(spot.getImg());
					}
				}
				dto.setType(entity.getType());
				dto.setUsedPrice(entity.getUsedPrice().toPlainString());
				dto.setId(entity.getCurrencyId()).setTotalPrice(total.toPlainString());
				if (StrUtil.equals(vo.getHide(), "Y") && new BigDecimal(dto.getTotalPrice()).compareTo(BigDecimal.ZERO) == 0)
					continue;
				list.add(dto);
			}
		}


		Contract contractQ = new Contract();
		contractQ.setDel("N");
		contractQ.setMemberId(member.getMemberId());
		//合约账户
		List<Contract> contractList = contractService.list(new QueryWrapper<>(contractQ));
		for (Contract o : contractList) {
			Contract contractObj = (Contract) redisUtil.get(String.format(CONTRACT_CODE, o.getType()) + member.getMemberId());
			if (contractObj == null)
				continue;
			BigDecimal total = contractObj.getWorthPrice();
			contractConvert = total;
			valuationTotalPrice = valuationTotalPrice.add(contractConvert);
			if (vo.getType().equals("CONTRACT")) {
				ApiWalletDto dto = new ApiWalletDto();
				for (Swap spot : swaps) {
					if (spot.getSymbol().equals(o.getType() + "/USDT")) {
						dto.setImg(spot.getImg());
					}
				}
				dto.setType(o.getType());
				dto.setUsedPrice(o.getUsedPrice().toPlainString());
				//可用余额看可用保证金
				dto.setId(o.getContractId())
						.setTotalPrice(total.toPlainString())
						.setUsedPrice(contractObj.getUsedPrice().toPlainString());
				if (StrUtil.equals(vo.getHide(), "Y") && new BigDecimal(dto.getTotalPrice()).compareTo(BigDecimal.ZERO) == 0)
					continue;
				list.add(dto);
			}
		}

		FinFutures futuresQ = new FinFutures();
		futuresQ.setDel("N");
		futuresQ.setMemberId(member.getMemberId());
		//黄金账户
		List<FinFutures> futuresList = finFuturesService.list(new QueryWrapper<>(futuresQ));
		if (futuresList == null || futuresList.size() == 0) {
			genFinFutures(member);
		}
		for (FinFutures o : futuresList) {
			FinFutures contractObj = F.me().getFinFutures(member.getMemberId(), o.getType()) /*redisUtil.get(String.format(FINFUTURES_CODE, o.getType()) + member.getMemberId())*/;
			if (contractObj == null) {
				genFinFutures(member);
				contractObj = F.me().getFinFutures(member.getMemberId(), o.getType());

			}
			BigDecimal total = contractObj.getUsedPrice()
					.add(contractObj.getWorthPrice() == null ? BigDecimal.ZERO : contractObj.getWorthPrice());
			futuresConvert = total;
			valuationTotalPrice = valuationTotalPrice.add(futuresConvert);
			if (vo.getType().equals("FUTURES")) {
				ApiWalletDto dto = new ApiWalletDto();
				for (Swap spot : swaps) {
					if (spot.getSymbol().equals(o.getType() + "/USDT")) {
						dto.setImg(spot.getImg());
					}
				}
				dto.setType(o.getType());
				dto.setUsedPrice(o.getUsedPrice().toPlainString());
				//可用余额看可用保证金
				dto.setId(o.getContractId())
						.setTotalPrice(total.toPlainString())
						.setUsedPrice(contractObj.getUsedPrice().toPlainString());
				if (StrUtil.equals(vo.getHide(), "Y") && new BigDecimal(dto.getTotalPrice()).compareTo(BigDecimal.ZERO) == 0)
					continue;
				list.add(dto);
			}
		}


		//法币资产
		Legal legalQ = new Legal();
		legalQ.setMemberId(member.getMemberId());
		// .setType("USDT")
//        legalQ.setMemberId(member.getMemberId())
//                .setType("USDT")
		;
		legalQ.setDel("N");
		String otc_coin = F.me().getSysConfigValueByCode("OTC_COIN");
		List<Legal> legalList = legalService.list(new QueryWrapper<>(legalQ).in("type", otc_coin.split(",")));
		//List<Legal> legalList = legalService.list(new QueryWrapper<>(legalQ));
		for (Legal entity : legalList) {
			BigDecimal total = entity.getUsedPrice().add(entity.getLockedPrice());
			legalConvert = total;
			valuationTotalPrice = valuationTotalPrice.add(legalConvert);
			if (vo.getType().equals("LEGAL")) {
				ApiWalletDto dto = new ApiWalletDto();
				for (Spot spot : spots) {
					if (spot.getSymbol().equals(entity.getType() + "/USDT")) {
						dto.setImg(spot.getImg());
					}
				}
				dto.setType(entity.getType());
				dto.setUsedPrice(entity.getUsedPrice().toPlainString());
				//可用余额看可用保证金
				dto.setId(entity.getLegalId()).setTotalPrice(total.toPlainString());
				if (StrUtil.equals(vo.getHide(), "Y") && new BigDecimal(dto.getTotalPrice()).compareTo(BigDecimal.ZERO) == 0)
					continue;
				list.add(dto);
			}
		}
		String cnyUsdt = F.me().getSysConfigValueByCode("CNY_USDT");
		//折合CNY
		BigDecimal cny = valuationTotalPrice.divide(new BigDecimal(cnyUsdt), 8, RoundingMode.DOWN);
		if (vo.getValuation().equals("BTC")) {
			Kline map = redisUtil.getBean(KINE + BTC_USDT + _NEW, Kline.class);
			BigDecimal close = new BigDecimal(map.getClose());
			valuationTotalPrice = valuationTotalPrice.divide(close, 8, BigDecimal.ROUND_DOWN);
			//(WALLET|CONTRACT|CURRENCY|LEGAL)
			switch (vo.getType()) {
				case "WALLET":
					accountTotalPrice = walletConvert.divide(close, 8, BigDecimal.ROUND_DOWN);
					break;
				case "CONTRACT":
					accountTotalPrice = contractConvert.divide(close, 8, BigDecimal.ROUND_DOWN);
					break;
				case "CURRENCY":
					accountTotalPrice = currencyConvert.divide(close, 8, BigDecimal.ROUND_DOWN);
					break;
				case "MINING":
					accountTotalPrice = miningConvert.divide(close, 8, BigDecimal.ROUND_DOWN);
					break;
				case "LEGAL":
					accountTotalPrice = legalConvert.divide(close, 8, BigDecimal.ROUND_DOWN);
					break;
				case "OPTION":
					accountTotalPrice = optionConvert.divide(close, 8, BigDecimal.ROUND_DOWN);
					break;
				case "FUTURES":
					accountTotalPrice = futuresConvert.divide(close, 8, BigDecimal.ROUND_DOWN);
					break;
			}
		} else {
			switch (vo.getType()) {
				case "WALLET":
					accountTotalPrice = walletConvert;
					break;
				case "CONTRACT":
					accountTotalPrice = contractConvert;
					break;
				case "CURRENCY":
					accountTotalPrice = currencyConvert;
					break;

				case "MINING":
					accountTotalPrice = miningConvert;
					break;
				case "LEGAL":
					accountTotalPrice = legalConvert;
					break;
				case "OPTION":
					accountTotalPrice = optionConvert;
					break;
				case "FUTURES":
					accountTotalPrice = futuresConvert;
					break;
			}
		}


		Map<String, Object> map = new HashedMap();
		map.put("valuationTotalPrice", valuationTotalPrice.toPlainString());
		map.put("cny", cny);
		map.put("accountTotalPrice", accountTotalPrice.toPlainString());
		map.put("list", list);
		return success("资产信息", map);
	}

	public Result walletDetail(String token, Long id, String type) {
		ApiWalletItemDto apiWalletItemDto = new ApiWalletItemDto();
		Member member = (Member) redisUtil.get(token);
		switch (type) {
			case "WALLET":
				apiWalletItemDto = walletItem(member, id);
				break;
			case "CONTRACT":
				apiWalletItemDto = contractItem(member, id);
				break;
			case "CURRENCY":
				apiWalletItemDto = currencyItem(member, id);
				break;
			case "LEGAL":
				apiWalletItemDto = legalItem(member, id);
				break;
		}
		return success("账户明细", apiWalletItemDto);

	}

	private ApiWalletItemDto legalItem(Member member, Long id) {
		Legal query = new Legal();
		query.setMemberId(member.getMemberId()).setLegalId(id);
		Legal entity = legalService.getOne(new QueryWrapper<>(query));

		ApiWalletItemDto dto = new ApiWalletItemDto();

		dto.setP1(entity.getUsedPrice().add(entity.getLockedPrice()).toPlainString())
				.setP2(entity.getLockedPrice().toPlainString())
				.setP3(entity.getUsedPrice().toPlainString())
				.setPage("LEGAL")
				.setType(entity.getType())
		;
		return dto;
	}

	private ApiWalletItemDto currencyItem(Member member, Long id) {
		Currency query = new Currency();
		query.setMemberId(member.getMemberId()).setCurrencyId(id);
		Currency entity = currencyService.getOne(new QueryWrapper<>(query));

		ApiWalletItemDto dto = new ApiWalletItemDto();

		dto.setP1(entity.getUsedPrice().add(entity.getLockedPrice()).toPlainString())
				.setP2(entity.getLockedPrice().toPlainString())
				.setP3(entity.getUsedPrice().toPlainString())
				.setPage("CURRENCY")
				.setType(entity.getType())
		;
		return dto;
	}

	private ApiWalletItemDto contractItem(Member member, Long id) {
		Contract query = new Contract();
		query.setMemberId(member.getMemberId()).setContractId(id);
		Contract entity = contractService.getOne(new QueryWrapper<>(query));
		return getMargin(member.getMemberId(), entity.getType());
	}

	private ApiWalletItemDto getMargin(Long memberId, String coin) {
//        Map<String, Object> map = new HashedMap();
//        //获取用户所有合约记录
//        Compact compactQ = new Compact();
//        compactQ.setDel("N")
//                .setMemberId(memberId)
//                .setCoin(coin)
//                .setEnabled("N")
//                .setStatus(CompactStatus.N.code);
//        List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
//        //总盈亏
//        BigDecimal tpl = BigDecimal.ZERO;
//        //平仓手续费比例
//        BigDecimal outFeeRate = new BigDecimal(F.me().cfg(OUT_FEE)).divide(new BigDecimal(100));
//
//        //配资手续费比例
//        BigDecimal giveFeeRate = new BigDecimal(F.me().cfg(GIVE_FEE)).divide(new BigDecimal(100));
//        //总手续费
//        BigDecimal totalFee = BigDecimal.ZERO;
//        //总持仓
//        BigDecimal totalLock = BigDecimal.ZERO;
//        //总持仓保证金
//        BigDecimal totalPosition = BigDecimal.ZERO;
//        //总持仓配资
//        BigDecimal totalGive = BigDecimal.ZERO;
//        for (Compact compact : compactList){
//            //杠杆*本金
//            BigDecimal levMulNum = compact.getLeverRate().multiply(new BigDecimal(compact.getNumbers()));
//            //总手续费
//            //平仓手续费=本金*杠杆*平仓手续费比例
//            BigDecimal outFee = levMulNum.multiply(outFeeRate);
//            //天数
//            int days = DateTimeUtil.getDays(compact.getCreateTime(), new Date());
//            //配资手续费=持仓配资*配资手续费比例*天数
//            BigDecimal giveFee = compact.getGivePrice().multiply(giveFeeRate).multiply(new BigDecimal(days));
//            //开仓手续费
//            BigDecimal fee = compact.getFee();
//            totalFee = totalFee.add(outFee).add(giveFee).add(fee);
//
//            BigDecimal pl = (BigDecimal) redisUtil.get(PL + compact.getOrderNo());
//            if (pl != null)
//                tpl = tpl.add(pl);
//            totalLock = totalLock.add(compact.getPositionPrice()).add(compact.getGivePrice());
//
//            totalPosition = totalPosition.add(compact.getPositionPrice());
//            totalGive = totalGive.add(compact.getGivePrice());
//
//        }


		//保证金率=(资产+盈亏)/(持仓保证金+持仓配资)
		Contract entrust = F.me().getContract(memberId, coin);

		Contract contractObj = (Contract) redisUtil.get(String.format(CONTRACT_CODE, coin) + memberId);

		ApiWalletItemDto dto = new ApiWalletItemDto();
		dto.setP1(contractObj.getWorthPrice().toPlainString())
				.setP2(contractObj.getNoPl().toPlainString())
				.setP3(contractObj.getPositionPrice().toPlainString())
				.setP4(contractObj.getUsedPrice().toPlainString())
				.setP5(contractObj.getEntrustPrice().toPlainString())
//                .setP6(contractObj.getGivePrice().toPlainString())
				.setPage("CONTRACT")
				.setType(coin)
		;
		log.info("合约账户信息" + JSONObject.toJSONString(dto));
		//持仓保证金(用户部分+持仓配资）
//        BigDecimal lockSum = contractObj.getPositionPrice();
//
//        if (totalLock.compareTo(BigDecimal.ZERO) > 0)
//            dto.setP7((contractObj.getWorthPrice().subtract(totalFee).subtract(entrust.getEntrustPrice())).multiply(new BigDecimal(100)).divide(lockSum, 2, RoundingMode.DOWN) + "%");
//        else
//            dto.setP7("--");
		return dto;
	}

	private ApiWalletItemDto walletItem(Member member, Long id) {
		Wallet query = new Wallet();
		query.setMemberId(member.getMemberId()).setWalletId(id);
		Wallet entity = walletService.getOne(new QueryWrapper<>(query));

		ApiWalletItemDto dto = new ApiWalletItemDto();
		dto.setP1(entity.getUsedPrice()
						.add(entity.getLockedPrice())
						.add(entity.getMortgagePrice())
						.add(entity.getFinancesPrice()).toPlainString())
				.setP2(entity.getLockedPrice().toPlainString())
				.setP3(entity.getUsedPrice().toPlainString())
				.setP4(entity.getMortgagePrice().toPlainString())
				.setP5(entity.getFinancesPrice().toPlainString())
				.setPage("WALLET")
				.setType(entity.getType())
		;
		return dto;
	}

	/**
	 * 获取充币地址
	 *
	 * @param token
	 * @param symbol
	 * @return
	 */
	public Result platformAddress(String token, String symbol) {
		Member member = (Member) redisUtil.get(token);
		if (StringUtils.isBlank(symbol)) {
			return Result.fail(ApiStatus.BAD_REQUEST);
		}


		// 判断是否开启钱包开关
		boolean open = false;
		if (StrUtil.equals(F.me().cfg(CHAIN_OPEN), "Y")) {
			open = true;
		}
		LambdaQueryWrapper<MemberRechargeAddress> addresQueryWrapper = new LambdaQueryWrapper<MemberRechargeAddress>()
				.eq(MemberRechargeAddress::getMemberId, member.getMemberId())
				.eq(MemberRechargeAddress::getCoin, symbol);
		MemberRechargeAddress rechargeAddress = memberRechargeAddressService.getOne(addresQueryWrapper);


		Map<String, Object> map = new HashedMap();
		if (open && rechargeAddress != null) {
			//如果开启钱包功能,和充值地址不为空
			map.put("address", rechargeAddress.getAddress());
			map.put("memoTagValue", rechargeAddress.getRemark());
			map.put("symbol", symbol);
			map.put("type", "user");
		} else {
			// 没开启和用户充值地址为空，查询平台的
			LambdaQueryWrapper<PlatformAddress> queryWrapper = new LambdaQueryWrapper<PlatformAddress>()
					.eq(PlatformAddress::getType, symbol)
					.eq(PlatformAddress::getDel, "N")
					.eq(PlatformAddress::getStatus, "Y");
			PlatformAddress platformAddress = this.platformAddressService.getOne(queryWrapper);
			if (platformAddress != null) {
				map.put("address", platformAddress.getAddress());
				map.put("memoTagValue", platformAddress.getRemark());
				map.put("symbol", symbol);
				map.put("type", "platform");
			}
		}
		return success("用户充币地址", map);
	}

	/**
	 * 充币
	 *
	 * @param token
	 * @param dto
	 * @return
	 */
	public Result recharge(String token, ApiRechargeDto dto) {

		Member member = (Member) redisUtil.get(token);
		if (!("1").equals(member.getRealStatus())) {
			return fail(ApiStatus.NOT_REAL);
		}
		Recharge recharge = new Recharge();
//        if (StrUtil.isBlank(member.getPayPassword()))
//            return fail(ApiStatus.PAY_PWD_EMPTY);
//        if (!isTruePayPwd(member, dto.getPayPwd()))
//            return fail(ApiStatus.ERROR_PAY_PWD);

		BeanUtil.copyProperties(dto, recharge);
		recharge.setMemberId(member.getMemberId())
				.setOrderNo(cn.stylefeng.guns.modular.base.util.RandomUtil.code("CR"))
				.setRechargeAddress(dto.getAddress())
				.setTxHash(dto.getAddress())
				.setStatus(Status.CHECK.code)
				.setCreateUser(member.getMemberId());
		rechargeService.save(recharge);
//        if(!"Y".equals(member.getValidStatus())){
//            member.setValidStatus("Y");
//            memberService.updateById(member);
//            redisUtil.set(token, member, Long.parseLong(F.me().cfg(TOKEN_EXPIRE)));
//        }
		try {
			WebSocketService.sendInfo(RECHARGESTRING, SYSTEMSOCKETSENDNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success(ApiStatus.OK);
	}

	public Result transferPage(String token) {
		Member member = (Member) redisUtil.get(token);
		String coin = "USDT";
		Wallet wallet = F.me().getWallet(member, coin);

		Map<String, Object> map = new HashedMap();
		map.put("account", member.getAccount());
		map.put("price", wallet.getUsedPrice());
		map.put("fee", F.me().cfg(coin + _TRANSFER_FEE));
		map.put("usdt", BigDecimal.ONE.divide(new BigDecimal(F.me().cfg(CNY_USDT)), 2, RoundingMode.DOWN));
		return success("转账页面信息", map);
	}

	public Result transfer(String token, ApiTransferDto apiTransfer) {
		String coin = "USDT";
		Member member = (Member) redisUtil.get(token);
		if (!"1".equals(member.getRealStatus()))
			return fail(ApiStatus.NOT_REAL);
		if (StrUtil.isBlank(member.getPayPassword()))
			return fail(ApiStatus.PAY_PWD_EMPTY);
		if (!isTruePayPwd(member, apiTransfer.getPayPwd()))
			return fail(ApiStatus.ERROR_PAY_PWD);
		Wallet wallet = F.me().getWallet(member, coin);
		BigDecimal fee = new BigDecimal(F.me().cfg(coin + _TRANSFER_FEE));
		if (apiTransfer.getType().equals("CNY")) {
			BigDecimal cnyUsdt = new BigDecimal(F.me().cfg(CNY_USDT));
			apiTransfer.setPrice(apiTransfer.getPrice().multiply(cnyUsdt));
		}

		if (wallet.getUsedPrice().compareTo(apiTransfer.getPrice()) < 0)
			return fail(ApiStatus.WALLET_LESS);

		if (apiTransfer.getPrice().subtract(fee).compareTo(BigDecimal.ZERO) <= 0) {
			return fail(ApiStatus.NOT_TRAND);
		}
		Member acceptMember = memberService.getBaseMapper().getUserForOtherType(apiTransfer.getAccount());
		if (acceptMember == null)
			return fail(ApiStatus.NOT_FIND_USER);
		if (acceptMember.getMemberId().longValue() == member.getMemberId().longValue())
			return fail(ApiStatus.NOT_OP_MINE);
		Transfer transfer = new Transfer();
		transfer.setMemberId(member.getMemberId())
				.setToAddress(acceptMember.getMemberId() + "")
				.setPrice(apiTransfer.getPrice())
				.setType("USDT")
				.setActualPrice(apiTransfer.getPrice().subtract(fee))
				.setFee(fee)
				.setOrderNo(cn.stylefeng.guns.modular.base.util.RandomUtil.code("T"))
				.setCreateUser(member.getMemberId());

		F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.TRANSFER_IN,
				transfer.getPrice(), transfer.getType(), transfer.getActualPrice(), transfer.getType(), fee, transfer.getType(),
				ItemCode.USED, transfer.getType(), null, null,
				wallet.getUsedPrice(), wallet.getUsedPrice().subtract(transfer.getPrice()),
				member.getMemberId(), acceptMember.getMemberId());

		wallet.setUsedPrice(wallet.getUsedPrice().subtract(transfer.getPrice())).setUpdateUser(member.getMemberId());
		walletService.updateById(wallet);

		Wallet acceptWallet = F.me().getWallet(acceptMember, coin);

		F.me().saveCashflow(acceptMember.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.TRANSFER_IN,
				transfer.getActualPrice(), transfer.getType(), transfer.getActualPrice(), transfer.getType(), BigDecimal.ZERO, transfer.getType(),
				ItemCode.USED, transfer.getType(), null, null,
				acceptWallet.getUsedPrice(), acceptWallet.getUsedPrice().add(transfer.getActualPrice()),
				member.getMemberId(), acceptMember.getMemberId());
		acceptWallet.setUsedPrice(acceptWallet.getUsedPrice().add(transfer.getActualPrice())).setUpdateUser(acceptMember.getMemberId());
		walletService.updateById(acceptWallet);
		return success();
	}

	public Result transferRecordFlow(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		Cashflow cashflowQ = new Cashflow();
		cashflowQ.setFlowType(CashFlowTypeEnum.TRANSFER_IN.getCode())
				.setMemberId(member.getMemberId());
		IPage<Cashflow> iPage = cashflowService.page(page, new QueryWrapper<>(cashflowQ).orderByDesc(CREATE_TIME));
		List<TransferRecordDto> recordDtoList = new ArrayList<>();
		List<Cashflow> dtoList = iPage.getRecords();
		for (Cashflow cashflow : dtoList) {
			TransferRecordDto dto = new TransferRecordDto();
			Member fromMember = F.me().getMember(cashflow.getFromId());
			Member toMember = F.me().getMember(cashflow.getToId());
			if (cashflow.getFlowOp().longValue() == CashFlowOpEnum.FLOW_IN.code.longValue())
				dto.setType("内部转入 USDT");
			else
				dto.setType("内部转出 USDT");
			dto.setPrice(cashflow.getFlowPrice())
					.setFrom(fromMember.getAccount())
					.setTo(toMember.getAccount())
					.setTime(cashflow.getCreateTime());
			recordDtoList.add(dto);
		}
		IPage<TransferRecordDto> result = new Page<>();
		result.setRecords(recordDtoList);
		return success(result);
	}

	public Result convertPage(String token, ApiConvertPageDto dto) {
		Member member = (Member) redisUtil.get(token);
		//法币禁用币种
		//String disableCoinLegal0 = "USDT";
		//参数校验
		//if (StrUtil.equals(dto.getFrom(), "LEGAL"))
		//    if (!StrUtil.equals(dto.getType(), disableCoinLegal0))
		//        return fail(ApiStatus.BAD_REQUEST);
		//合约指定币种
		String disableCoin0 = "USDT";
		if (StrUtil.equals(dto.getFrom(), "CONTRACT"))
			if (!StrUtil.equals(dto.getType(), disableCoin0))
				return fail(ApiStatus.BAD_REQUEST);
		Map<String, Object> map = new HashedMap();
		BigDecimal price = BigDecimal.ZERO;
		switch (dto.getFrom()) {
			case "WALLET":
				price = F.me().getWallet(member.getMemberId(), dto.getType()).getUsedPrice();
				break;
			case "CONTRACT":
				Contract contract = (Contract) redisUtil.get(String.format(CONTRACT_CODE, dto.getType()) + member.getMemberId());
				price = contract.getUsedPrice();
				break;
			case "CURRENCY":
				price = F.me().getCurrency(member.getMemberId(), dto.getType()).getUsedPrice();
				break;
			case "MINING":
				price = F.me().getMining(member.getMemberId(), dto.getType()).getUsedPrice();
				break;
			case "LEGAL":
				price = F.me().getLegal(member.getMemberId(), dto.getType()).getUsedPrice();
				break;
			case "OPTION":
				FinOption finOption = F.me().getFinOption(member.getMemberId(), dto.getType());
				if (finOption != null) {
					price = finOption.getUsedPrice();
				} else {
					genOption(member);
				}
				break;
			case "FUTURES":
				FinFutures finFutures = F.me().getFinFutures(member.getMemberId(), dto.getType());
				if (finFutures != null) {
					price = finFutures.getUsedPrice();
				} else {
					genFinFutures(member);
				}
				break;
		}
		map.put("price", price.toString());
		map.put("unit", dto.getType());

		return success("资产划转页面信息", map);
	}

	//资产划转
	@Transactional
	public Result convert(String token, ApiConvertDto dto) {
		if (BigDecimal.ZERO.compareTo(dto.getNumbers()) > 0)
			return fail(ApiStatus.BAD_REQUEST);
		Member member = (Member) redisUtil.get(token);
		if (!("1").equals(member.getRealStatus())) {
			return fail(ApiStatus.NOT_REAL);
		}
//        if (StrUtil.equals(dto.getFrom(), "LEGAL") || StrUtil.equals(dto.getTo(), "LEGAL"))
//            if (!StrUtil.equals(dto.getType(), "USDT"))
//                return fail(ApiStatus.BAD_REQUEST);
		//合约只能划USDT和MGE
		if (StrUtil.equals(dto.getFrom(), "CONTRACT") || StrUtil.equals(dto.getTo(), WalletBigType.CONTRACT.code))
			if (!StrUtil.equals(dto.getType(), "USDT"))
				return fail(ApiStatus.BAD_REQUEST);
//        if (StrUtil.equals(dto.getFrom(), "LEGAL") || StrUtil.equals(dto.getTo(), WalletBigType.LEGAL.code))
//            if (!StrUtil.equals(dto.getType(), "USDT"))
//                return fail(ApiStatus.BAD_REQUEST);
		if (StrUtil.equals(dto.getFrom(), dto.getTo()))
			return fail(ApiStatus.BAD_REQUEST);
		//期权只能划转法币 已经废弃
		if (StrUtil.equals(dto.getFrom(), "OPTION") || StrUtil.equals(dto.getTo(), WalletBigType.OPTION.code)) {
			if (!(StrUtil.equals(dto.getType(), "USDT")))
				return fail(ApiStatus.BAD_REQUEST_OPTION);
		}
		//黄金只接受 USDT
		if (StrUtil.equals(dto.getFrom(), "FUTURES") || StrUtil.equals(dto.getTo(), WalletBigType.FUTURES.code))
			if (!StrUtil.equals(dto.getType(), "USDT"))
				return fail(ApiStatus.BAD_REQUEST);
		//矿机规则
		if (StrUtil.equals(dto.getFrom(), "MINING") || StrUtil.equals(dto.getTo(), WalletBigType.MINING.code)) {

		}


		Result result = null;
		//数量校验
		switch (dto.getFrom()) {
			case "WALLET":
				result = convertWallet(member, dto);
				break;
			case "CONTRACT":
				result = convertContract(member, dto);
				break;
			case "CURRENCY":
				result = convertCurrency(member, dto);
				break;
			case "MINING":
				result = miningCurrency(member, dto);
				break;
			case "LEGAL":
				result = convertLegal(member, dto);
				break;
			case "OPTION":
				result = convertOption(member, dto);
				break;
			case "FUTURES":
				result = convertFinFutures(member, dto);
				break;
		}
		return result;

	}

	private Result convertOption(Member member, ApiConvertDto dto) {

		FinOption from = F.me().getFinOption(member.getMemberId(), dto.getType());
		if (from.getUsedPrice().compareTo(dto.getNumbers()) < 0)
			return fail(ApiStatus.WALLET_LESS);
		//划出提示
		String convertOutTip = genRemark(dto, 1);
		F.me().saveCashflow(member.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONVERT_OUT,
				dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
				ItemCode.USED, dto.getType(), null, convertOutTip,
				from.getUsedPrice(), from.getUsedPrice().subtract(dto.getNumbers()),
				member.getMemberId(), member.getMemberId());

		from.setUsedPrice(from.getUsedPrice().subtract(dto.getNumbers()))
				.setUpdateUser(member.getMemberId());
		finOptionService.updateById(from);
		// 划入账户操作
		convertIn(member, dto);
		return success("划出成功", null);
	}

	private Result convertLegal(Member member, ApiConvertDto dto) {
		Legal from = F.me().getLegal(member.getMemberId(), dto.getType());
		if (from.getUsedPrice().compareTo(dto.getNumbers()) < 0)
			return fail(ApiStatus.WALLET_LESS);
		//划出提示
		String convertOutTip = genRemark(dto, 1);
		F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONVERT_OUT,
				dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
				ItemCode.USED, dto.getType(), null, convertOutTip,
				from.getUsedPrice(), from.getUsedPrice().subtract(dto.getNumbers()),
				member.getMemberId(), member.getMemberId());
		from.setUsedPrice(from.getUsedPrice().subtract(dto.getNumbers()))
				.setUpdateUser(member.getMemberId());
		legalService.updateById(from);
		// 划入账户操作
		convertIn(member, dto);
		return success("划出成功", null);
	}

	private Result convertCurrency(Member member, ApiConvertDto dto) {
		Currency from = F.me().getCurrency(member.getMemberId(), dto.getType());
		if (from.getUsedPrice().compareTo(dto.getNumbers()) < 0)
			return fail(ApiStatus.WALLET_LESS);
		//划出提示
		String convertOutTip = genRemark(dto, 1);

		F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONVERT_OUT,
				dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
				ItemCode.USED, dto.getType(), null, convertOutTip,
				from.getUsedPrice(), from.getUsedPrice().subtract(dto.getNumbers()),
				member.getMemberId(), member.getMemberId());
		from.setUsedPrice(from.getUsedPrice().subtract(dto.getNumbers()))
				.setUpdateUser(member.getMemberId());
		currencyService.updateById(from);
		// 划入账户操作
		convertIn(member, dto);
		return success("划出成功", null);
	}

	private Result miningCurrency(Member member, ApiConvertDto dto) {
		FinMining from = F.me().getMining(member.getMemberId(), dto.getType());
		if (from.getUsedPrice().compareTo(dto.getNumbers()) < 0)
			return fail(ApiStatus.WALLET_LESS);
		//划出提示
		String convertOutTip = genRemark(dto, 1);

		F.me().saveCashflow(member.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONVERT_OUT,
				dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
				ItemCode.USED, dto.getType(), null, convertOutTip,
				from.getUsedPrice(), from.getUsedPrice().subtract(dto.getNumbers()),
				member.getMemberId(), member.getMemberId());
		from.setUsedPrice(from.getUsedPrice().subtract(dto.getNumbers()))
				.setUpdateUser(member.getMemberId());
		finMiningService.updateById(from);
		// 划入账户操作
		convertIn(member, dto);
		return success("划出成功", null);
	}

	private Result convertContract(Member member, ApiConvertDto dto) {

		synchronized (this) {
			if (compactService.getBaseMapper().getHold(member.getMemberId(), dto.getType()) > 0) {
				return fail(ApiStatus.POS_NOT_CVT);//"当前合约存在持仓，无法划转"
			}
			// Contract from = (Contract) redisUtil.get(String.format(CONTRACT_CODE, dto.getType()) + member.getMemberId());
			//当前委托
//            BigDecimal currentIn = compactService.getBaseMapper().getIn(member.getMemberId());
//            //划转需要减去配资
			Contract from = F.me().getContract(member.getMemberId(), dto.getType());
//            BigDecimal total = from.getUsedPrice().subtract(currentIn).subtract(entrustObj.getEntrustPrice());
			BigDecimal total = from.getUsedPrice();
			if (total.compareTo(dto.getNumbers()) < 0) {
				return fail(ApiStatus.WALLET_LESS);
			}
			//划出提示
			String convertOutTip = genRemark(dto, 1);
			F.me().saveCashflow(member.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONVERT_OUT,
					dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
					ItemCode.USED, dto.getType(), null, convertOutTip,
					from.getUsedPrice(), from.getUsedPrice().subtract(dto.getNumbers()),
					member.getMemberId(), member.getMemberId());
			from.setUsedPrice(from.getUsedPrice().subtract(dto.getNumbers()));
			from.setUpdateUser(1L);
			from.setUpdateTime(new Date());
			while (true) {
				if (contractService.updateWallet(from) > 0) {
					break;
				}
			}
			jobService.refreshContractInfo(member.getMemberId(), dto.getType());
			redisUtil.lSet(OP_CONTRACT_DATA, member.getMemberId());
			// 划入账户操作
			convertIn(member, dto);
		}

		return success("划出成功", null);
	}

	private Result convertFinFutures(Member member, ApiConvertDto dto) {

		synchronized (this) {
			if (compactService.getBaseMapper().getHold(member.getMemberId(), dto.getType()) > 0) {
				return fail(ApiStatus.POS_NOT_CVT);//"当前合约存在持仓，无法划转"
			}
			// Contract from = (Contract) redisUtil.get(String.format(CONTRACT_CODE, dto.getType()) + member.getMemberId());
			//当前委托
//            BigDecimal currentIn = compactService.getBaseMapper().getIn(member.getMemberId());
//            //划转需要减去配资
			FinFutures from = F.me().getFinFutures(member.getMemberId(), dto.getType());
//            BigDecimal total = from.getUsedPrice().subtract(currentIn).subtract(entrustObj.getEntrustPrice());
			BigDecimal total = from.getUsedPrice();
			if (total.compareTo(dto.getNumbers()) < 0) {
				return fail(ApiStatus.WALLET_LESS);
			}
			//划出提示
			String convertOutTip = genRemark(dto, 1);
			F.me().saveCashflow(member.getMemberId(), WalletBigType.FUTURES, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONVERT_OUT,
					dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
					ItemCode.USED, dto.getType(), null, convertOutTip,
					from.getUsedPrice(), from.getUsedPrice().subtract(dto.getNumbers()),
					member.getMemberId(), member.getMemberId());
			from.setUsedPrice(from.getUsedPrice().subtract(dto.getNumbers()));
			from.setUpdateUser(1L);
			from.setUpdateTime(new Date());
			while (true) {
				if (finFuturesService.updateWallet(from) > 0) {
					break;
				}
			}
			jobService.refreshFinFuturesInfo(member.getMemberId(), dto.getType());
			redisUtil.lSet(OP_FINFUTURES_DATA, member.getMemberId());
			// 划入账户操作
			convertIn(member, dto);
		}

		return success("划出成功", null);
	}

	private Result convertWallet(Member member, ApiConvertDto dto) {
		Wallet from = F.me().getWallet(member.getMemberId(), dto.getType());

		if (from.getUsedPrice().compareTo(dto.getNumbers()) < 0)
			return fail(ApiStatus.WALLET_LESS);

		//划出提示
		String convertOutTip = genRemark(dto, 1);

		Result result = convertIn(member, dto);
		if (result.getSuccess()) {
			F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONVERT_OUT,
					dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
					ItemCode.USED, dto.getType(), null, convertOutTip,
					from.getUsedPrice(), from.getUsedPrice().subtract(dto.getNumbers()),
					member.getMemberId(), member.getMemberId());
			from.setUsedPrice(from.getUsedPrice().subtract(dto.getNumbers()))
					.setUpdateUser(member.getMemberId());
			walletService.updateById(from);
		}
		// 划入账户操作
		return result;
	}

	/**
	 * 划入账户操作
	 */
	private Result convertIn(Member member, ApiConvertDto dto) {
		//划入提示
		String convertInTip = genRemark(dto, 0);
		Result result = success("划出成功", null);
		WalletBigType walletBigType;
		switch (dto.getTo()) {
			case "WALLET":
				walletBigType = WalletBigType.WALLET;
				Wallet wallet = F.me().getWallet(member.getMemberId(), dto.getType());
				F.me().saveCashflow(member.getMemberId(), walletBigType, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONVERT_IN,
						dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
						ItemCode.USED, dto.getType(), null, convertInTip,
						wallet.getUsedPrice(), wallet.getUsedPrice().add(dto.getNumbers()),
						member.getMemberId(), member.getMemberId());
				wallet.setUsedPrice(wallet.getUsedPrice().add(dto.getNumbers()))
						.setUpdateUser(member.getMemberId());
				walletService.updateById(wallet);

				break;
			case "CONTRACT":
				//查询判断是否下合约订单
//                if (compactService.getBaseMapper().getHold(member.getMemberId(), dto.getType()) > 0){
//                    return fail(ApiStatus.POS_NOT_CVT);//"当前合约存在持仓，无法划转"
//                }
				walletBigType = WalletBigType.CONTRACT;
				Contract contract = F.me().getContract(member.getMemberId(), dto.getType());
				F.me().saveCashflow(member.getMemberId(), walletBigType, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONVERT_IN,
						dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
						ItemCode.USED, dto.getType(), null, convertInTip,
						BigDecimal.ZERO, BigDecimal.ZERO,
						member.getMemberId(), member.getMemberId());
				contract.setUsedPrice(contract.getUsedPrice().add(dto.getNumbers()));
				contract.setUpdateTime(new Date());
				contract.setUpdateUser(1l);
				while (true) {
					if (contractService.updateWallet(contract) > 0) {
						break;
					}
				}
				jobService.refreshContractInfo(member.getMemberId(), dto.getType());
				redisUtil.lSet(OP_CONTRACT_DATA, member.getMemberId());
				break;
			case "FUTURES":
				//查询判断是否下合约订单
//                if (compactService.getBaseMapper().getHold(member.getMemberId(), dto.getType()) > 0){
//                    return fail(ApiStatus.POS_NOT_CVT);//"当前合约存在持仓，无法划转"
//                }
				walletBigType = WalletBigType.CONTRACT;
				FinFutures finFutures = F.me().getFinFutures(member.getMemberId(), dto.getType());
				F.me().saveCashflow(member.getMemberId(), walletBigType, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONVERT_IN,
						dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
						ItemCode.USED, dto.getType(), null, convertInTip,
						BigDecimal.ZERO, BigDecimal.ZERO,
						member.getMemberId(), member.getMemberId());
				finFutures.setUsedPrice(finFutures.getUsedPrice().add(dto.getNumbers()));
				finFutures.setUpdateTime(new Date());
				finFutures.setUpdateUser(1l);
				while (true) {
					if (finFuturesService.updateWallet(finFutures) > 0) {
						break;
					}
				}
				jobService.refreshFinFuturesInfo(member.getMemberId(), dto.getType());
				redisUtil.lSet(OP_FINFUTURES_DATA, member.getMemberId());
				break;
			case "CURRENCY":
				walletBigType = WalletBigType.CURRENCY;
				Currency currency = F.me().getCurrency(member.getMemberId(), dto.getType());
				F.me().saveCashflow(member.getMemberId(), walletBigType, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONVERT_IN,
						dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
						ItemCode.USED, dto.getType(), null, convertInTip,
						currency.getUsedPrice(), currency.getUsedPrice().add(dto.getNumbers()),
						member.getMemberId(), member.getMemberId());
				currency.setUsedPrice(currency.getUsedPrice().add(dto.getNumbers()))
						.setUpdateUser(member.getMemberId());
				currencyService.updateById(currency);
				break;

			case "MINING":
				walletBigType = WalletBigType.MINING;
				FinMining mining = F.me().getMining(member.getMemberId(), dto.getType());
				if (mining == null) {
					if (mining == null) {
						mining = new FinMining();
						mining.setMemberId(member.getMemberId())
								.setType(dto.getType()).setUsedPrice(BigDecimal.ZERO).setLockedPrice(BigDecimal.ZERO)
								.setCreateUser(member.getMemberId());
						mining.setCreateTime(new Date());
						mining.setUpdateUser(member.getMemberId());
						finMiningService.save(mining);
					}
				}
				F.me().saveCashflow(member.getMemberId(), walletBigType, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONVERT_IN,
						dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
						ItemCode.USED, dto.getType(), null, convertInTip,
						mining.getUsedPrice(), mining.getUsedPrice().add(dto.getNumbers()),
						member.getMemberId(), member.getMemberId());
				mining.setUsedPrice(mining.getUsedPrice().add(dto.getNumbers()))
						.setUpdateUser(member.getMemberId());
				finMiningService.updateById(mining);
				break;
			case "LEGAL":
				walletBigType = WalletBigType.LEGAL;
				Legal legal = F.me().getLegal(member.getMemberId(), dto.getType());
				F.me().saveCashflow(member.getMemberId(), walletBigType, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONVERT_IN,
						dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
						ItemCode.USED, dto.getType(), null, convertInTip,
						legal.getUsedPrice(), legal.getUsedPrice().add(dto.getNumbers()),
						member.getMemberId(), member.getMemberId());
				legal.setUsedPrice(legal.getUsedPrice().add(dto.getNumbers()))
						.setUpdateUser(member.getMemberId());
				legalService.updateById(legal);
				break;
			case "OPTION":
				walletBigType = WalletBigType.OPTION;
				FinOption finOption = F.me().getFinOption(member.getMemberId(), dto.getType());
				F.me().saveCashflow(member.getMemberId(), walletBigType, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONVERT_IN,
						dto.getNumbers(), dto.getType(), dto.getNumbers(), dto.getType(), BigDecimal.ZERO, dto.getType(),
						ItemCode.USED, dto.getType(), null, convertInTip,
						finOption.getUsedPrice(), finOption.getUsedPrice().add(dto.getNumbers()),
						member.getMemberId(), member.getMemberId());
				finOption.setUsedPrice(finOption.getUsedPrice().add(dto.getNumbers()))
						.setUpdateUser(member.getMemberId());
				finOptionService.updateById(finOption);
				break;
		}

		return result;
	}

	/**
	 * 划转方向
	 *
	 * @param dto bean 类型
	 * @param i   1-划出 0-划入
	 * @return 方向
	 */
	private String genRemark(ApiConvertDto dto, int i) {
		String before = "";
		String after = "";
		for (WalletBigType e : WalletBigType.values()) {
			if (StrUtil.equals(e.code, dto.getFrom()))
				before = e.value;
			if (StrUtil.equals(e.code, dto.getTo()))
				after = e.value;

		}
//        if (i == 1)
//            return before + "账户" + "⇋" + after + "账户";
//        else
//            return after + "账户" + "⇋" + before + "账户";

		return before + "账户" + "⇋" + after + "账户";

	}

//    /**
//     * 币币交易
//     * @param token
//     * @param dto
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public Result currency(String token, ApiCurrencyDto dto)
//    {
//
//        Member member = (Member) redisUtil.get(token);
//        String feeType = dto.getSymbols().split("/")[0];
//        //流水币种
//        String coin;
//        //用户币币账户
//        Currency currency;
//        //买单 USDT->
//        if (dto.getMatchType().equals(MatchType.BUY.code))
//        {
//            coin = dto.getSymbols().split("/")[1];
//            currency = PromotionFactory.me().getCurrency(member.getMemberId(), coin); //USDT账户
//        }
//        else
//        {
//            coin = dto.getSymbols().split("/")[0];
//            currency = PromotionFactory.me().getCurrency(member.getMemberId(), coin);//卖单 对应账户
//            feeType = dto.getSymbols().split("/")[1];
//        }
//
//        //市价、限价
//        BigDecimal unit = dto.getUnit();
//        if (StrUtil.equals(dto.getDealWay(), DealWay.MARKET.code))
//        {//市价，拉取最新价
//            unit = getClosePrice(dto.getSymbols(),KINE);
//        }
//
//        if(unit.compareTo(BigDecimal.ZERO)<=0)
//            return fail(ApiStatus.MANY_OP);
//
//        //手续费比例
//        BigDecimal rate = new BigDecimal(PromotionFactory.me().cfg(feeType + _MATCH_FEE)).divide(new BigDecimal(100), 8, RoundingMode.DOWN);
//
//
//        //余额扣除
//        BigDecimal flowPrice;
//        //手续费
//        BigDecimal fee;
//        //买
//        if (dto.getMatchType().equals(MatchType.BUY.code))
//        {
//            //扣除USDT余额
//            flowPrice = dto.getNumber().multiply(unit);
//            //手续费
//            fee = dto.getNumber().multiply(rate).multiply(unit);
//        }
//        else
//        {
//            //卖
//            //扣除对应币种账户
//            flowPrice = dto.getNumber();
//            //手续费
//            fee = dto.getNumber().multiply(rate);
//        }
//
//        if (currency.getUsedPrice().compareTo(flowPrice) < 0)
//        {
//            return fail(ApiStatus.WALLET_LESS);
//        }
//
//
//        BigDecimal beforeUsed = currency.getUsedPrice();
//        BigDecimal afterUsed = currency.getUsedPrice().subtract(flowPrice);
//
//        BigDecimal beforeLocked = currency.getLockedPrice();
//        BigDecimal afterLocked = currency.getLockedPrice().add(flowPrice);
//
//
//        //钱包更新
//        currency.setUsedPrice(afterUsed)
//                .setLockedPrice(afterLocked)
//                .setUpdateUser(member.getMemberId());
//
//        int rows = currencyService.updateWallet(currency);
//        if (rows <= 0)
//        {
//            log.error("币币交易：币币钱包更新异常！！ {}");
//
//            return fail(ApiStatus.ERROR, "交易繁忙");
//
//        }
//        //生成委托单
//        Match match = new Match();
//        String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("M");
//        BeanUtil.copyProperties(dto, match);
//        match.setMemberId(member.getMemberId())
//                .setOrderNo(orderNo)
//                .setRate(rate + "")
//                .setFee(fee)
//                .setUnFinishNumebr(flowPrice.subtract(fee))
//                .setFinishNumber(BigDecimal.ZERO)
//                .setTotalPrice(flowPrice)
//                .setRestFrozen(fee)
//                .setVersion(0L)
//                .setStatus(MatchStatus.PAID.code)
//                .setCreateUser(member.getMemberId());
//
//        matchService.save(match);
//        //流水记录
//        //可用 流出
//        PromotionFactory.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
//                flowPrice, coin, flowPrice.subtract(fee), coin, fee, coin,
//                ItemCode.USED, coin, null, "手续费比例：" + rate,
//                beforeUsed, afterUsed,
//                member.getMemberId(), member.getMemberId());
//        //冻结 流入
//        PromotionFactory.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
//                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
//                ItemCode.LOCKED, coin, null, null,
//                beforeLocked, afterLocked,
//                member.getMemberId(), member.getMemberId());
//
//
//        //放入 买卖队列 预处理队列
//        if (dto.getMatchType().equals(MatchType.BUY.code))
//        {
//            //入队 买锁
//            redisUtil.set(BUY_LOCK + dto.getSymbols(), 1);
//            redisUtil.lSet(BUY_READY + dto.getSymbols(), match);
//            redisUtil.del(BUY_LOCK + dto.getSymbols());
//        }
//        else
//        {
//            //入队 卖锁
//            redisUtil.set(SELL_LOCK + dto.getSymbols(), 1);
//            redisUtil.lSet(SELL_READY + dto.getSymbols(), match);
//            redisUtil.del(SELL_LOCK + dto.getSymbols());
//        }
//        return success("委托成功");
//
//    }

//
//    /**
//     * 币币交易  V2 优化版
//     *
//     * @param token
//     * @param dto
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public Result currencyV2(String token, ApiCurrencyDto dto) throws Exception
//    {
//
//        Member member = (Member) redisUtil.get(token);
//        if (!"1".equals(member.getRealStatus()))
//        {
//            return Result.fail(ApiStatus.NOT_REAL);
//        }
//        String feeType = dto.getSymbols().split("/")[0];
//        //流水币种
//        String coin;
//        //用户币币账户
//        Currency currency;
//        //买单 USDT->
//        if (dto.getMatchType().equals(MatchType.BUY.code))
//        {
//            coin = dto.getSymbols().split("/")[1];
//            currency = F.me().getCurrency(member.getMemberId(), coin); //USDT账户
//        }
//        else
//        {
//            coin = dto.getSymbols().split("/")[0];
//            currency = F.me().getCurrency(member.getMemberId(), coin);//卖单 对应账户
//            feeType = dto.getSymbols().split("/")[1];
//        }
//
//        //市价、限价
//        BigDecimal unit = dto.getUnit();
//        if (StrUtil.equals(dto.getDealWay(), DealWay.MARKET.code))
//        {//市价，拉取最新价
//            unit = getClosePrice(dto.getSymbols(), KINE);
//        }
//
//        if (unit.compareTo(BigDecimal.ZERO) <= 0)
//            return fail(ApiStatus.MANY_OP);
//
//        //手续费比例
//        //例如：BTC/USDT
//        // 买单-手续费计算的拉取BTC扣除 比例
//        // 卖单-手续费计算拉取USDT扣除 比例
//        BigDecimal rate = new BigDecimal(F.me().cfg(feeType + _MATCH_FEE)).divide(new BigDecimal(100), 8, RoundingMode.DOWN);
//
//
//        //余额扣除
//        BigDecimal flowPrice;
//        //手续费
//        BigDecimal fee;
//        //买 :买入btc数量
//        if (dto.getMatchType().equals(MatchType.BUY.code))
//        {
//            //扣除USDT余额 （做转换，得到实际扣除USDT量）
//            flowPrice = dto.getNumber().multiply(unit);
//            //手续费 （转换，得到实际扣除USDT量手续费）
//            fee = dto.getNumber().multiply(rate).multiply(unit);
//        }
//        else //卖：卖出BTC数量
//        {
//
//            //扣除对应币种账户 （BTC数量）
//            flowPrice = dto.getNumber();
//            //手续费 （BTC数量）
//            fee = dto.getNumber().multiply(rate);
//        }
//
//        //可用<交易量
//        if (currency.getUsedPrice().compareTo(flowPrice) < 0)
//            return fail(ApiStatus.WALLET_LESS);
//
//        //买-USDT账户
//        //卖-BTC账户
//        BigDecimal beforeUsed = currency.getUsedPrice();
//        BigDecimal afterUsed = currency.getUsedPrice().subtract(flowPrice);
//
//        BigDecimal beforeLocked = currency.getLockedPrice();
//        BigDecimal afterLocked = currency.getLockedPrice().add(flowPrice);
//
//
//        //钱包更新
//        currency.setUsedPrice(afterUsed)
//                .setLockedPrice(afterLocked)
//                .setUpdateUser(member.getMemberId());
//
//        int rows = currencyService.updateWallet(currency);
//        if (rows <= 0)
//        {
//            log.error("币币交易：币币钱包更新异常！！ {}");
//
//            return fail(ApiStatus.MANY_OP);
//
//        }
//
//        //生成委托单
//        Match match = new Match();
//        String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("M");
//        BeanUtil.copyProperties(dto, match);
//        match.setMemberId(member.getMemberId())
//                .setOrderNo(orderNo)
//                .setRate(rate + "")
//                .setFee(fee)
//                .setUnFinishNumebr(flowPrice.subtract(fee)) //买-币种为USDT 卖-币种为BTC
//                .setFinishNumber(BigDecimal.ZERO)
//                .setTotalPrice(flowPrice)
//                .setRestFrozen(fee)
//                .setVersion(0L)
//                .setStatus(MatchStatus.PAID.code)
//                .setCreateUser(member.getMemberId());
//
//        matchService.save(match);
//        //流水记录
//        //可用 流出
//        F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
//                flowPrice, coin, flowPrice.subtract(fee), coin, fee, coin,
//                ItemCode.USED, coin, null, "手续费比例：" + rate,
//                beforeUsed, afterUsed,
//                member.getMemberId(), member.getMemberId());
//        //冻结 流入
//        F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
//                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
//                ItemCode.LOCKED, coin, null, null,
//                beforeLocked, afterLocked,
//                member.getMemberId(), member.getMemberId());
//
//        //币币撮合
//        systemMatchService.matchOrder(match);
//
////        //放入 买卖队列 预处理队列
////        if (dto.getMatchType().equals(MatchType.BUY.code))
////        {
////            //入队 买锁
////            redisUtil.set(BUY_LOCK + dto.getSymbols(), 1);
////            redisUtil.lSet(BUY_READY + dto.getSymbols(), match);
////            redisUtil.del(BUY_LOCK + dto.getSymbols());
////        }
////        else
////        {
////            //入队 卖锁
////            redisUtil.set(SELL_LOCK + dto.getSymbols(), 1);
////            redisUtil.lSet(SELL_READY + dto.getSymbols(), match);
////            redisUtil.del(SELL_LOCK + dto.getSymbols());
////        }
//
//        return success(ApiStatus.OK_CURRENCY);
//
//    }
//
//    //

	/**
	 * 获取币种最新价
	 *
	 * @param symbols
	 * @param type    现货:KINE 期货-永续:KINE_PERPETUAL
	 * @return
	 */
	private BigDecimal getClosePrice(String symbols, String type) {
		int number = 8;
		if ("KINE".equals(type)) {
			Spot spot = new Spot();
			spot.setDel("N");
			spot.setSymbol(symbols);
			spot = spotService.getOne(new QueryWrapper<>(spot));
			if (spot != null) {
				number = spot.getNumber();
			}
		} else if (KINE_FUTURES.equals(type)) {
			String zh = symbols.split("/")[0];
			symbols = FuturesType.getCode(zh);
		} else {
			Swap swap = new Swap();
			swap.setDel("N");
			swap.setSymbol(symbols);
			swap = swapService.getOne(new QueryWrapper<>(swap));
			if (swap != null) {
				number = swap.getNumber();
			}
		}
		Kline map = redisUtil.getBean(type + symbols + _NEW, Kline.class);
		return map == null ? null : new BigDecimal(map.getClose()).setScale(number, BigDecimal.ROUND_HALF_UP);
	}

	//撤销币币委托单
	public Result cancelCurrency(String token, Long matchId) {
		Member member = (Member) redisUtil.get(token);
		Match matchQ = new Match();
		matchQ.setMemberId(member.getMemberId())
				.setMatchId(matchId).setDel("N");
		Match matchR = matchService.getOne(new QueryWrapper<>(matchQ).in("status", MatchStatus.PAID.code, MatchStatus.PART_MATCH.code));
		if (matchR == null)
			return fail(ApiStatus.NOT_FOUND);
		if (redisUtil.get(MATCH_LOCK + matchR.getOrderNo()) != null)
			return fail(ApiStatus.MATCHING);
		//加锁
		redisUtil.set(MATCH_LOCK + matchR.getOrderNo(), 1);

		String coin = "USDT";
		Currency currency;
		//冻结返还数量
		BigDecimal returnNumber = BigDecimal.ZERO;

		//计算扣除手续费，返还剩余数量
		if (StrUtil.equals(matchR.getMatchType(), MatchType.BUY.code)) {

			//买单
			//返还USDT
			currency = F.me().getCurrency(member.getMemberId(), coin);
			//
//            feePrice = matchR.getFee().multiply(rate);

			//冻结返还数量
//            returnNumber=matchR.getTotalPrice().subtract(matchR.getFinishNumber()).subtract(feePrice);

			//USDT直接返还
			returnNumber = matchR.getTotalPrice().subtract(matchR.getFinishNumber());


		} else {
			coin = matchR.getSymbols().split("/")[0];
			//卖单
			//返还对应币种 BTC
			currency = F.me().getCurrency(member.getMemberId(), coin);
//            feePrice = matchR.getFeeNumber().multiply(rate);

			//冻结返还数量
//            returnNumber=matchR.getWill().subtract(matchR.getOk()).subtract(feePrice);
			returnNumber = matchR.getWill().subtract(matchR.getOk());
		}

		BigDecimal beforeUsd = currency.getUsedPrice();
		BigDecimal afterUsd = beforeUsd.add(returnNumber);

		BigDecimal beforeLock = currency.getLockedPrice();
		BigDecimal afterLock = beforeLock.subtract(returnNumber);

		currency.setLockedPrice(afterLock.compareTo(MIN_DATA) > 0 ? afterLock : BigDecimal.ZERO)
				.setUsedPrice(afterUsd.compareTo(MIN_DATA) > 0 ? afterUsd : BigDecimal.ZERO)
				.setUpdateUser(member.getMemberId());

		if (!currencyService.updateById(currency))
			throw new UpdateDataException(100);

		F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_CANCEL,
				returnNumber, coin, returnNumber, coin, BigDecimal.ZERO, coin,
				ItemCode.LOCKED, coin, null, null,
				beforeLock, afterLock,
				member.getMemberId(), member.getMemberId());

		F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_CANCEL,
				returnNumber, coin, returnNumber, coin, BigDecimal.ZERO, coin,
				ItemCode.USED, coin, null, null,
				beforeUsd, afterUsd,
				member.getMemberId(), member.getMemberId());


		matchR.setStatus(matchR.getOk().compareTo(matchR.getActual()) == 0 ? MatchStatus.CANCEL.code : MatchStatus.CANCEL_FINISH.code)
				.setUpdateUser(member.getMemberId());
		matchService.updateById(matchR);

		//解锁
		redisUtil.del(MATCH_LOCK + matchR.getOrderNo());

		return success();
	}

	public Result currencyPage(String token, String type, String matchType) {
		Member member = (Member) redisUtil.get(token);
		HashedMap map = new HashedMap();
		String[] symbols = type.split("/");
		String feeType = "";
		String coin = "";
		if (matchType.equals("BUY")) {
			feeType = symbols[0];
			coin = symbols[1];
		} else {
			feeType = symbols[1];
			coin = symbols[0];
		}
		Spot spot = F.me().getSpot(type);
		map.put("feeRate", spot.getSpotFee());
		if ("SELL".equals(matchType)) {
			map.put("feeRate", spot.getUsdtSpotFee());
		}
		map.put("minBuyNumber", F.me().getSpot(type).getMinBuyNumber());
		map.put("price", F.me().getCurrency(member.getMemberId(), coin).getUsedPrice());
		return success("币币交易页面信息", map);
	}

	/**
	 * 委托记录
	 *
	 * @param token 令牌
	 * @param type  类型 1-当前委托 2-买入  3-卖出 4-撤销,5全部类型
	 * @param page  分页
	 * @return 委托记录
	 */
	public Result currencyRecord(String token, int type, Page page, String symbols) {
		Member member = (Member) redisUtil.get(token);
		if (StringUtils.isNotBlank(symbols)) {
			symbols = symbols + "/USDT";
		}
		IPage<Match> iPage = matchService.getBaseMapper().getPagesList(page, member.getMemberId(), type, symbols);
		List<Match> records = iPage.getRecords();
		IPage<MatchDto> dtoIPage = new Page<>();
		List<MatchDto> list = new ArrayList<>();
		records.forEach(e -> {
			MatchDto matchDto = new MatchDto();
			BeanUtil.copyProperties(e, matchDto);
			//委托量 BTC
			matchDto.setWillNumberB(e.getWill().toPlainString());
			//获取成交明细订单
			List<MatchDetail> matchDetailList = matchDetailService.getBaseMapper().getMatchListByOrderNo(e.getOrderNo());
			//成交量 BTC
			BigDecimal totalB = BigDecimal.ZERO;
			BigDecimal div = BigDecimal.ZERO;

			Spot spot = new Spot();
			spot.setSymbol(e.getSymbols());
			spot.setDel("N");
			spot.setStatus("Y");
			spot = this.spotService.getOne(new QueryWrapper<>(spot));
			if (spot != null) {
				BigDecimal feePrice = e.getWill().multiply(spot.getSpotFee()).divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
				if ("SELL".equals(e.getMatchType())) {
					feePrice = e.getWill().multiply(e.getUnit()).multiply(spot.getUsdtSpotFee()).divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
				}
				//log.info(feePrice.toPlainString()+"=="+e.getWill());
				matchDto.setNumberFee(feePrice.toPlainString());
				//计算
				for (MatchDetail matchDetail : matchDetailList) {
					totalB = totalB.add(new BigDecimal(matchDetail.getRemark()));
					div = div.add(new BigDecimal(matchDetail.getRemark()).multiply(matchDetail.getUnit()));
				}
				if (totalB.compareTo(BigDecimal.ZERO) > 0 && div.compareTo(BigDecimal.ZERO) > 0) {
					//成交均价=[(成交价1*成交量1)+(成交价2*成交量2)]/(成交量1+成交量2)
					BigDecimal avgUnit = div.divide(totalB, 8, BigDecimal.ROUND_DOWN);
					//成交总额=成交均价*成交量
					BigDecimal numberU = avgUnit.multiply(totalB);
					matchDto.setAvgUnit(avgUnit.toPlainString())
							.setNumbersB(totalB.toPlainString())
							.setNumbersU(numberU.toPlainString())
							.setNumberFee(StrUtil.equals(e.getMatchType(), MatchType.BUY.code)
									? totalB.multiply(new BigDecimal(e.getRate())).toPlainString()
									: numberU.multiply(new BigDecimal(e.getRate())).toPlainString()) //买入手续费：成交量*手续费比例 卖出：成交总额*手续费比例
					;
				}
				list.add(matchDto);
			}

		});

		dtoIPage.setRecords(list).setSize(iPage.getSize()).setPages(iPage.getPages()).setCurrent(iPage.getCurrent()).setTotal(iPage.getTotal());

		return success("委托记录", dtoIPage);

	}

	/**
	 * 合约页面信息
	 *
	 * @param token   token
	 * @param symbols 交易对
	 * @param coin    币种 USDT MGE
	 * @return Result
	 */
	public Result contractPage(String token, String symbols, String coin, String handlerType) {
		if (StringUtils.isBlank(symbols)) {
			return Result.fail(ApiStatus.ERROR);
		}
		Member member = (Member) redisUtil.get(token);
		Contract contract = (Contract) redisUtil.get(String.format(CONTRACT_CODE, coin) + member.getMemberId());
		if (contract == null) {
			return fail(ApiStatus.BAD_REQUEST);
		}

		Swap swap = new Swap();
		swap.setSymbol(symbols);
		swap.setDel("N");
		swap = swapService.getOne(new QueryWrapper<>(swap));
		if (swap == null) return Result.fail(ApiStatus.ERROR);

		HashedMap map = new HashedMap();
		map.put("handNumber", swap.getHandNumber());//一手价值
		map.put("openFeePrice", swap.getOpenFeePrice().toPlainString());//开仓手续费率
		map.put("symbols", symbols);//币种
		map.put("price", contract.getUsedPrice().toPlainString());//可用保证金
		Leverage leverage = new Leverage();
		leverage.setDel("N");
		List<Leverage> list;
		if(handlerType.equals("CURRENT")){
			leverage.setValue(BigDecimal.ONE);
			list = leverageService.list(new QueryWrapper<>(leverage));
			if(list.isEmpty()){
				leverage.setName("1x");
				leverage.setCreateTime(new Date());
				leverage.setCreateUser(1L);
				leverageService.addLeverage(leverage);
				list.add(leverage);
			}
		}else {
			list = leverageService.list(new QueryWrapper<>(leverage));
		}
		List<Map<String, Object>> leverageList = new ArrayList<>(list.size());
		for (Leverage leverage1 : list) {
			Map<String, Object> leverageMap = new HashMap<>();
			leverageMap.put("id", leverage1.getLeverageId());
			leverageMap.put("name", leverage1.getName());
			leverageList.add(leverageMap);
		}
		map.put("leverageList", leverageList);//杠杠倍数
		return success("合约页面信息", map);
	}

	public Result leverage(String token) {
		Leverage leverage = new Leverage();
		leverage.setDel("N");

		return success(leverageService.list(new QueryWrapper<>(leverage)));
	}

	/**
	 * 合约交易t
	 */
	@Transactional(rollbackFor = Exception.class)
	public Result contract(String token, ApiContractDto dto) throws Exception {
		synchronized (this) {
			Member member = (Member) redisUtil.get(token);
			if (!"1".equals(member.getRealStatus())) {
				return Result.fail(ApiStatus.NOT_REAL);
			}

			//获取火币的最新行情价
			BigDecimal close = getClosePrice(dto.getSymbols(), KINE);
			if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)) {//限价，判断是买入模式是否委托价大于当前行情价
				// ，卖出模式是否委托价小于当前行情价
				if (StrUtil.equals(dto.getCompactType(), CompactType.BUY.code) && dto.getUnit().compareTo(close) > 0) {
					return Result.fail(ApiStatus.SELL_UNIT_MAX);
				}
				if (StrUtil.equals(dto.getCompactType(), CompactType.SELL.code) && dto.getUnit().compareTo(close) < 0) {
					return Result.fail(ApiStatus.BUY_UNIT_MAX);
				}
			}

			Swap swap = new Swap();
			swap.setSymbol(dto.getSymbols());
			swap.setDel("N");
			swap = this.swapService.getOne(new QueryWrapper<>(swap));
			if (swap == null) {
				return Result.fail(ApiStatus.ERROR);
			}
			//获取杠杆倍率
			Leverage leverage = leverageService.getById(dto.getLeverageId());
			Contract contract = F.me().getContract(member.getMemberId(), "USDT");
			//市价、限价
			BigDecimal unit = dto.getUnit();
			if (StrUtil.equals(dto.getDealWay(), DealWay.MARKET.code)) {//市价，拉取最新价
				unit = close;
			}
			boolean isConOpen = F.me().cfg(CONTRACT_OPEN).equals(Y);
			BigDecimal ctrlFee = new BigDecimal(F.me().cfg(CONTRACT_FEE)).multiply(dto.getNumbers());
			BigDecimal ctrlPrice = new BigDecimal(F.me().cfg(CONTRACT_MARKET_PRICE)).multiply(dto.getNumbers());

			//获取手续费比例
			BigDecimal contractFee = swap.getOpenFeePrice().divide(new BigDecimal(100), 8, RoundingMode.DOWN);
			//开仓手续费 =手数*每手的价值*开仓手续费率*委托价格  。
			BigDecimal fee = isConOpen ? ctrlFee : dto.getNumbers().multiply(new BigDecimal(swap.getHandNumber())).multiply(unit).multiply(contractFee);
			//开仓保证金=委托手数*每手价值数量*委托价格/杠杆倍数
			BigDecimal openPrice = isConOpen ? ctrlPrice : dto.getNumbers().multiply(new BigDecimal(swap.getHandNumber()))
					.multiply(unit).divide(leverage.getValue(), 8, BigDecimal.ROUND_HALF_UP);
			//委托占用=仓位保证金+开仓手续费
			//委托占用
			BigDecimal totalPayPrice = fee.add(openPrice).setScale(4, BigDecimal.ROUND_HALF_UP);
			//用户合约资产
			Contract contractObj = (Contract) redisUtil.get(String.format(CONTRACT_CODE, dto.getCoin()) + member.getMemberId());
			//实际可用部分
			BigDecimal actualPrice = contract.getUsedPrice();
			if (contractObj.getNoPl().compareTo(BigDecimal.ZERO) < 0) {
				actualPrice = actualPrice.add(contractObj.getNoPl());
			}
			//查询可用保证金够不够扣
			if (actualPrice.compareTo(totalPayPrice) < 0)
				return fail(ApiStatus.NO_ORDER_MONEY);
			//默认未平仓
			String status = CompactStatus.N.code;
			//订单状态处理
			//“卖出开空”：用户输入价 < 当前行情价时，订单显示在当前委托中
			if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)
					&& StrUtil.equals(dto.getCompactType(), CompactType.SELL.code)
					&& dto.getUnit().compareTo(close) > 0) {
				status = CompactStatus.IN.code;//委托中
			}
			//“买入开多”:用户输入价 > 当前行情价时，订单显示在当前委托中
			if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)
					&& StrUtil.equals(dto.getCompactType(), CompactType.BUY.code)
					&& dto.getUnit().compareTo(close) < 0) {
				status = CompactStatus.IN.code;
			}
			//建仓价计算
			BigDecimal tradePrice;
			if (StrUtil.equals(dto.getCompactType(), CompactType.BUY.code)) {
				//买涨  当前价+点差
				tradePrice = unit.add(BigDecimal.ZERO);
			} else {
				//买跌
				tradePrice = unit.subtract(BigDecimal.ZERO);
			}
			BigDecimal willWorth = contractObj.getWorthPrice().subtract(fee);
			if (willWorth.compareTo(BigDecimal.ZERO) <= 0)
				return fail(ApiStatus.NO_MONEY);

			//生成订单
			Compact compact = new Compact();
			compact.setCompactType(dto.getCompactType())
					.setDealWay(dto.getDealWay())
					.setTradePrice(tradePrice)
					.setOpenFeeRaito(swap.getOpenFeePrice())
					.setFee(fee)
					.setUnit(unit)
					.setOrderNo(cn.stylefeng.guns.modular.base.util.RandomUtil.code("C"))
					.setSymbols(dto.getSymbols())
					.setNumbers(dto.getNumbers())
					.setPositionPrice(openPrice)
					.setTotalPrice(dto.getNumbers())
					.setMemberId(member.getMemberId())
					.setLeverName(leverage.getName())
					.setLeverRate(leverage.getValue())
					.setStatus(status)
					.setCoin(dto.getCoin())
					.setHandNumber(swap.getHandNumber())
					.setCreateUser(member.getMemberId());
			compact.setHandlerType(dto.getHandlerType());
			compactService.save(compact);

			String flowCoin = dto.getCoin();


			//变更用户的可用余额
			BigDecimal userPrice = contract.getUsedPrice();
			contract.setUsedPrice(contract.getUsedPrice().subtract(totalPayPrice));
			contract.setUpdateTime(new Date());
			contract.setUpdateUser(1L);
			while (true) {
				if (this.contractService.updateWallet(contract) > 0) {
					//流水生成可用项
					F.me().saveCashflow(member.getMemberId(), WalletBigType.CONTRACT,
							CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CONTRACT, totalPayPrice,
							flowCoin, totalPayPrice, flowCoin, fee, flowCoin,
							ItemCode.USED, flowCoin, null, null,
							userPrice, contract.getUsedPrice(),
							member.getMemberId(), SYS_PLATFORM);
					break;
				}
			}
			jobService.refreshContractInfo(member.getMemberId(), flowCoin);
			redisUtil.lSet(OP_CONTRACT_DATA, member.getMemberId());
		}
		return success(ApiStatus.OK);
	}

	/**
	 * 合约订单列表
	 *
	 * @param token
	 * @param type       页面类型 N-持仓 IN-当前委托 Y-平仓订单
	 * @param showMethod 持仓或当前委托需传递显示的数据：ALL显示全部，CURRENT 当前的币种
	 * @param symbols    币种
	 * @param buyMethod  下单方式：BUY或者SELL
	 * @param status     状态类型：手动平仓：HANDLE，FIXED：强制平仓，PROFIT： 止盈平仓，LOSS：止损平仓
	 * @param handlerType     操作类型：CONTRACT：合约交易, CURRENT：现货交易：
	 * @param page
	 * @return Result
	 */
	public Result contractList(String token, String type, Page page,
							   String showMethod, String symbols, String buyMethod, String status, String handlerType) {
		Member member = (Member) redisUtil.get(token);
		Compact compact = new Compact();
		compact.setMemberId(member.getMemberId()).setDel("N");
		compact.setStatus(type);
		compact.setHandlerType(handlerType);
		if ("CURRENT".equals(showMethod)) {
			compact.setSymbols(symbols);
		}
		if ("Y".equals(type)) {
			if (StringUtils.isNotBlank(symbols)) compact.setSymbols(symbols);
			if (StringUtils.isNotBlank(buyMethod)) compact.setCompactType(buyMethod);
			if (StringUtils.isNotBlank(status)) compact.setExitType(status);
		}
		IPage iPage = compactService.page(page, new QueryWrapper<>(compact).orderByDesc("exit_time").orderByDesc(CREATE_TIME));
		List<Compact> dtoList = iPage.getRecords();
		List<ApiCompactDto> list = new ArrayList<>(dtoList.size());
		for (Compact entity : dtoList) {
			Object pl = entity.getPl();
			ApiCompactDto apiCompactDto = new ApiCompactDto();
			Double lossProfitRatio = 0.0;
			if (redisUtil.get(PL + entity.getOrderNo()) != null && type.equals("N")) {
				pl = redisUtil.get(PL + entity.getOrderNo());
				lossProfitRatio = new BigDecimal(pl + "").subtract(entity.getFee() != null ? entity.getFee() : BigDecimal.ZERO).subtract(entity.getCloseFeePrice() !=null ? entity.getCloseFeePrice() : BigDecimal.ZERO).divide((
						entity.getTradePrice().multiply(entity.getNumbers())
								.multiply(new BigDecimal(entity.getHandNumber()))
				), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
			} else if ("Y".equals(type)) {
				lossProfitRatio = new BigDecimal(pl + "").subtract(entity.getFee()).subtract(entity.getCloseFeePrice()).divide((
						entity.getTradePrice().multiply(entity.getHandPrice())
				), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
			}
			apiCompactDto.setCompactId(entity.getCompactId());
			apiCompactDto.setOrderNo(entity.getOrderNo());
			apiCompactDto.setSymbols(entity.getSymbols());
			apiCompactDto.setCompactType(entity.getCompactType());
			apiCompactDto.setLeverName(entity.getLeverName());
			apiCompactDto.setPositionPrice(entity.getPositionPrice().toPlainString());
			apiCompactDto.setTradePrice(entity.getTradePrice().toPlainString());
			apiCompactDto.setOpenHandPrice((entity.getHandNumber() * entity.getNumbers().doubleValue()) + "");
			apiCompactDto.setHandNumber(entity.getHandNumber() + "");
			if (!"Y".equals(type)) {
				Swap swap = new Swap();
				swap.setSymbol(entity.getSymbols());
				swap.setDel("N");
				swap = this.swapService.getOne(new QueryWrapper<>(swap));
				//平仓手续费比例
				apiCompactDto.setExitFeeRatio(swap.getCloseFeePrice().toPlainString());
			}
			apiCompactDto.setFee(entity.getFee().toPlainString());
			if (entity.getStopLoss() != null)
				apiCompactDto.setStopLoss(entity.getStopLoss().toPlainString());
			if (entity.getStopProfit() != null)
				apiCompactDto.setStopProfit(entity.getStopProfit().toPlainString());
			if ("Y".equals(type)) {
				apiCompactDto.setExitPositionPrice(entity.getExitPositionPrice().toPlainString());
				apiCompactDto.setExitPrice(entity.getExitPrice().toPlainString());
				apiCompactDto.setCloseNumber(entity.getCloseNumber());
				apiCompactDto.setHandPrice(entity.getHandPrice().toPlainString());
				apiCompactDto.setCloseFeePrice(entity.getCloseFeePrice().toPlainString());
				for (ExitType exitType : ExitType.values()) {
					if (exitType.code.equals(entity.getExitType())) {
						apiCompactDto.setExitType(exitType.value);
					}
				}
			}
			apiCompactDto.setLossProfit("0.0");
			if (pl != null) {
				apiCompactDto.setLossProfit(new BigDecimal(pl + "").subtract(entity.getFee()!= null ? entity.getFee() : BigDecimal.ZERO).subtract(entity.getCloseFeePrice() !=null ? entity.getCloseFeePrice() : BigDecimal.ZERO).toPlainString() + "");
			}
			apiCompactDto.setLossProfitRatio(lossProfitRatio + "%");
			apiCompactDto.setCreateTime(entity.getCreateTime());
			apiCompactDto.setExitTime(entity.getExitTime());
			apiCompactDto.setNumbers(entity.getNumbers());
			list.add(apiCompactDto);
		}
		iPage.setRecords(list);
		return success(iPage);

	}

	@Transactional(rollbackFor = Exception.class)
	public Result cancelContract(String token, Long compactId, Integer type, String handlerType) {
		Member member = (Member) redisUtil.get(token);
		if (type <= 1) {
			Compact compact = compactService.getById(compactId);
			if (!StrUtil.equals(compact.getStatus(), CompactStatus.IN.code))
				return fail(ApiStatus.CANCEL_FAIL);
			synchronized (this) {
				compact.setStatus(CompactStatus.CANCEL.code).setUpdateUser(member.getMemberId());
				compactService.updateById(compact);

				String flowCoin = compact.getCoin();
				//保证金退回
				Contract contract = F.me().getContract(member.getMemberId(), flowCoin);
				//保证金项
				F.me().saveCashflow(member.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONTRACT_CANCEL,
						compact.getPositionPrice().add(compact.getFee()), flowCoin, compact.getPositionPrice().add(compact.getFee()), flowCoin, BigDecimal.ZERO, flowCoin,
						ItemCode.USED, flowCoin, null, null,
						contract.getUsedPrice(), contract.getUsedPrice().add(compact.getPositionPrice().add(compact.getFee())),
						SYS_PLATFORM, member.getMemberId());

				//更新钱包
				contract.setUsedPrice(contract.getUsedPrice().add(compact.getPositionPrice().add(compact.getFee())))
						.setEntrustPrice(contract.getEntrustPrice().subtract(compact.getPositionPrice().add(compact.getFee())));
				contract.setUpdateTime(new Date());
				contract.setUpdateUser(member.getMemberId());
				while (true) {
					if (contractService.updateWallet(contract) > 0) {
						break;
					}
				}
				jobService.refreshContractInfo(member.getMemberId(), flowCoin);
				redisUtil.lSet(OP_CONTRACT_DATA, member.getMemberId());
			}
			return success(ApiStatus.OK);
		}
		synchronized (this) {
			//全部委托订单撤销
			Compact compact = new Compact();
			compact.setStatus(CompactStatus.IN.code);
			compact.setDel("N");
			compact.setMemberId(member.getMemberId());
			compact.setHandlerType(handlerType);
			List<Compact> list = compactService.list(new QueryWrapper<>(compact));
			if (list != null && list.size() > 0) {
				//redisUtil.lSet(OP_CONTRACT_DATA, member.getMemberId());
				for (Compact compact1 :
						list) {
					Compact entity = compactService.getById(compact1.getCompactId());
					if (!StrUtil.equals(entity.getStatus(), CompactStatus.IN.code)) {
						continue;
					}
					entity.setStatus(CompactStatus.CANCEL.code).setUpdateUser(member.getMemberId());
					compactService.updateById(entity);

					String flowCoin = entity.getCoin();
					//保证金退回
					Contract contract = F.me().getContract(member.getMemberId(), flowCoin);
					//保证金项
					F.me().saveCashflow(member.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CONTRACT_CANCEL,
							entity.getPositionPrice().add(entity.getFee()), flowCoin, entity.getPositionPrice().add(entity.getFee()), flowCoin, BigDecimal.ZERO, flowCoin,
							ItemCode.USED, flowCoin, null, null,
							contract.getUsedPrice(), contract.getUsedPrice().add(entity.getPositionPrice().add(entity.getFee())),
							SYS_PLATFORM, member.getMemberId());

					//更新钱包
					contract.setUsedPrice(contract.getUsedPrice().add(entity.getPositionPrice().add(entity.getFee())))
							.setEntrustPrice(contract.getEntrustPrice().subtract(entity.getPositionPrice().add(entity.getFee())));
					contract.setUpdateTime(new Date());
					contract.setUpdateUser(member.getMemberId());
					while (true) {
						if (contractService.updateWallet(contract) > 0) {
							break;
						}
					}
					// jobService.refreshContractInfo(member.getMemberId(), flowCoin);
				}
			}
		}
		return success(ApiStatus.OK);
	}

	public Result contractPl(String token, ApiPlDto dto) {
		Member member = (Member) redisUtil.get(token);
		Compact compactQ = new Compact();
		compactQ.setMemberId(member.getMemberId())
				.setCompactId(dto.getCompactId())
				.setDel("N")
				.setStatus(CompactStatus.N.code);
		Compact compactR = compactService.getOne(new QueryWrapper<>(compactQ));
		if (compactR == null) {
			return fail(ApiStatus.ERROR, "订单不存在");
		}
		//当前行情
		BigDecimal close = getClosePrice(compactR.getSymbols(), KINE);

		if (StrUtil.equals(compactR.getCompactType(), CompactType.BUY.code)) {//买涨
			if (StringUtils.isNotBlank(dto.getStopProfit())
					&& new BigDecimal(dto.getStopProfit()).compareTo(compactR.getTradePrice()) <= 0) {
				return fail(ApiStatus.MAX_STOP_PROFIT);
			}

			if (StringUtils.isNotBlank(dto.getStopLoss())
					&& new BigDecimal(dto.getStopLoss()).compareTo(compactR.getTradePrice()) >= 0) {
				return fail(ApiStatus.MIN_STOP_LOSS);
			}
			if (StringUtils.isNotBlank(dto.getStopLoss()) && new BigDecimal(dto.getStopLoss()).compareTo(close) >= 0) {
				return fail(ApiStatus.NOT_UP_LOSS);
			}

		} else {//买跌
			if (StringUtils.isNotBlank(dto.getStopProfit())
					&& new BigDecimal(dto.getStopProfit()).compareTo(compactR.getTradePrice()) >= 0) {
				return fail(ApiStatus.MIN_STOP_PROFIT);
			}
			if (StringUtils.isNotBlank(dto.getStopLoss())
					&& new BigDecimal(dto.getStopLoss()).compareTo(compactR.getTradePrice()) <= 0) {
				return fail(ApiStatus.MAX_STOP_LOSS);
			}
			if (StringUtils.isNotBlank(dto.getStopLoss()) && new BigDecimal(dto.getStopLoss()).compareTo(close) <= 0) {
				return fail(ApiStatus.NOT_DOWN_LOSS);
			}
		}
		if (StringUtils.isNotBlank(dto.getStopLoss())) {
			compactR.setStopLoss(new BigDecimal(dto.getStopLoss()));
		}
		if (StringUtils.isNotBlank(dto.getStopProfit())) {
			compactR.setStopProfit(new BigDecimal(dto.getStopProfit()));
		}
		compactR.setUpdateTime(new Date());
		compactR.setUpdateUser(member.getMemberId());
		compactService.updateById(compactR);
		jobService.refreshContractInfo(member.getMemberId(), compactR.getCoin());
		redisUtil.lSet(OP_CONTRACT_DATA, member.getMemberId());
		return success(ApiStatus.OK);
	}

	/**
	 * 平仓
	 *
	 * @param token     token
	 * @param compactId compactId
	 * @param type
	 * @return 平仓
	 */
	@Transactional(rollbackFor = Exception.class)
	public Result outContract(String token, Long compactId, Integer type, BigDecimal number, String handlerType) {
		synchronized (this) {
			Member member = (Member) redisUtil.get(token);
			if (type <= 1) {
				Compact compactQ = new Compact();
				compactQ.setMemberId(member.getMemberId())
						.setCompactId(compactId)
						.setDel("N")
						.setStatus(CompactStatus.N.code)
						.setHandlerType(handlerType);
				Compact compactR = compactService.getOne(new QueryWrapper<>(compactQ));
				if (compactR == null)
					fail(ApiStatus.NOT_OUT_CONTRACT);
				if (number.compareTo(BigDecimal.ZERO) <= 0) {
					return Result.fail(ApiStatus.MIN_ZERO_NUMBER);
				}
				if (number.compareTo(compactR.getNumbers()) > 0) {
					return Result.fail(ApiStatus.EXIT_NUMBER_NO);
				}
				dealOutContract(compactR, number, member);
				return success(ApiStatus.OK);
			}
			Compact compactQ = new Compact();
			compactQ.setMemberId(member.getMemberId())
					.setDel("N")
					.setStatus(CompactStatus.N.code)
					.setHandlerType(handlerType);
			List<Compact> list = compactService.list(new QueryWrapper<>(compactQ));
			for (Compact compact : list) {
				dealOutContract(compact, compact.getNumbers(), member);
			}
			return success(ApiStatus.OK);
		}

	}

	@Transactional
	public void dealOutContract(Compact compactR, BigDecimal number, Member member) {
		boolean flag = true;
		if (compactR.getNumbers().compareTo(number) > 0) {
			flag = false;
		}
		//当前行情价
		BigDecimal currentUnit = getClosePrice(compactR.getSymbols(), KINE);
		//总盈亏
		BigDecimal totalPlPrice = BigDecimal.ZERO;
		//最新行情价
		BigDecimal close = getClosePrice(compactR.getSymbols(), KINE);
		//平仓价
		BigDecimal outDivTrade = close;
		//BigDecimal levMulNum = compactR.getLeverRate().multiply(number);
		BigDecimal levMulNum = number;
		//平仓价值数量
		BigDecimal handPrice = levMulNum.multiply(new BigDecimal(compactR.getHandNumber()));
		//买涨
		if (StrUtil.equals(compactR.getCompactType(), CompactType.BUY.code)) {
			//做多盈亏 =平仓
			BigDecimal tradeOutNum = (outDivTrade.subtract(compactR.getTradePrice()));
			totalPlPrice = tradeOutNum.multiply(handPrice);
		}
		//买跌
		if (StrUtil.equals(compactR.getCompactType(), CompactType.SELL.code)) {
			BigDecimal tradeOutNum = (compactR.getTradePrice().subtract(outDivTrade));
			totalPlPrice = tradeOutNum.multiply(handPrice);
		}

		Swap swap = new Swap();
		swap.setSymbol(compactR.getSymbols());
		swap.setDel("N");
		swap = this.swapService.getOne(new QueryWrapper<>(swap));
		//平仓手续费比例
		BigDecimal outFeeRate = swap.getCloseFeePrice().divide(new BigDecimal(100));
		//平仓手续费=平仓手数*每手价值数量*平仓价格*平仓手续费率
		BigDecimal outFee = handPrice
				.multiply(outDivTrade)
				.multiply(outFeeRate);
		BigDecimal exitPostionPrice = number
				.multiply(compactR.getPositionPrice())
				.divide(compactR.getNumbers(), 8, BigDecimal.ROUND_HALF_UP);
		BigDecimal totalReturnPrice = exitPostionPrice.add(totalPlPrice).subtract(outFee);

		if (flag) {//表示该订单已完成，无需在创建
			compactR.setExitPrice(close)
					.setExitType(ExitType.HANDLE.code)
					.setStatus(CompactStatus.Y.code)
					.setExitTime(new Date())
					.setTpl(totalPlPrice)//盈亏额
					.setCloseNumber(number)//平仓手数
					.setPl(totalPlPrice)//盈亏
					.setExitPositionPrice(compactR.getPositionPrice())//平仓保证金
					.setCloseFeePrice(outFee)
					.setHandPrice(handPrice)
					.setUpdateUser(compactR.getMemberId())
			;
			compactService.updateById(compactR);
		} else {
			//生成订单
			Compact compact = new Compact();
			compact.setCompactType(compactR.getCompactType())
					.setDealWay(compactR.getDealWay())
					.setTradePrice(compactR.getTradePrice())
					.setFee(compactR.getFee())
					.setUnit(compactR.getUnit())
					.setOrderNo(cn.stylefeng.guns.modular.base.util.RandomUtil.code("C"))
					.setSymbols(compactR.getSymbols())
					.setNumbers(compactR.getNumbers())
					.setPositionPrice(compactR.getPositionPrice())
					.setMemberId(member.getMemberId())
					.setLeverName(compactR.getLeverName())
					.setLeverRate(compactR.getLeverRate())
					.setStatus(CompactStatus.Y.code)
					.setCoin(compactR.getCoin())
					.setHandNumber(compactR.getHandNumber())
					.setExitPrice(currentUnit)
					.setExitType(ExitType.HANDLE.code)
					.setExitTime(new Date())
					.setTpl(totalPlPrice)
					.setCloseNumber(number)
					.setPl(totalPlPrice)
					.setCloseFeePrice(outFee)
					.setHandPrice(handPrice)
					.setExitPositionPrice(exitPostionPrice)
					.setCreateUser(member.getMemberId());
			compactService.save(compact);

			//重新进行老的剩余的仓位保证金
			BigDecimal num = compactR.getNumbers().subtract(number);
			BigDecimal newPostionPrice = new BigDecimal(compactR.getHandNumber())
					.multiply(num)
					.multiply(compactR.getTradePrice())
					.divide(compactR.getLeverRate(), 8, BigDecimal.ROUND_HALF_UP);
			compactR.setNumbers(num);
			compactR.setPositionPrice(newPostionPrice);
			BigDecimal newFee = compactR.getUnit().multiply(compactR.getNumbers())
					.multiply(new BigDecimal(compactR.getHandNumber())).multiply(compactR.getOpenFeeRaito().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP));
			compactR.setFee(newFee);
			compactR.setUpdateTime(new Date());
			compactR.setUpdateUser(member.getMemberId());
			compactService.updateById(compactR);
		}

		String coin = compactR.getCoin();
		//配资账户
		Contract entrust = F.me().getContract(member.getMemberId(), compactR.getCoin());
		BigDecimal usedPrice = entrust.getUsedPrice().add(totalReturnPrice).compareTo(BigDecimal.ZERO) < 0
				? BigDecimal.ZERO : entrust.getUsedPrice().add(totalReturnPrice);
		if (totalPlPrice.compareTo(BigDecimal.ZERO) > 0) {
			F.me().saveCashflow(compactR.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.HANDLE,
					totalPlPrice, coin, totalPlPrice, coin, outFee, coin,
					ItemCode.USED, coin, null, "盈利",
					entrust.getUsedPrice(), usedPrice,
					compactR.getMemberId(), compactR.getMemberId());
		} else if (totalPlPrice.compareTo(BigDecimal.ZERO) < 0) {
			F.me().saveCashflow(compactR.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.HANDLE,
					totalPlPrice.abs(), coin, totalPlPrice.abs(), coin, outFee, coin,
					ItemCode.USED, coin, null, "亏损",
					entrust.getUsedPrice(), usedPrice,
					compactR.getMemberId(), compactR.getMemberId());
		}

		entrust.setUsedPrice(usedPrice);
		entrust.setUpdateUser(-1L);
		entrust.setUpdateTime(new Date());
		while (true) {
			if (contractService.updateWallet(entrust) > 0) {
				log.info("用户平仓,更新钱包:{}", member.getMemberId());
				break;
			}
		}
		jobService.refreshContractInfo(compactR.getMemberId(), coin);
		log.info("用户平仓:{}", member.getMemberId());
	}

	public Result legalPage(String token) {
		Member member = (Member) redisUtil.get(token);

		Map<String, Object> map = new HashedMap();
		map.put("unit", F.me().cfg(LEGAL_USDT));
		map.put("min", F.me().cfg(LEGAL_MIN));

		return success("法币页面信息", map);

	}

	public Result legal(String token, BigDecimal numbers) {
		Member member = (Member) redisUtil.get(token);
		if (!"1".equals(member.getRealStatus())) {
			return Result.fail(ApiStatus.NOT_REAL);
		}
		BigDecimal min = new BigDecimal(F.me().cfg(LEGAL_MIN));
		if (numbers.compareTo(min) < 0)
			return fail(ApiStatus.MIN_WITHDRAW_NUM);
		//价格
		BigDecimal legalUsdt = new BigDecimal(F.me().cfg(LEGAL_USDT));

		//计算usdt数量
		BigDecimal numbersUsdt = numbers.divide(legalUsdt, 4, RoundingMode.DOWN);

		String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("L");
		//生成购买记录
		Bought bought = new Bought();
		bought.setUnit(new BigDecimal(F.me().cfg(LEGAL_USDT)))
				.setPrice(numbers)
				.setNumbers(numbersUsdt)
				.setMemberId(member.getMemberId())
				.setOrderNo(orderNo)
				.setStatus(LegalStatus.N.code)
				.setCreateUser(member.getMemberId());

		boughtService.save(bought);

		//唤起收款台
		CashDto cashDto = new CashDto();
		cashDto.setUrl(idaUrl)
				.setSign(idaKey)
				.setCrmNo(idaAppid)
				.setPickupUrl(pickupUrl)
				.setReceiveUrl(receiveUrl)
				.setOrderCurrency("CNY")
				.setCustomerId(member.getMemberId() + "")
				.setCoin("USDT")
				.setOrderNo(orderNo)
				.setOrderAmount(numbers + "")
				.setAmount(0 + "");

		return success("唤起收银台", IdaUtils.toCashierIndex(cashDto));
	}

	public Result legalList(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		Bought bought = new Bought();
		bought.setMemberId(member.getMemberId()).setDel("N");
		return success(boughtService.page(page, new QueryWrapper<>(bought).orderByDesc(CREATE_TIME)));
	}


	/*
	 *
	 *
	 *  ==============> 二期需求
	 *
	 *
	 *
	 *
	 */

	public Result legalMgrPage(String token) {
		Member member = (Member) redisUtil.get(token);
		Map<String, Object> map = new HashedMap();

		//昵称，为空则需要补填
		map.put("nickName", member.getNickName());
		Deposit deposit = getDeposit(member);
		//押金缴纳情况：2-未开通 1-全部缴纳 0-待补缴
		map.put("deposit", deposit == null ? "2" : deposit.getStatus());

		return success(map);
	}

	public Result nickName(String token, String nickName) {
		Member member = (Member) redisUtil.get(token);
		if ("0".equals(member.getRealStatus())) {
			return Result.fail(ApiStatus.NOT_REAL);
		}
		if ("2".equals(member.getRealStatus())) {
			return Result.fail(ApiStatus.REAL_CHECK);
		}
		if (StringUtils.isBlank(nickName)) {
			return Result.fail(ApiStatus.NOT_NULL_NICK_NAME);
		}
		if (!nickName.equals(member.getNickName())) {
			Member member1 = new Member();
			member1.setNickName(nickName);
			member1.setDel("N");
			int count = memberService.count(new QueryWrapper<>(member1));
			if (count > 0) {
				return Result.fail(ApiStatus.NOT_EXIT_NICK_NAME);
			}
		}
		member.setNickName(nickName)
				.setUpdateUser(SYS_PLATFORM);
		redisUtil.set(token, member, Long.parseLong(F.me().cfg(TOKEN_EXPIRE)));
		redisUtil.set(SINGLE_ACCOUNT + member.getAccount(), token, Long.parseLong(F.me().cfg(TOKEN_EXPIRE)));
		member.setUpdateUser(member.getMemberId());
		member.setUpdateTime(new Date());
		memberService.updateById(member);


		return Result.success();
	}

	public Result depositPage(String token) {
		Member member = (Member) redisUtil.get(token);

		if (isExistDeposit(member))
			return fail(ApiStatus.BAD_REQUEST);

		Map<String, Object> map = new HashedMap();

//        map.put("MGE", F.me().cfg("OTC_BAIL_MGE"));
		map.put("USDT", F.me().cfg("OTC_BAIL_USDT"));

		return success(map);
	}

	/**
	 * 是否存在缴纳押金记录
	 *
	 * @param member
	 * @return
	 */
	private boolean isExistDeposit(Member member) {
		Deposit query = new Deposit();
		query.setDel("N")
				.setMemberId(member.getMemberId())
		;
		return depositService.getOne(new QueryWrapper<>(query)) != null ? true : false;
	}

	/**
	 * 获取缴纳押金记录 ，为空则不存在
	 *
	 * @param member
	 * @return
	 */
	private Deposit getDeposit(Member member) {
		Deposit query = new Deposit();
		query.setDel("N")
				.setMemberId(member.getMemberId())
		;
		return depositService.getOne(new QueryWrapper<>(query));
	}

	@Transactional(rollbackFor = Exception.class)
	public Result deposit(String token, String type, String payPwd) {
		String prefix = "OTC_BAIL_";

		Member member = (Member) redisUtil.get(token);
		if (member.getNickName() == null)
			return fail(ApiStatus.BAD_REQUEST);
		if (isExistDeposit(member))
			return fail(ApiStatus.BAD_REQUEST);
		if (member.getPayPassword() == null)
			return fail(ApiStatus.PAY_PWD_EMPTY);

		if (!isTruePayPwd(member, payPwd))
			return fail(ApiStatus.ERROR_PAY_PWD);

		BigDecimal price = new BigDecimal(F.me().cfg(prefix + type));

		Legal legal = F.me().getLegal(member.getMemberId(), type);

		if (legal.getUsedPrice().compareTo(price) < 0)
			return fail(ApiStatus.WALLET_LESS);


		//押金记录
		Deposit deposit = new Deposit();
		deposit.setMemberId(member.getMemberId())
				.setAccount(member.getAccount())
				.setNickName(member.getNickName())
				.setCoin(type)
				.setNumber(price)
				.setStatus("1") //全额缴纳-1   待补缴-0
				.setCreateUser(SYS_PLATFORM);

		depositService.save(deposit);


		BigDecimal beforeUsed = legal.getUsedPrice();
		BigDecimal afterUsed = legal.getUsedPrice().subtract(price);

		BigDecimal beforeLock = legal.getLockedPrice();
		BigDecimal afterLock = legal.getLockedPrice().add(price);

		legal.setUsedPrice(afterUsed)
				.setLockedPrice(afterLock)
				.setUpdateUser(SYS_PLATFORM);
		if (!legalService.updateById(legal))
			throw new UpdateDataException(100);
		//可用流水
		F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.DEPOSIT,
				price, type, price, type, BigDecimal.ZERO, type,
				ItemCode.USED, type, null, null,
				beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
		//冻结流水
		F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.DEPOSIT,
				price, type, price, type, BigDecimal.ZERO, type,
				ItemCode.LOCKED, type, null, null,
				beforeLock, afterLock, member.getMemberId(), member.getMemberId());


		return success(ApiStatus.OK);
	}

	/**
	 * 检查验证码是否正确
	 *
	 * @param account 账号
	 * @param code    验证码
	 * @return Result
	 */
	public Result checkSmsCode(String account, String code, String token) {
		Member member = (Member) redisUtil.get(token);
		Map<String, Object> map = new HashMap<>();
		map.put("isTrue", "N");
		if (!account.contains("@")) {
			if (F.me().cfg(SMSSENDOPEN).equals("N")) {
				map.put("isTrue", "Y");
			}
		} else {
			if (F.me().cfg(EMAILSENDOPEN).equals("N")) {
				map.put("isTrue", "Y");
			}
		}


		if (StringUtils.isNotBlank(member.getAccount()) && redisUtil.get(SMS + member.getAccount()) != null
				&& StringUtils.isNotBlank(code)) {
			String oldCode = (String) redisUtil.get(SMS + member.getAccount());
			if (oldCode.equals(code)) {
				map.put("isTrue", "Y");
				redisUtil.del(SMS + account);
			}
		}
		return Result.success(map);
	}

	@Transactional
	public void updateWalletMoney(Trade trade) {
		//金额为最小单位，需要转换,包括amount和fee字段
		BigDecimal amount = trade.getAmount().divide(BigDecimal.TEN.pow(trade.getDecimals()), 8, RoundingMode.DOWN);
		BigDecimal fee = trade.getFee().divide(BigDecimal.TEN.pow(trade.getDecimals()), 8, RoundingMode.DOWN);
		log.info("amount={},fee={}", amount.toPlainString(), fee.toPlainString());
		MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
		memberRechargeAddress.setAddress(trade.getAddress());
		memberRechargeAddress = this.memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
		if (memberRechargeAddress != null) {
			Recharge recharge = new Recharge();
			recharge.setTxHash(trade.getTxId());
			recharge = rechargeService.getOne(new QueryWrapper<>(recharge));
			if (recharge != null) {
				log.info("txHash已存在");
				return;
			}
			recharge = new Recharge();
			recharge.setOrderNo(trade.getTradeId());
			recharge.setPrice(amount);
			recharge.setActualPrice(amount);
			recharge.setGas(fee);
			recharge.setFromAddress(trade.getAddress());
			recharge.setTxHash(trade.getTxId());
			recharge.setType(memberRechargeAddress.getCoin());
			recharge.setStatus("Y");
			recharge.setMemberId(memberRechargeAddress.getMemberId());
			recharge.setDel("N");
			recharge.setRemark(trade.getMemo());
			recharge.setCreateTime(new Date());
			recharge.setCreateUser(1L);
			rechargeService.save(recharge);
			if ("USDT-ERC20".equals(memberRechargeAddress.getCoin())
					|| "USDT-TRC20".equals(memberRechargeAddress.getCoin())
					|| "USDT-OMNI".equals(memberRechargeAddress.getCoin())
					|| "USDT".equals(memberRechargeAddress.getCoin())) {
				memberRechargeAddress.setCoin("USDT");
			}
			Wallet wallet = F.me().getWallet(memberRechargeAddress.getMemberId(), memberRechargeAddress.getCoin());
			BigDecimal beforePrice = wallet.getUsedPrice();
			/**
			 * 给用户账户加钱
			 */
			wallet.setUsedPrice(wallet.getUsedPrice().add(amount))
					.setUpdateUser(wallet.getMemberId());
			walletService.updateById(wallet);
			//流水记录
			F.me().saveCashflow(memberRechargeAddress.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.RECHARGE
					, amount, memberRechargeAddress.getCoin(), amount, memberRechargeAddress.getCoin(), fee, memberRechargeAddress.getCoin()
					, ItemCode.USED, memberRechargeAddress.getCoin(), null, null,
					beforePrice, wallet.getUsedPrice(),
					SYS_PLATFORM, memberRechargeAddress.getMemberId()
			);
		}
	}

	@Transactional
	public void updateWithdraw(Trade trade) {
		Withdraw withdraw = new Withdraw();
		withdraw.setOrderNo(trade.getBusinessId());
		withdraw = withdrawService.getOne(new QueryWrapper<>(withdraw));
		if (withdraw != null && WithdrawStatusEnum.COIN.getCode().equals(withdraw.getStatus())) {
			if (trade.getStatus() == 1) {
				log.info("审核通过，转账中");
				//TODO: 提币交易已发出，理提币订单状态，扣除提币资金
				withdraw.setTxHash(trade.getTxId());
				withdraw.setStatus(ProConst.WithdrawStatusEnum.PASS.getCode());
				withdraw.setGases(trade.getFee()); //暂存旷工费
				withdraw.setUpdateUser(SYS_PLATFORM);
				withdrawService.updateById(withdraw);
			} else if (trade.getStatus() == 2) {
				log.info("审核不通过");
				//TODO: 处理提币订单状态，订单号为 businessId
				withdraw.setStatus(WithdrawStatusEnum.REJECT.getCode());
				withdraw.setUpdateUser(SYS_PLATFORM);
				withdrawService.updateById(withdraw);
				Wallet wallet = F.me().getWallet(withdraw.getMemberId(), withdraw.getType());
				BigDecimal usedBefore = wallet.getUsedPrice();
				BigDecimal usedAfter = wallet.getUsedPrice().add(withdraw.getPrice());

				wallet.setUsedPrice(usedAfter);
				wallet.setUpdateTime(new Date());
				wallet.setUpdateUser(1l);
				boolean rows = walletService.updateById(wallet);
				if (!rows)
					throw new UpdateDataException(100);

				F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.WITHDRAW_REJECT,
						withdraw.getPrice(), withdraw.getType(), withdraw.getPrice(), withdraw.getType(), BigDecimal.ZERO, withdraw.getType(),
						ProConst.ItemCode.USED, withdraw.getType(), null, null,
						usedBefore, usedAfter
						, wallet.getMemberId(), wallet.getMemberId());

			}
		}

	}

	public Result phoneCode() {
		PhoneCode phoneCode = new PhoneCode();
		phoneCode.setDel("N");
		List<PhoneCode> list = phoneCodeService.list(new QueryWrapper<>(phoneCode));
		List<Map<String, Object>> phoneCodeList = new ArrayList<>(list.size());
		for (PhoneCode phoneCode1 : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("code", phoneCode1.getCode());
			map.put("area", phoneCode1.getArea());
			map.put("country", phoneCode1.getCountry());
			map.put("type", phoneCode1.getType());
			map.put("label", " " + phoneCode1.getCountry() + "  +" + phoneCode1.getCode());
			phoneCodeList.add(map);
		}
		return Result.success(phoneCodeList);
	}

	public Result updateMemberAddress() {
		Member member = new Member();
		member.setDel("N");
		List<Member> list = this.memberService.list(new QueryWrapper<>(member));
		for (Member member1 : list) {
			updateMemberChainAddress(member1);
		}
//        Member member = this.memberService.getById(1482);
//        updateMemberChainAddress(member);
		return Result.success("更新完成", 200);
	}

	private void updateMemberChainAddress(Member member) {
		boolean open = false;
		if (StrUtil.equals(F.me().cfg(CHAIN_OPEN), "Y")) {
			open = true;
		}
		if (open) {
			Spot spot = new Spot();
			spot.setDel("N");
			spot.setStatus("Y");
			List<Spot> spotList = spotService.list(new QueryWrapper<>(spot));
			for (Spot spot1 : spotList) {
				if (StringUtils.isNotBlank(spot1.getCode())) {
					String[] symbols = spot1.getSymbol().split("/");
					MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
					memberRechargeAddress.setMemberId(member.getMemberId());
					memberRechargeAddress.setCoin(symbols[0]);
					memberRechargeAddress = this.memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
					if (memberRechargeAddress != null) {
						if (StringUtils.isNotBlank(spot1.getCode()) && (!"EOS".equals(spot1.getSymbol()) || !"XRP".equals(spot1.getSymbol()))) {
							Address address = biPayService.createCoinAddress(spot1.getCode(), "", spot1.getWalletCode());
							System.out.println(JSONObject.toJSON(address));
							if (address != null) {
								memberRechargeAddress.setAddress(address.getAddress());
								memberRechargeAddress.setUpdateTime(new Date());
								this.memberRechargeAddressService.updateById(memberRechargeAddress);
								log.info("更新用户【" + memberRechargeAddress.getMemberId() + "】钱包币种" + memberRechargeAddress.getCoin() + ",地址：" + memberRechargeAddress.getAddress());
							}
						}
					}
				}
			}
		}
	}

	public Result whiteBook(String language) {
		WhiteBook whiteBook = new WhiteBook();
		whiteBook.setDel(N);
		whiteBook.setLanguage(language);
		return success(whiteBookService.list(new QueryWrapper<>(whiteBook).orderByDesc(CREATE_TIME)).get(0));
	}

	public Result whiteBookDetail(Long id) {
		WhiteBook whiteBook = this.whiteBookService.getById(id);
		return Result.success(whiteBook);
	}

	public HashMap timeCount(String symbol) {
		long l = Calendar.getInstance().getTimeInMillis();

		List<ContractOption> lists = starting(symbol);
		Long c = 0L;
		Long createLong = 0L;
		if (lists != null && lists.size() > 0) {
			ContractOption contractOption = lists.get(0);
			createLong = contractOption.getCreateTime().getTime();

			c = (l - createLong) / 1000;//过去

		}
		HashMap map = new HashMap<String, Long>(3);

		map.put("num", c);
		map.put("systemTime", l);
		map.put("createLongTime", createLong);

		return map;
	}

	public Result history(String symbol, int count) {
		Page page = new Page();
		page.setSize(count);
		ContractOption contractOption = new ContractOption();
		contractOption.setSymbol(symbol);
		QueryWrapper wapper = new QueryWrapper<>(contractOption);
		wapper.orderByDesc("option_no");
		wapper.gt("result", "0");
		//Page optionList =  optionService.selectByCount(symbol,count);
		return Result.success(optionService.page(page, wapper));
	}

	// 获取正在投注中的合约
	public List<ContractOption> starting(String symbol) {
		ContractOption contractOption = new ContractOption();
		contractOption.setSymbol(symbol);
		contractOption.setStatus(ContractOptionStatus.STARTING.getCode());
		List<ContractOption> optionList = optionService.list(new QueryWrapper<>(contractOption));
		return optionList;
	}

	// 获取正在开奖中的合约
	public Result opening(String symbol) {
		ContractOption contractOption = new ContractOption();
		contractOption.setSymbol(symbol);
		contractOption.setStatus(ContractOptionStatus.OPENING.getCode());
		List<ContractOption> optionList = optionService.list(new QueryWrapper<>(contractOption));
		return Result.success(optionList);
	}

	/**
	 * 查询现货交易对列表
	 */

	public Result coinlist() {
		ContractOptionCoin contractOptionCoin = new ContractOptionCoin();
		//contractOptionCoin.setEnable(1);
		contractOptionCoin.setVisible(1);
		List<ContractOptionCoin> list = optionCoinService.list(new QueryWrapper<>(contractOptionCoin).orderByAsc("sort"));
		Object o = redisUtil.get(CNY_USDT);
		BigDecimal cnyUsdt = o != null ? new BigDecimal(o.toString()) : new BigDecimal("0.14492753");
		for (ContractOptionCoin optionCoin : list) {
			String symbol = optionCoin.getSymbol();
			Kline kline = commonService.detailTicket(symbol, cnyUsdt, TF_TICKET, KINE, optionCoin.getCoinScale());
			optionCoin.setRose(kline.getRose());
		}
		return Result.success(list);
	}

	/**
	 * 获取结算时间-收益比率List
	 */
	public List secondsYieldList(String symbol) {
		ContractOptionCoin contractOptionCoin = new ContractOptionCoin();
		contractOptionCoin.setSymbol(symbol);
		ContractOptionCoin coinInfo = optionCoinService.getOne(new QueryWrapper<>(contractOptionCoin));
		String[] secondsYieldList = coinInfo.getSecondsYield().split(",");
		List resultList = new ArrayList();
		for (String secondsYield : secondsYieldList) {
			Map secondsYieldMap = new HashMap<>();
			secondsYieldMap.put("seconds",Integer.parseInt(secondsYield.split("/")[0]));
			secondsYieldMap.put("winRatio",Double.parseDouble(secondsYield.split("/")[1]));
			secondsYieldMap.put("loseRatio",Double.parseDouble(secondsYield.split("/")[2]));
			resultList.add(secondsYieldMap);
		}
		return resultList;
	}

	// 获取期权合约交易对信息
	public Result coinInfo(String symbol) {
		ContractOptionCoin contractOptionCoin = new ContractOptionCoin();
		contractOptionCoin.setSymbol(symbol);
		ContractOptionCoin coinInfo = optionCoinService.getOne(new QueryWrapper<>(contractOptionCoin));
		return Result.success(coinInfo);
	}

	/**
	 * 获取当前币种指定期数ID的参与记录
	 *
	 * @param
	 * @param symbol
	 * @param optionId
	 * @return
	 */

	public Result ordercurrent(String token, String symbol, /* 交易对符号*/
							   Long optionId /* 参与对象*/) {
		Member member = (Member) redisUtil.get(token);
		ContractOptionOrder contractOptionOrder = new ContractOptionOrder();
		contractOptionOrder.setSymbol(symbol);
		contractOptionOrder.setOptionNo(optionId.intValue());
		contractOptionOrder.setMemberId(member.getMemberId());
		ContractOptionOrder orderList = optionOrderService.getOne(new QueryWrapper<>(contractOptionOrder));
		return Result.success(orderList);
	}

	public Result orderhistory(String token,
							   String symbol,
							   Integer status,
							   Page<ContractOptionOrder> page
	) {
		Member member = (Member) redisUtil.get(token);
		ContractOptionOrder contractOptionOrder = new ContractOptionOrder();
		contractOptionOrder.setMemberId(member.getMemberId());
		contractOptionOrder.setSymbol(symbol);
		contractOptionOrder.setStatus(status);
		IPage<ContractOptionOrder> orderPage = optionOrderService.page(page, new QueryWrapper<>(contractOptionOrder).orderByDesc(CREATE_TIME));
		//获取记录中所有的合约id
		Set<Long> optionIds = orderPage.getRecords().stream().map(ContractOptionOrder::getOptionId).collect(Collectors.toSet());

		//获取记录相关联的所有合约
		if (optionIds.size() > 0) {
			List<ContractOption> options = (List<ContractOption>) optionService.listByIds(optionIds);
			Map<Long, ContractOption> optionMap = options.stream().collect(Collectors.toMap(ContractOption::getId, x -> x));
			//给记录添加开仓价和平仓价
			orderPage.getRecords().forEach(order -> {
				ContractOption option = optionMap.get(order.getOptionId());
				order.setOpenPrice(option.getOpenPrice().setScale(3, RoundingMode.HALF_DOWN));
				order.setClosePrice(option.getClosePrice().setScale(3, RoundingMode.HALF_DOWN));
			});
		}
		return Result.success(orderPage);
	}

	public Result orderAdd(String token, String symbol, Integer direction, Long optionId, BigDecimal amount) {


		Assert.notNull(symbol, "请携带币种");
		Assert.notNull(direction, "请携带投注方向");
		Assert.notNull(optionId, "请携带期数");
		Assert.notNull(amount, "请携带金额");

		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		// 用户是否被禁止交易
		//if(member.getTransactionStatus().equals(BooleanEnum.IS_FALSE)){
		//    return MessageResult.error(500,msService.getMessage("CANNOT_TRADE"));
		//}
		ContractOptionCoin contractOptionCoin1 = new ContractOptionCoin();
		contractOptionCoin1.setSymbol(symbol);
		ContractOptionCoin contractOptionCoin = optionCoinService.getOne(new QueryWrapper<>(contractOptionCoin1));
		Assert.notNull(contractOptionCoin, "币种不存在");
		// 预测方向是否在看涨和看跌
		if (direction != ContractOptionOrderDirection.BUY.getCode() && direction != ContractOptionOrderDirection.SELL.getCode()) {
			return Result.fail(ApiStatus.LOOK_UP_OR_DOWN);
		}
		if (contractOptionCoin.getEnable() == 0) {
			return Result.fail(ApiStatus.OPTION_DOWN_NOT_CARE);
		}

		if (direction == ContractOptionOrderDirection.SELL.getCode() && contractOptionCoin.getEnableSell() == 0) {
			return Result.fail(ApiStatus.THAT_OPTION_EN_END);
		}
		if (direction == ContractOptionOrderDirection.BUY.getCode() && contractOptionCoin.getEnableBuy() == 0) {
			return Result.fail(ApiStatus.THAT_OPTION_EN_END);
		}
		// 期权合约是否存在
		ContractOption contractOption1 = new ContractOption();
		contractOption1.setSymbol(symbol);
		contractOption1.setOptionNo(optionId.intValue());
		ContractOption contractOption = optionService.getOne(new QueryWrapper<>(contractOption1));
		Assert.notNull(contractOption, "CONTRACT_PERIOD_DOES_NOT_EXIST");
		if (contractOption.getStatus() != ContractOptionStatus.STARTING.getCode()) {
			return Result.fail(ApiStatus.THAT_COIN_DOWN);
		}
		if (!contractOption.getSymbol().equals(symbol)) {
			return Result.fail(ApiStatus.COIN_NOT_MATCHING);
		}

		// 投注金额是否超出范围
		String[] amountArr = contractOptionCoin.getAmount().split(",");
		BigDecimal amountStart = BigDecimal.valueOf(Long.valueOf(amountArr[0]));
		BigDecimal amountEnd = BigDecimal.valueOf(Long.valueOf(amountArr[amountArr.length - 1]));
		if (amount.compareTo(amountStart) < 0 || amount.compareTo(amountEnd) > 0) {
			return Result.fail(ApiStatus.BET_AMOUNT_OUT_OF_RANGE);
		}

		// 是否已参与过
		ContractOptionOrder contractOptionOrder = new ContractOptionOrder();
		contractOptionOrder.setMemberId(member.getMemberId());
		contractOptionOrder.setSymbol(symbol);
		contractOptionOrder.setOptionNo(optionId.intValue());
		List<ContractOptionOrder> contractOptionOrderList = optionOrderService.list(new QueryWrapper<>(contractOptionOrder));
		if (contractOptionOrderList != null && contractOptionOrderList.size() > 0) {
			return Result.fail(ApiStatus.OPTION_PARTICIPATED);
		}

		Wallet walletQ = new Wallet();
		walletQ.setMemberId(member.getMemberId());
		//walletQ.setType("USDT");
		//钱包账户
		FinOption finOption = F.me().getFinOption(member.getMemberId(), "USDT");
//        List<Wallet> walletList = walletService.list(new QueryWrapper<>(walletQ));
		// 需要 投注额 + 手续费
		BigDecimal totalAmount = amount.add(contractOptionCoin.getFeePercent());
		if (totalAmount.compareTo(finOption.getUsedPrice()) > 0) {
			return Result.fail(ApiStatus.WALLET_LESS);
		}

		// 新建订单
		ContractOptionOrder orderObj = new ContractOptionOrder();
		orderObj.setBaseSymbol(contractOptionCoin.getBaseSymbol());
		orderObj.setBetAmount(amount);
		orderObj.setCoinSymbol(contractOptionCoin.getCoinSymbol());
		orderObj.setOptionId(contractOption.getId());
		orderObj.setDirection(direction);
		orderObj.setFee(contractOptionCoin.getFeePercent());
		orderObj.setWinFee(contractOptionCoin.getWinFeePercent().divide(new BigDecimal(1000)).multiply(amount));
		orderObj.setMemberId(member.getMemberId());
		orderObj.setResult(ContractOptionOrderResult.WAIT.getCode());
		orderObj.setRewardAmount(BigDecimal.ZERO);
		orderObj.setOrRecord("N");
		orderObj.setRecordNum(BigDecimal.ZERO);
		orderObj.setOrTowRecord("N");
		orderObj.setRecordTowNum(BigDecimal.ZERO);
		orderObj.setStatus(ContractOptionOrderStatus.OPEN.getCode());
		orderObj.setSymbol(contractOptionCoin.getSymbol());
		orderObj.setCreateTime(Calendar.getInstance().getTime());
		orderObj.setOptionNo(contractOption.getOptionNo());
		optionOrderService.save(orderObj);

		// 锁定资产
		//memberWalletService.freezeBalance(memberWallet, amount.add(amount.multiply(contractOptionCoin.getFeePercent())));
		finOption.setUsedPrice(finOption.getUsedPrice().subtract(totalAmount));
		finOption.setLockedPrice(finOption.getLockedPrice().add(totalAmount));
		finOptionService.updateById(finOption);
//        F.me().saveCashflow(orderObj.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, direction==0? CashFlowTypeEnum.OPTIONKZ:CashFlowTypeEnum.OPTIONKD,
//                totalAmount, orderObj.getBaseSymbol(),totalAmount, orderObj.getBaseSymbol(),
//                BigDecimal.ZERO, orderObj.getBaseSymbol(),
//                ItemCode.USED, orderObj.getSymbol(), null, "期权交易",
//                finOption.getUsedPrice().add(totalAmount), finOption.getUsedPrice(),
//                orderObj.getMemberId(), orderObj.getMemberId());
		// 总单投注额增加
		if (direction == ContractOptionOrderDirection.BUY.getCode()) {
			contractOption.setTotalBuy(contractOption.getTotalBuy().add(amount));
			contractOption.setTotalBuyCount(contractOption.getTotalBuyCount() + 1);
		} else if (direction == ContractOptionOrderDirection.SELL.getCode()) {
			contractOption.setTotalSell(contractOption.getTotalSell().add(amount));
			contractOption.setTotalSellCount(contractOption.getTotalSellCount() + 1);
		}
		optionService.updateById(contractOption);
		return Result.success("下注成功");


	}

	@Transactional
	public Result createContactAndOrder(String token, String symbol, String openPrice, String amount, Integer direction, Integer seconds, Double winRatio, Double loseRatio) {
		Assert.notNull(symbol, "请携带币种");
		Assert.notNull(direction, "请携带投注方向");
		Assert.notNull(amount, "请携带金额");
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");

        // 检测币种是否存在
		ContractOptionCoin contractOptionCoin = contractOptionCoinService.getOne(new LambdaQueryWrapper<ContractOptionCoin>()
				.eq(ContractOptionCoin::getSymbol, symbol));
		Assert.notNull(contractOptionCoin, "币种不存在");

        // 预测方向是否在看涨和看跌
        if (direction != ContractOptionOrderDirection.BUY.getCode() && direction != ContractOptionOrderDirection.SELL.getCode()) {
            return Result.fail(ApiStatus.LOOK_UP_OR_DOWN);
        }

        // 检测该期权状态
        if (contractOptionCoin.getEnable() == 0) {
            return Result.fail(ApiStatus.OPTION_DOWN_NOT_CARE);
        }

		//判断钱包账户余额
		FinOption finOption = F.me().getFinOption(member.getMemberId(), "USDT");
		// 需要 投注额 + 手续费
		BigDecimal totalAmount = new BigDecimal(amount).add(contractOptionCoin.getFeePercent());
		if (totalAmount.compareTo(finOption.getUsedPrice()) > 0) {
			return Result.fail(ApiStatus.WALLET_LESS);
		}


		// 创建新的一期期权合约
		log.info("币种：{}，创建新的一期", symbol);
		ContractOption contractOption = new ContractOption();
		Date date = new Date();
		contractOption.setOptionNo((int) (date.getTime() / 1000));
		contractOption.setSymbol(symbol);
		contractOption.setOpenPrice(new BigDecimal(openPrice));
		contractOption.setOpenTime(date.getTime());
		contractOption.setStatus(ContractOptionStatus.OPENING.getCode()); // 开奖中
		contractOption.setResult(0); // 待开奖
		DateTime dateTime = DateUtil.offsetSecond(date, seconds);
		contractOption.setCloseTime(dateTime.getTime()); // 收盘时间
		contractOption.setCreateTime(new Date()); //创建时间
		if (direction == ContractOptionOrderDirection.BUY.getCode()) {
			contractOption.setTotalBuy(new BigDecimal(amount)); // 买涨奖池总金额
			contractOption.setTotalBuyCount(1); // 买涨人数
		} else if (direction == ContractOptionOrderDirection.SELL.getCode()) {
			contractOption.setTotalSell(new BigDecimal(amount)); // 买跌奖池总金额
			contractOption.setTotalSellCount(1); // 买跌人数
		}
//		System.out.println("新合约:" + contractOption.toString());
		boolean save = contractOptionService.save(contractOption);
		if (!save) {
			return Result.fail(500,"创建合约失败");
		}
		log.info("===新的一期创建完毕===");
		// 创建新建订单
		ContractOptionOrder contractOptionOrder = new ContractOptionOrder();
		contractOptionOrder.setBaseSymbol(contractOptionCoin.getBaseSymbol());
		contractOptionOrder.setBetAmount(new BigDecimal(amount));
		contractOptionOrder.setCoinSymbol(contractOptionCoin.getCoinSymbol());
		contractOptionOrder.setOptionId(contractOption.getId());
		contractOptionOrder.setDirection(direction);
		contractOptionOrder.setFee(contractOptionCoin.getFeePercent());
		contractOptionOrder.setWinFee(contractOptionCoin.getWinFeePercent().divide(new BigDecimal(1000)).multiply(new BigDecimal(amount)));
		contractOptionOrder.setMemberId(member.getMemberId());
		contractOptionOrder.setResult(ContractOptionOrderResult.WAIT.getCode());
		contractOptionOrder.setRewardAmount(BigDecimal.ZERO);
		contractOptionOrder.setOrRecord("N");
		contractOptionOrder.setRecordNum(BigDecimal.ZERO);
		contractOptionOrder.setOrTowRecord("N");
		contractOptionOrder.setRecordTowNum(BigDecimal.ZERO);
		contractOptionOrder.setStatus(ContractOptionOrderStatus.OPEN.getCode());
		contractOptionOrder.setSymbol(contractOptionCoin.getSymbol());
		contractOptionOrder.setCreateTime(Calendar.getInstance().getTime());
		contractOptionOrder.setOptionNo(contractOption.getOptionNo());
		contractOptionOrder.setSecondsYield(seconds + "/" + winRatio + "/" + loseRatio);
//		System.out.println("新订单:"+contractOptionOrder.toString());
		boolean save1 = optionOrderService.save(contractOptionOrder);
		if (!save1) {
			return Result.fail(500,"创建订单失败!!");
		}
		// 锁定资产
		finOption.setUsedPrice(finOption.getUsedPrice().subtract(totalAmount));
		finOption.setLockedPrice(finOption.getLockedPrice().add(totalAmount));
		boolean save2 = finOptionService.updateById(finOption);
		if (!save2) {
			return Result.fail(500,"锁定资产失败");
		}
		// 预计赢的收益((未抽水)
		BigDecimal reward = contractOptionOrder.getBetAmount().multiply(new BigDecimal(contractOptionOrder.getSecondsYield().split("/")[1])).setScale(5, RoundingMode.DOWN);
		// 预计输
		BigDecimal losePrice = contractOptionOrder.getBetAmount().multiply(new BigDecimal(contractOptionOrder.getSecondsYield().split("/")[2]));
		Map resultmap = new HashMap<>();
		resultmap.put("contractOptionOrderId",contractOptionOrder.getId());
		resultmap.put("direction", direction);
		resultmap.put("symbol", symbol);
		resultmap.put("amount", amount);
		resultmap.put("openPrice", openPrice);
		resultmap.put("winRatio", winRatio);
		resultmap.put("loseRatio", loseRatio);
		resultmap.put("seconds", seconds);
		resultmap.put("rewardAmount", reward.subtract(contractOptionOrder.getWinFee()));
		resultmap.put("losePrice",losePrice);
		return Result.success(resultmap);
	}

	public Result optionsContractSettlement(String token, Long contractOptionOrderId, String closePrice) {
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		// 合约订单
		ContractOptionOrder contractOptionOrder = contractOptionOrderService.getOne(new LambdaQueryWrapper<ContractOptionOrder>()
				.eq(ContractOptionOrder::getId, contractOptionOrderId).eq(ContractOptionOrder::getMemberId, member.getMemberId()));
		//合约每一期
		ContractOption contractOption = contractOptionService.getOne(new LambdaQueryWrapper<ContractOption>().eq(ContractOption::getId, contractOptionOrder.getOptionId()));
		contractOption.setClosePrice(new BigDecimal(closePrice));
		contractOption.setStatus(ContractOptionStatus.CLOSED.getCode());
		BigDecimal openPrice = contractOption.getOpenPrice().setScale(4, RoundingMode.DOWN);
		BigDecimal closePrice1 = contractOption.getClosePrice();
		// 计算这一期合约的结果
		if (openPrice.compareTo(closePrice1) == 0) {
			contractOption.setResult(3); // 平
		} else if (openPrice.compareTo(closePrice1) > 0) {
			contractOption.setResult(2); // 跌
		} else {
			contractOption.setResult(1); // 涨
		}
		contractOptionService.updateById(contractOption);
		// 判断合约钱包是否存在
		FinOption finOption = F.me().getFinOption(contractOptionOrder.getMemberId(), contractOptionOrder.getBaseSymbol());
		if (finOption == null) {
			member = F.me().getMember(contractOptionOrder.getMemberId());
			this.genOption(member);
		}
		//
		ContractOptionCoin coin = contractOptionCoinService.getOne(Wrappers.<ContractOptionCoin>lambdaQuery().eq(ContractOptionCoin::getSymbol, contractOptionOrder.getSymbol()));
		if (coin == null) return Result.fail(500,"期权交易对不存在");
		Integer result = contractOption.getResult();
		Boolean ctrlFlag = result == 3 ? null : Objects.equals(result, contractOptionOrder.getDirection() + 1);
		boolean flag = this.optionsContractSettlement(contractOptionOrder, finOption, coin, ctrlFlag);
		if (flag) {
			//Y 需要返佣
			this.optionRecord(coin, contractOptionOrder);
		}
		optionOrderService.updateById(contractOptionOrder);
		finOptionService.updateWallet(finOption);
		contractOptionCoinService.updateById(coin);
		Map resultMap = new HashMap();
		resultMap.put("symbol",contractOptionOrder.getSymbol());
		resultMap.put("amount",contractOptionOrder.getBetAmount());
		resultMap.put("openPrice",contractOption.getOpenPrice());
		resultMap.put("closePrice",contractOption.getClosePrice());
		resultMap.put("direction",contractOptionOrder.getDirection());
		resultMap.put("seconds",contractOptionOrder.getSecondsYield().split("/")[0]);
		resultMap.put("rewardAmount",contractOptionOrder.getRewardAmount());
		return Result.success(resultMap);
	}

	/**
	 * 另一套期权合约结算
	 * @param order 合约订单
	 * @param finOption 合约钱包
	 * @param coin 每一期合约
	 * @param isCtrl 买的方向和本期结果方向相同为胜(true)，反之为负(false)，若平局则为null
	 * @return 是否返佣
	 */
	public boolean optionsContractSettlement(ContractOptionOrder order, FinOption finOption, ContractOptionCoin coin, Boolean isCtrl){
		//是否返佣
		boolean isRebate = false;

		//公共 必要的
		// 退回保证金(此处只退回了投注额，开仓手续费还是冻结状态)
		if (finOption.getLockedPrice().compareTo(order.getBetAmount().add(order.getFee())) >= 0) {
			finOption.setLockedPrice(finOption.getLockedPrice().subtract(order.getBetAmount()));
			finOption.setUsedPrice(finOption.getUsedPrice().add(order.getBetAmount()));
		}

		if (null == isCtrl) {
			//平局 不收取手续费  退回
			F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONTUIHUI,
					order.getBetAmount(), order.getBaseSymbol(), order.getBetAmount(), order.getBaseSymbol(),
					BigDecimal.ZERO, order.getBaseSymbol(),
					ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONTUIHUI.getValue(),
					finOption.getUsedPrice().subtract(order.getBetAmount()), finOption.getUsedPrice(),
					order.getMemberId(), order.getMemberId());
			order.setRewardAmount(BigDecimal.ZERO);
			order.setResult(ContractOptionOrderResult.TIED.getCode());
			order.setWinFee(BigDecimal.ZERO);
			// 退回开仓手续费
			finOption.setLockedPrice(finOption.getLockedPrice().subtract(order.getFee()));
			finOption.setUsedPrice(finOption.getUsedPrice().add(order.getFee()));
			//增加记录
			F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONFEE,
					order.getFee(), order.getBaseSymbol(), order.getFee(), order.getBaseSymbol(),
					BigDecimal.ZERO, order.getBaseSymbol(),
					ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONFEE.getValue(),
					finOption.getUsedPrice(), finOption.getUsedPrice().add(order.getFee()),
					order.getMemberId(), order.getMemberId());
		} else {
			//赢家有手续费以及抽水
			if (isCtrl) {
				// 扣除手续费  具体数额 1，2，3
				if (order.getFee().compareTo(BigDecimal.ZERO) > 0) {
					// 扣除开仓手续费
					finOption.setLockedPrice(finOption.getLockedPrice().subtract(order.getFee()));
					//增加记录
					F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OPTIONFEE,
							order.getFee(), order.getBaseSymbol(), order.getFee(), order.getBaseSymbol(),
							BigDecimal.ZERO, order.getBaseSymbol(),
							ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONFEE.getValue(),
							finOption.getUsedPrice().add(order.getFee()), finOption.getUsedPrice(),
							order.getMemberId(), order.getMemberId());

					// 平台手续费收益
					coin.setTotalProfit(coin.getTotalProfit().add(order.getFee()));
				}
				//用户收益
				BigDecimal reward = order.getBetAmount().multiply(new BigDecimal(order.getSecondsYield().split("/")[1])).setScale(5, RoundingMode.DOWN);
				//平台收益
				coin.setTotalProfit(coin.getTotalProfit().subtract(reward));

				//计算抽水 百分比 千分之
				BigDecimal winFee = order.getWinFee();
				if (coin.getWinFeePercent().compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal usedPrice = finOption.getUsedPrice();
					finOption.setUsedPrice(usedPrice.subtract(winFee));
					//增加资产变更记录
					F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OPTIONCHOUSHUI,
							winFee, order.getBaseSymbol(), winFee, order.getBaseSymbol(),
							winFee, order.getBaseSymbol(),
							ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONCHOUSHUI.getValue(),
							usedPrice, finOption.getUsedPrice(),
							order.getMemberId(), order.getMemberId());
					coin.setTotalProfit(coin.getTotalProfit().add(winFee));
				}
				//赢家
				finOption.setUsedPrice(finOption.getUsedPrice().add(reward));
				F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONY,
						reward, order.getBaseSymbol(), reward, order.getBaseSymbol(),
						winFee, order.getBaseSymbol(),
						ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONY.getValue(),
						finOption.getUsedPrice().subtract(reward), finOption.getUsedPrice(),
						order.getMemberId(), order.getMemberId());

				order.setRewardAmount(reward.subtract(winFee));
				order.setResult(ContractOptionOrderResult.WIN.getCode());
				order.setWinFee(winFee);
				isRebate = true;
			} else {
				//输家 不需要扣取手续费
				BigDecimal losePrice = order.getBetAmount().multiply(new BigDecimal(order.getSecondsYield().split("/")[2]));
				finOption.setUsedPrice(finOption.getUsedPrice().subtract(losePrice));
				F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OPTIONK,
						losePrice, order.getBaseSymbol(), losePrice, order.getBaseSymbol(),
						BigDecimal.ZERO, order.getBaseSymbol(),
						ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONK.getValue(),
						finOption.getUsedPrice().subtract(losePrice), finOption.getUsedPrice(),
						order.getMemberId(), order.getMemberId());
				//平台收益
				//temOption.setTotalPl(temOption.getTotalPl().add(shu));
				coin.setTotalProfit(coin.getTotalProfit().add(losePrice));

				order.setRewardAmount(losePrice.negate());
				order.setResult(ContractOptionOrderResult.LOSE.getCode());
				order.setWinFee(BigDecimal.ZERO);
				// 退回开仓手续费
				finOption.setLockedPrice(finOption.getLockedPrice().subtract(order.getFee()));
				finOption.setUsedPrice(finOption.getUsedPrice().add(order.getFee()));
				//增加记录
				F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONFEE,
						order.getFee(), order.getBaseSymbol(), order.getFee(), order.getBaseSymbol(),
						BigDecimal.ZERO, order.getBaseSymbol(),
						ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONFEE.getValue(),
						finOption.getUsedPrice(), finOption.getUsedPrice().add(order.getFee()),
						order.getMemberId(), order.getMemberId());
			}
		}
		order.setStatus(ContractOptionOrderStatus.CLOSE.getCode());
		return isRebate;
	}

	public Result finPeriodicList() {
		Periodic periodic = new Periodic();
		periodic.setIsDelete("N");
		List<Periodic> list = finPeriodicService.list(new QueryWrapper<>(periodic));
		return Result.success(list);
	}

	public Result finPeriodicMyOrderList(String token) {
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");

		List list = finPeriodicOrderService.selectByList(member.getMemberId());
		Map map = new HashMap();
		Map map1 = finPeriodicOrderService.selectTotal(member.getMemberId());
		map.putAll(map1);
		map.put("list", list);
		return Result.success(map);
	}

	@Transactional
	public Result finPeriodicOrderAdd(String token, Long periodicId, BigDecimal amount) {
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		Periodic p = new Periodic();
		p.setIsDelete("N");
		p.setId(periodicId);
		Periodic periodic = finPeriodicService.getOne(new QueryWrapper<>(p));
		Assert.notNull(periodic, "利率不存在");
		//钱包账户
		Wallet usdt = F.me().getWallet(member.getMemberId(), "USDT");
		if (amount.compareTo(usdt.getUsedPrice()) > 0) {
			return Result.fail(ApiStatus.WALLET_LESS);
		}
		if (amount.compareTo(periodic.getNum()) > 0) {
			return Result.fail(ApiStatus.MAXIMUM_AMOUNT_EXCEEDED);
		}
		if (amount.compareTo(periodic.getMinNum()) < 0) {
			return Result.fail(ApiStatus.MINIMUM_AMOUNT_NOT_REACHED);
		}

		Calendar calendar = Calendar.getInstance();
		//冻结金额
		BigDecimal usedPrice = usdt.getUsedPrice();
		BigDecimal lockedPrice = usdt.getLockedPrice();
		usdt.setUsedPrice(usedPrice.subtract(amount));
		usdt.setLockedPrice(lockedPrice.add(amount));
		walletService.updateById(usdt);
		//新建订单
		PeriodicOrder periodicOrder = new PeriodicOrder();
		periodicOrder.setPeriodicId(periodicId);
		periodicOrder.setMemberId(member.getMemberId());
		periodicOrder.setIsDelete("N");
		periodicOrder.setResult("1");
		periodicOrder.setRate(periodic.getRate());
		periodicOrder.setWinN(periodic.getRate().multiply(amount.divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP)));
		periodicOrder.setOrderNo("DD" + UUIDUtil.getUUID());
		periodicOrder.setSymbol("USDT");
		periodicOrder.setAmount(amount);
		periodicOrder.setRemark("未结算");
		periodicOrder.setCreateTime(calendar.getTime());
		switch (periodic.getType()) {
			case "1": //周
				calendar.add(Calendar.DAY_OF_YEAR, 7);
				break;
			case "2"://月
				calendar.add(Calendar.MONTH, 1);
				break;
			case "3"://季度
				calendar.add(Calendar.MONTH, 3);
				break;
			case "4"://年
				calendar.add(Calendar.YEAR, 1);
				break;
		}
		periodicOrder.setEndTime(calendar.getTime());
		periodicOrder.setCreateUser(member.getMemberId());
		finPeriodicOrderService.save(periodicOrder);
		BigDecimal addManey = periodicOrder.getAmount();
		F.me().saveCashflow(periodicOrder.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.SAVECOINDO,
				addManey, periodicOrder.getSymbol(), addManey, periodicOrder.getSymbol(),
				BigDecimal.ZERO, periodicOrder.getSymbol(),
				ProConst.ItemCode.USED, periodicOrder.getSymbol(), null, ProConst.CashFlowTypeEnum.SAVECOINDO.getValue(),
				usdt.getUsedPrice().add(addManey), usdt.getUsedPrice(),
				periodicOrder.getMemberId(), periodicOrder.getMemberId());
		return Result.success("存入成功");
	}

	public Result teManagementLogAdd(String token, Long teManagementId, BigDecimal amount) {
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		TeManagement teManagement = teManagementService.getById(teManagementId);
		Assert.notNull(teManagement, "认购币种不存在");
		String coin = teManagement.getCurrency();
		TeManagementLog teManagementLogQ = new TeManagementLog();

		teManagementLogQ.setInstrument(coin);
		teManagementLogQ.setStatus("0");
		List<TeManagementLog> list = teManagementLogService.list(new QueryWrapper<>(teManagementLogQ));
		BigDecimal sums = BigDecimal.ZERO;
		if (ToolUtil.isNotEmpty(list)) {
			for (TeManagementLog teManagementLog : list) {
				sums = sums.add(new BigDecimal(teManagementLog.getSum()));
			}
		}
		//钱包账户
		Wallet usdt = F.me().getWallet(member.getMemberId(), teManagement.getCurrency());
		if (amount.compareTo(usdt.getUsedPrice()) > 0) {
			return Result.fail(ApiStatus.WALLET_LESS);
		}
		//issuanceCount
		BigDecimal co = new BigDecimal(teManagement.getIssuanceCount());
		if (co.compareTo(sums) < 0) {
			return Result.fail(ApiStatus.EXCEEDING_THE_MAXIMUM_SUBSCRIPTION_AMOUNT);
		}
		if (amount.compareTo(teManagement.getMinPrice()) < 0) {
			return Result.fail(ApiStatus.THE_MINIMUM_SUBSCRIPTION_AMOUNT_IS_NOT_REACHED);
		}
		//冻结金额
		BigDecimal usedPrice = usdt.getUsedPrice();
		BigDecimal lockedPrice = usdt.getLockedPrice();
		usdt.setUsedPrice(usedPrice.subtract(amount));
		usdt.setLockedPrice(lockedPrice.add(amount));
		walletService.updateById(usdt);

		TeManagementLog teManagementLog = new TeManagementLog();
		teManagementLog.setCreatetime(new Date());
		teManagementLog.setUid(member.getMemberId());
		teManagementLog.setUsername(member.getAccount());
		teManagementLog.setInstrument(teManagement.getTitle());
		teManagementLog.setSum(amount.toString());
		teManagementLog.setEarningsSum(amount.multiply(teManagement.getOddsWinning().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP)).toString()); //收益数量
		teManagementLog.setManagementCurrency(teManagement.getCurrency());
//        teManagementLog.setManagementSum(amount.toString());
		teManagementLog.setManagementSum(teManagement.getIssuanceCount().toString());
		teManagementLog.setSalesmanId(teManagement.getId());
		teManagementLog.setStatus("0");
		teManagementLog.setPeriodTime(teManagement.getEndTime());
		//增加资产记录
		F.me().saveCashflow(teManagementLog.getUid(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.TE_MANAGEMENT_COIN_DO,
				amount, coin, amount, coin,
				BigDecimal.ZERO, coin,
				ItemCode.USED, coin, null, CashFlowTypeEnum.TE_MANAGEMENT_COIN_DO.getValue(),
				usdt.getUsedPrice().subtract(amount), usdt.getUsedPrice(),
				teManagementLog.getUid(), teManagementLog.getUid());

		teManagementLog.setImg(teManagement.getImg());

		teManagementLogService.save(teManagementLog);

		return Result.success("认购成功");

	}

	public Result teManagementLogList(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		TeManagementLog teManagementLogQ = new TeManagementLog();
		teManagementLogQ.setUid(member.getMemberId());
		teManagementLogQ.setStatus("0");
		//page.setSize(3L);
		//List<TeManagementLog> list = teManagementLogService.list(new QueryWrapper<>(teManagementLogQ));
		return Result.success(teManagementLogService.page(page, new QueryWrapper<>(teManagementLogQ)));
	}

	public Result teManagementLogList1(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		TeManagementLog teManagementLogQ = new TeManagementLog();
		teManagementLogQ.setUid(member.getMemberId());
		teManagementLogQ.setStatus("1");
		return Result.success(teManagementLogService.page(page, new QueryWrapper<>(teManagementLogQ)));
	}

	public Result teManagementReleaseList(String token) {
		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		TeManagementRelease tmr = new TeManagementRelease();
		tmr.setUid(member.getMemberId());
		List<TeManagementRelease> list = teManagementReleaseService.list(new QueryWrapper<>(tmr));
		return Result.success(list);

	}
	/*黄金交易start*/

	public Result historyGold(String symbol, int count) {
		Page page = new Page();
		page.setSize(count);
		ContractGold contractOption = new ContractGold();
		contractOption.setSymbol(symbol);
		QueryWrapper wapper = new QueryWrapper<>(contractOption);
		wapper.orderByDesc("gold_no");
		wapper.gt("result", "0");
		//Page optionList =  optionService.selectByCount(symbol,count);
		return Result.success(goldService.page(page, wapper));
	}

	// 获取正在投注中的合约
	public Result startingGold(String symbol) {
		ContractGold contractOption = new ContractGold();
		contractOption.setSymbol(symbol);
		contractOption.setStatus(ContractOptionStatus.STARTING.getCode());
		List<ContractGold> optionList = goldService.list(new QueryWrapper<>(contractOption));
		return Result.success(optionList);
	}

	// 获取正在开奖中的合约
	public Result openingGold(String symbol) {
		ContractGold contractGold = new ContractGold();
		contractGold.setSymbol(symbol);
		contractGold.setStatus(ContractOptionStatus.OPENING.getCode());
		List<ContractGold> optionList = goldService.list(new QueryWrapper<>(contractGold));
		return Result.success(optionList);
	}

	/**
	 * 查询现货交易对列表
	 */

	public Result coinlistGold() {
		List<ContractGoldCoin> list = goldCoinService.list();
		return Result.success(list);
	}

	// 获取期权合约交易对信息
	public Result coinInfoGold(String symbol) {
		ContractGoldCoin contractOptionCoin = new ContractGoldCoin();
		contractOptionCoin.setSymbol(symbol);
		ContractGoldCoin coinInfo = goldCoinService.getOne(new QueryWrapper<>(contractOptionCoin));
		return Result.success(coinInfo);
	}

	/**
	 * 获取当前币种指定期数ID的参与记录
	 *
	 * @param
	 * @param symbol
	 * @param optionId
	 * @return
	 */

	public Result ordercurrentGold(String token, String symbol, /* 交易对符号*/
								   Long optionId /* 参与对象*/) {
		Member member = (Member) redisUtil.get(token);
		ContractGoldOrder contractOptionOrder = new ContractGoldOrder();
		contractOptionOrder.setSymbol(symbol);
		contractOptionOrder.setGoldNo(optionId.intValue());
		contractOptionOrder.setMemberId(member.getMemberId());
		ContractGoldOrder orderList = goldOrderService.getOne(new QueryWrapper<>(contractOptionOrder));
		return Result.success(orderList);
	}

	public Result orderhistoryGold(String token,
								   String symbol,
								   Page page
	) {
		Member member = (Member) redisUtil.get(token);
		ContractGoldOrder contractOptionOrder = new ContractGoldOrder();
		contractOptionOrder.setMemberId(member.getMemberId());
		contractOptionOrder.setSymbol(symbol);
		return Result.success(goldOrderService.page(page, new QueryWrapper<>(contractOptionOrder)));
	}

	public Result orderAddGold(String token, String symbol, Integer direction, Long optionId, BigDecimal amount) {


		Assert.notNull(symbol, "请携带币种");
		Assert.notNull(direction, "请携带投注方向");
		Assert.notNull(optionId, "请携带期数");
		Assert.notNull(amount, "请携带金额");

		Member member = (Member) redisUtil.get(token);
		Assert.notNull(member, "用户不存在或未登录");
		// 用户是否被禁止交易
		//if(member.getTransactionStatus().equals(BooleanEnum.IS_FALSE)){
		//    return MessageResult.error(500,msService.getMessage("CANNOT_TRADE"));
		//}
		ContractGoldCoin contractOptionCoin1 = new ContractGoldCoin();
		contractOptionCoin1.setSymbol(symbol);
		ContractGoldCoin contractOptionCoin = goldCoinService.getOne(new QueryWrapper<>(contractOptionCoin1));
		Assert.notNull(contractOptionCoin, "币种不存在");
		// 预测方向是否在看涨和看跌
		if (direction != ContractOptionOrderDirection.BUY.getCode() && direction != ContractOptionOrderDirection.SELL.getCode()) {
			return Result.fail(ApiStatus.LOOK_UP_OR_DOWN);
		}

		if (direction == ContractOptionOrderDirection.SELL.getCode() && contractOptionCoin.getEnableSell() == 0) {
			return Result.fail(ApiStatus.THAT_OPTION_EN_END);
		}
		if (direction == ContractOptionOrderDirection.BUY.getCode() && contractOptionCoin.getEnableBuy() == 0) {
			return Result.fail(ApiStatus.THAT_OPTION_EN_END);
		}
		// 期权合约是否存在
		ContractGold contractOption1 = new ContractGold();
		contractOption1.setSymbol(symbol);
		contractOption1.setGoldNo(optionId.intValue());
		ContractGold contractOption = goldService.getOne(new QueryWrapper<>(contractOption1));
		Assert.notNull(contractOption, "CONTRACT_PERIOD_DOES_NOT_EXIST");
		if (contractOption.getStatus() != ContractOptionStatus.STARTING.getCode()) {
			return Result.fail(ApiStatus.THAT_COIN_DOWN);
		}
		if (!contractOption.getSymbol().equals(symbol)) {
			return Result.fail(ApiStatus.COIN_NOT_MATCHING);
		}

		// 投注金额是否超出范围
		String[] amountArr = contractOptionCoin.getAmount().split(",");
		BigDecimal amountStart = BigDecimal.valueOf(Long.valueOf(amountArr[0]));
		BigDecimal amountEnd = BigDecimal.valueOf(Long.valueOf(amountArr[amountArr.length - 1]));
		if (amount.compareTo(amountStart) < 0 || amount.compareTo(amountEnd) > 0) {
			return Result.fail(ApiStatus.BET_AMOUNT_OUT_OF_RANGE);
		}

		// 是否已参与过
		ContractGoldOrder contractOptionOrder = new ContractGoldOrder();
		contractOptionOrder.setMemberId(member.getMemberId());
		contractOptionOrder.setSymbol(symbol);
		contractOptionOrder.setGoldNo(optionId.intValue());
		List<ContractGoldOrder> contractOptionOrderList = goldOrderService.list(new QueryWrapper<>(contractOptionOrder));
		if (contractOptionOrderList != null && contractOptionOrderList.size() > 0) {
			return Result.fail(ApiStatus.OPTION_PARTICIPATED);
		}

		Wallet walletQ = new Wallet();
		walletQ.setMemberId(member.getMemberId());
		//walletQ.setType("USDT");
		//钱包账户
		Wallet finOption = F.me().getWallet(member.getMemberId(), "USDT");
//        List<Wallet> walletList = walletService.list(new QueryWrapper<>(walletQ));
		// 需要 投注额 + 手续费
		BigDecimal totalAmount = amount.add(contractOptionCoin.getFeePercent().multiply(amount));
		if (totalAmount.compareTo(finOption.getUsedPrice()) > 0) {
			return Result.fail(ApiStatus.WALLET_LESS);
		}

		// 新建订单
		ContractGoldOrder orderObj = new ContractGoldOrder();
		orderObj.setBaseSymbol(contractOptionCoin.getBaseSymbol());
		orderObj.setBetAmount(amount);
		orderObj.setCoinSymbol(contractOptionCoin.getCoinSymbol());
		orderObj.setGoldId(contractOption.getId());
		orderObj.setDirection(direction);
		orderObj.setFee(contractOptionCoin.getFeePercent().multiply(amount));
		orderObj.setWinFee(BigDecimal.ZERO);
		orderObj.setMemberId(member.getMemberId());
		orderObj.setResult(ContractOptionOrderResult.WAIT.getCode());
		orderObj.setRewardAmount(BigDecimal.ZERO);
		orderObj.setStatus(ContractOptionOrderStatus.OPEN.getCode());
		orderObj.setSymbol(contractOptionCoin.getSymbol());
		orderObj.setCreateTime(Calendar.getInstance().getTime());
		orderObj.setGoldNo(contractOption.getGoldNo());
		goldOrderService.save(orderObj);

		// 锁定资产
		//memberWalletService.freezeBalance(memberWallet, amount.add(amount.multiply(contractOptionCoin.getFeePercent())));
		finOption.setUsedPrice(finOption.getUsedPrice().subtract(totalAmount));
		finOption.setLockedPrice(finOption.getLockedPrice().add(totalAmount));
		walletService.updateById(finOption);
		// 总单投注额增加
		if (direction == ContractOptionOrderDirection.BUY.getCode()) {
			contractOption.setTotalBuy(contractOption.getTotalBuy().add(amount));
			contractOption.setTotalBuyCount(contractOption.getTotalBuyCount() + 1);
		} else if (direction == ContractOptionOrderDirection.SELL.getCode()) {
			contractOption.setTotalSell(contractOption.getTotalSell().add(amount));
			contractOption.setTotalSellCount(contractOption.getTotalSellCount() + 1);
		}
		goldService.updateById(contractOption);
		return Result.success("下注成功");


	}


	/*黄金交易end*/

	/**
	 * 期货页面信息
	 *
	 * @param token   token
	 * @param symbols 交易对
	 * @param coin    币种 USDT MGE
	 * @return Result
	 */
	public Result futuresContractPage(String token, String symbols, String coin) {
		if (StringUtils.isBlank(symbols)) {
			return Result.fail(ApiStatus.ERROR);
		}
		Member member = (Member) redisUtil.get(token);
		FinFutures contract = (FinFutures) redisUtil.get(String.format(FINFUTURES_CODE, coin) + member.getMemberId());
		if (contract == null) {
			return fail(ApiStatus.BAD_REQUEST);
		}
		Futures swap = getFuturesOne(symbols);
		if (swap == null) return Result.fail(ApiStatus.ERROR);

		HashedMap map = new HashedMap();
		map.put("handNumber", swap.getHandNumber());//一手价值
		map.put("openFeePrice", swap.getOpenFeePrice().toPlainString());//开仓手续费率
		map.put("symbols", symbols);//币种
		map.put("price", contract.getUsedPrice().toPlainString());//可用保证金
		FuturesLeverage leverage = new FuturesLeverage();
		leverage.setDel("N");
		List<FuturesLeverage> list = futuresLeverageService.list(new QueryWrapper<>(leverage));
		List<Map<String, Object>> leverageList = new ArrayList<>(list.size());
		for (FuturesLeverage leverage1 : list) {
			Map<String, Object> leverageMap = new HashMap<>();
			leverageMap.put("id", leverage1.getFuturesLeverageId());
			leverageMap.put("name", leverage1.getName());
			leverageList.add(leverageMap);
		}
		map.put("leverageList", leverageList);//杠杠倍数
		return success("合约页面信息", map);
	}

	public Result futuresLeverage(String token) {
		FuturesLeverage leverage = new FuturesLeverage();
		leverage.setDel("N");

		return success(futuresLeverageService.list(new QueryWrapper<>(leverage)));
	}

	//Symbol变更修改地方
	public Futures getFuturesOne(String symbol) {
		Futures s = new Futures();
		s.setSymbol(symbol);
		s.setDel("N");
		s.setStatus("Y");
		return futuresService.getOne(new QueryWrapper<>(s));
//        if (swaps == null) return null;
//        Futures swap = null;
//        for (Futures futures : swaps) {
//
//            if(futures.getSymbol().split("/")[1].equals(symbol)){
//                swap=futures;
//            }
//        }
		// return s;
	}

	/**
	 * 合约交易t
	 */
	@Transactional(rollbackFor = Exception.class)
	public Result futures(String token, ApiFuturesDto dto) throws Exception {
		synchronized (this) {
			Member member = (Member) redisUtil.get(token);
			if (!"1".equals(member.getRealStatus())) {
				return Result.fail(ApiStatus.NOT_REAL);
			}

			//获取火币的最新行情价
			BigDecimal close = getClosePrice(ProConst.FuturesType.getCode(dto.getSymbols().split("/")[0]), KINE_FUTURES);
			if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)) {//限价，判断是买入模式是否委托价大于当前行情价
				// ，卖出模式是否委托价小于当前行情价
				if (StrUtil.equals(dto.getCompactType(), CompactType.BUY.code) && dto.getUnit().compareTo(close) > 0) {
					return Result.fail(ApiStatus.SELL_UNIT_MAX);
				}
				if (StrUtil.equals(dto.getCompactType(), CompactType.SELL.code) && dto.getUnit().compareTo(close) < 0) {
					return Result.fail(ApiStatus.BUY_UNIT_MAX);
				}
			}

//            Futures swap = new Futures();
//            swap.setSymbol(dto.getSymbols());
//            swap.setDel("N");
//            swap =  this.futuresService.getOne(new QueryWrapper<>(swap));
			Futures swap = getFuturesOne(dto.getSymbols());
			if (swap == null) {
				return Result.fail(ApiStatus.ERROR);
			}
			//获取杠杆倍率
			FuturesLeverage leverage = futuresLeverageService.getById(dto.getLeverageId());
			FinFutures contract = F.me().getFinFutures(member.getMemberId(), "USDT");
			//市价、限价
			BigDecimal unit = dto.getUnit();
			if (StrUtil.equals(dto.getDealWay(), DealWay.MARKET.code)) {//市价，拉取最新价
				unit = close;
			}
			//获取手续费比例
			BigDecimal contractFee = swap.getOpenFeePrice().divide(new BigDecimal(100), 8, RoundingMode.DOWN);
			//开仓手续费 =手数*每手的价值*开仓手续费率*委托价格  。
			BigDecimal fee = dto.getNumbers().multiply(swap.getHandNumber()).multiply(unit).multiply(contractFee);
			//开仓保证金=委托手数*每手价值数量*委托价格/杠杆倍数
			BigDecimal openPrice = dto.getNumbers().multiply(swap.getHandNumber())
					.multiply(unit).divide(leverage.getValue(), 8, BigDecimal.ROUND_HALF_UP);
			//委托占用=仓位保证金+开仓手续费
			BigDecimal totalPayPrice = fee.add(openPrice);
			//用户合约资产
			FinFutures contractObj = F.me().getFinFutures(member.getMemberId(), dto.getCoin());
			//实际可用部分
			BigDecimal actualPrice = contract.getUsedPrice();
			if (BigDecimal.ZERO.compareTo(contractObj.getNoPl() == null ? BigDecimal.ZERO : contractObj.getNoPl()) < 0) {
				actualPrice = actualPrice.add(contractObj.getNoPl());
			}
			//查询可用保证金够不够扣
			if (actualPrice.compareTo(totalPayPrice) < 0)
				return fail(ApiStatus.NO_ORDER_MONEY);
			//默认未平仓
			String status = CompactStatus.N.code;
			//订单状态处理
			//“卖出开空”：用户输入价 < 当前行情价时，订单显示在当前委托中
			if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)
					&& StrUtil.equals(dto.getCompactType(), CompactType.SELL.code)
					&& dto.getUnit().compareTo(close) > 0) {
				status = CompactStatus.IN.code;//委托中
			}
			//“买入开多”:用户输入价 > 当前行情价时，订单显示在当前委托中
			if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)
					&& StrUtil.equals(dto.getCompactType(), CompactType.BUY.code)
					&& dto.getUnit().compareTo(close) < 0) {
				status = CompactStatus.IN.code;
			}
			//建仓价计算
			BigDecimal tradePrice;
			if (StrUtil.equals(dto.getCompactType(), CompactType.BUY.code)) {
				//买涨  当前价+点差
				tradePrice = unit.add(BigDecimal.ZERO);
			} else {
				//买跌
				tradePrice = unit.subtract(BigDecimal.ZERO);
			}
			BigDecimal willWorth = contractObj.getUsedPrice().subtract(fee);
			if (willWorth.compareTo(BigDecimal.ZERO) <= 0)
				return fail(ApiStatus.NO_MONEY);

			//生成订单
			FuturesCompact compact = new FuturesCompact();
			compact.setCompactType(dto.getCompactType())
					.setDealWay(dto.getDealWay())
					.setTradePrice(tradePrice)
					.setOpenFeeRatio(swap.getOpenFeePrice())
					.setFee(fee)
					.setUnit(unit)
					.setOrderNo(cn.stylefeng.guns.modular.base.util.RandomUtil.code("F"))
					.setSymbols(dto.getSymbols())
					.setNumbers(dto.getNumbers())
					.setPositionPrice(openPrice)
					.setTotalPrice(dto.getNumbers())
					.setMemberId(member.getMemberId())
					.setLeverName(leverage.getName())
					.setLeverRate(leverage.getValue())
					.setStatus(status)
					.setCoin(dto.getCoin())
					.setHandNumber(swap.getHandNumber().intValue())
					.setCreateUser(member.getMemberId());
			futuresCompactService.save(compact);

			String flowCoin = dto.getCoin();


			//变更用户的可用余额
			BigDecimal userPrice = contract.getUsedPrice();
			contract.setUsedPrice(contract.getUsedPrice().subtract(totalPayPrice));
			contract.setUpdateTime(new Date());
			contract.setUpdateUser(1L);
			while (true) {
				if (this.finFuturesService.updateWallet(contract) > 0) {
					//流水生成可用项
					F.me().saveCashflow(member.getMemberId(), WalletBigType.FUTURES,
							CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.FUTURES, totalPayPrice,
							flowCoin, totalPayPrice, flowCoin, fee, flowCoin,
							ItemCode.USED, flowCoin, null, null,
							userPrice, contract.getUsedPrice(),
							member.getMemberId(), SYS_PLATFORM);
					break;
				}
			}
			jobService.refreshFinFuturesInfo(member.getMemberId(), flowCoin);
			redisUtil.lSet(OP_FINFUTURES_DATA, member.getMemberId());
		}
		return success(ApiStatus.OK);
	}

	/**
	 * 期货订单列表
	 *
	 * @param token
	 * @param type       页面类型 N-持仓 IN-当前委托 Y-平仓订单
	 * @param showMethod 持仓或当前委托需传递显示的数据：ALL显示全部，CURRENT 当前的币种
	 * @param symbols    币种
	 * @param buyMethod  下单方式：BUY或者SELL
	 * @param status     状态类型：手动平仓：HANDLE，FIXED：强制平仓，PROFIT： 止盈平仓，LOSS：止损平仓
	 * @param page
	 * @return Result
	 */
	public Result futuresContractList(String token, String type, Page page,
									  String showMethod, String symbols, String buyMethod, String status) {
		Member member = (Member) redisUtil.get(token);
		FuturesCompact compact = new FuturesCompact();
		compact.setMemberId(member.getMemberId()).setDel("N");
		compact.setStatus(type);
		if ("CURRENT".equals(showMethod)) {
			compact.setSymbols(symbols);
		}
		if ("Y".equals(type)) {
			if (StringUtils.isNotBlank(symbols)) compact.setSymbols(symbols);
			if (StringUtils.isNotBlank(buyMethod)) compact.setCompactType(buyMethod);
			if (StringUtils.isNotBlank(status)) compact.setExitType(status);
		}
		IPage iPage = futuresCompactService.page(page, new QueryWrapper<>(compact).orderByDesc(CREATE_TIME));
		List<FuturesCompact> dtoList = iPage.getRecords();
		List<ApiFuturesCompactDto> list = new ArrayList<>(dtoList.size());
		for (FuturesCompact entity : dtoList) {
			Object pl = entity.getPl();
			ApiFuturesCompactDto apiCompactDto = new ApiFuturesCompactDto();
			Double lossProfitRatio = 0.0;
			if (redisUtil.get(PL + entity.getOrderNo()) != null && type.equals("N")) {
				pl = redisUtil.get(PL + entity.getOrderNo());
				lossProfitRatio = new BigDecimal(pl + "").divide((
						entity.getTradePrice().multiply(entity.getNumbers())
								.multiply(new BigDecimal(entity.getHandNumber()))
				), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
			} else if ("Y".equals(type)) {
				lossProfitRatio = new BigDecimal(pl + "").divide((
						entity.getTradePrice().multiply(entity.getHandPrice())
				), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
			}
			apiCompactDto.setCompactId(entity.getFuturesCompactId());
			apiCompactDto.setOrderNo(entity.getOrderNo());
			apiCompactDto.setSymbols(entity.getSymbols());
			apiCompactDto.setCompactType(entity.getCompactType());
			apiCompactDto.setLeverName(entity.getLeverName());
			apiCompactDto.setPositionPrice(entity.getPositionPrice().toPlainString());
			apiCompactDto.setTradePrice(entity.getTradePrice().toPlainString());
			apiCompactDto.setOpenHandPrice((entity.getHandNumber() * entity.getNumbers().doubleValue()) + "");
			apiCompactDto.setHandNumber(entity.getHandNumber() + "");
			if (!"Y".equals(type)) {
				Futures swap = getFuturesOne(entity.getSymbols());
//                swap.setSymbol(entity.getSymbols());
////                swap.setDel("N");
				//swap = this.futuresService.getOne(new QueryWrapper<>(swap));
				//平仓手续费比例
				apiCompactDto.setExitFeeRatio(swap.getCloseFeePrice().toPlainString());
			}
			apiCompactDto.setFee(entity.getFee().toPlainString());
			if (entity.getStopLoss() != null)
				apiCompactDto.setStopLoss(entity.getStopLoss().toPlainString());
			if (entity.getStopProfit() != null)
				apiCompactDto.setStopProfit(entity.getStopProfit().toPlainString());
			if ("Y".equals(type)) {
				apiCompactDto.setExitPositionPrice(entity.getExitPositionPrice().toPlainString());
				apiCompactDto.setExitPrice(entity.getExitPrice().toPlainString());
				apiCompactDto.setCloseNumber(entity.getCloseNumber());
				apiCompactDto.setHandPrice(entity.getHandPrice().toPlainString());
				apiCompactDto.setCloseFeePrice(entity.getCloseFeePrice().toPlainString());
				for (ExitType exitType : ExitType.values()) {
					if (exitType.code.equals(entity.getExitType())) {
						apiCompactDto.setExitType(exitType.value);
					}
				}
			}
			apiCompactDto.setLossProfit("0.0");
			if (pl != null) {
				apiCompactDto.setLossProfit(new BigDecimal(pl + "").toPlainString() + "");
			}
			apiCompactDto.setLossProfitRatio(lossProfitRatio + "%");
			apiCompactDto.setCreateTime(entity.getCreateTime());
			apiCompactDto.setExitTime(entity.getExitTime());
			apiCompactDto.setNumbers(entity.getNumbers());
			list.add(apiCompactDto);
		}
		iPage.setRecords(list);
		return success(iPage);

	}

	@Transactional(rollbackFor = Exception.class)
	public Result cancelFuturesContract(String token, Long compactId, Integer type) {
		Member member = (Member) redisUtil.get(token);
		if (type <= 1) {
			FuturesCompact compact = futuresCompactService.getById(compactId);
			if (!StrUtil.equals(compact.getStatus(), CompactStatus.IN.code))
				return fail(ApiStatus.CANCEL_FAIL);
			synchronized (this) {
				compact.setStatus(CompactStatus.CANCEL.code).setUpdateUser(member.getMemberId());
				futuresCompactService.updateById(compact);

				String flowCoin = compact.getCoin();
				//保证金退回
				FinFutures contract = F.me().getFinFutures(member.getMemberId(), flowCoin);
				//保证金项
				F.me().saveCashflow(member.getMemberId(), WalletBigType.FUTURES, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.FUTURES_CANCEL,
						compact.getPositionPrice().add(compact.getFee()), flowCoin, compact.getPositionPrice().add(compact.getFee()), flowCoin, BigDecimal.ZERO, flowCoin,
						ItemCode.USED, flowCoin, null, null,
						contract.getUsedPrice(), contract.getUsedPrice().add(compact.getPositionPrice().add(compact.getFee())),
						SYS_PLATFORM, member.getMemberId());

				//更新钱包
				contract.setUsedPrice(contract.getUsedPrice().add(compact.getPositionPrice().add(compact.getFee())))
						.setEntrustPrice(contract.getEntrustPrice().subtract(compact.getPositionPrice().add(compact.getFee())));
				contract.setUpdateTime(new Date());
				contract.setUpdateUser(member.getMemberId());
				while (true) {
					if (finFuturesService.updateWallet(contract) > 0) {
						break;
					}
				}
				jobService.refreshFinFuturesInfo(member.getMemberId(), flowCoin);
				redisUtil.lSet(OP_FINFUTURES_DATA, member.getMemberId());

			}
			return success(ApiStatus.OK);
		}
		synchronized (this) {
			//全部委托订单撤销
			FuturesCompact compact = new FuturesCompact();
			compact.setStatus(CompactStatus.IN.code);
			compact.setDel("N");
			compact.setMemberId(member.getMemberId());
			List<FuturesCompact> list = futuresCompactService.list(new QueryWrapper<>(compact));
			if (list != null && list.size() > 0) {
				//redisUtil.lSet(OP_CONTRACT_DATA, member.getMemberId());
				for (FuturesCompact compact1 :
						list) {
					FuturesCompact entity = futuresCompactService.getById(compact1.getFuturesCompactId());
					if (!StrUtil.equals(entity.getStatus(), CompactStatus.IN.code)) {
						continue;
					}
					entity.setStatus(CompactStatus.CANCEL.code).setUpdateUser(member.getMemberId());
					futuresCompactService.updateById(entity);

					String flowCoin = entity.getCoin();
					//保证金退回
					FinFutures contract = F.me().getFinFutures(member.getMemberId(), flowCoin);
					//保证金项
					F.me().saveCashflow(member.getMemberId(), WalletBigType.FUTURES, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.FUTURES_CANCEL,
							entity.getPositionPrice().add(entity.getFee()), flowCoin, entity.getPositionPrice().add(entity.getFee()), flowCoin, BigDecimal.ZERO, flowCoin,
							ItemCode.USED, flowCoin, null, null,
							contract.getUsedPrice(), contract.getUsedPrice().add(entity.getPositionPrice().add(entity.getFee())),
							SYS_PLATFORM, member.getMemberId());

					//更新钱包
					contract.setUsedPrice(contract.getUsedPrice().add(entity.getPositionPrice().add(entity.getFee())))
							.setEntrustPrice(contract.getEntrustPrice().subtract(entity.getPositionPrice().add(entity.getFee())));
					contract.setUpdateTime(new Date());
					contract.setUpdateUser(member.getMemberId());
					while (true) {
						if (finFuturesService.updateWallet(contract) > 0) {
							break;
						}
					}
					// jobService.refreshContractInfo(member.getMemberId(), flowCoin);
				}
			}
		}
		return success(ApiStatus.OK);
	}

	public Result futuresContractPl(String token, ApiPlDto dto) {
		Member member = (Member) redisUtil.get(token);
		FuturesCompact compactQ = new FuturesCompact();
		compactQ.setMemberId(member.getMemberId())
				.setFuturesCompactId(dto.getCompactId())
				.setDel("N")
				.setStatus(CompactStatus.N.code);
		FuturesCompact compactR = futuresCompactService.getOne(new QueryWrapper<>(compactQ));
		if (compactR == null) {
			return fail(ApiStatus.ERROR, "订单不存在");
		}
		//当前行情
		BigDecimal close = getClosePrice(compactR.getSymbols(), KINE_FUTURES);

		if (StrUtil.equals(compactR.getCompactType(), CompactType.BUY.code)) {//买涨
			if (StringUtils.isNotBlank(dto.getStopProfit())
					&& new BigDecimal(dto.getStopProfit()).compareTo(compactR.getTradePrice()) <= 0) {
				return fail(ApiStatus.MAX_STOP_PROFIT);
			}

			if (StringUtils.isNotBlank(dto.getStopLoss())
					&& new BigDecimal(dto.getStopLoss()).compareTo(compactR.getTradePrice()) >= 0) {
				return fail(ApiStatus.MIN_STOP_LOSS);
			}
			if (StringUtils.isNotBlank(dto.getStopLoss()) && new BigDecimal(dto.getStopLoss()).compareTo(close) >= 0) {
				return fail(ApiStatus.NOT_UP_LOSS);
			}

		} else {//买跌
			if (StringUtils.isNotBlank(dto.getStopProfit())
					&& new BigDecimal(dto.getStopProfit()).compareTo(compactR.getTradePrice()) >= 0) {
				return fail(ApiStatus.MIN_STOP_PROFIT);
			}
			if (StringUtils.isNotBlank(dto.getStopLoss())
					&& new BigDecimal(dto.getStopLoss()).compareTo(compactR.getTradePrice()) <= 0) {
				return fail(ApiStatus.MAX_STOP_LOSS);
			}
			if (StringUtils.isNotBlank(dto.getStopLoss()) && new BigDecimal(dto.getStopLoss()).compareTo(close) <= 0) {
				return fail(ApiStatus.NOT_DOWN_LOSS);
			}
		}
		if (StringUtils.isNotBlank(dto.getStopLoss())) {
			compactR.setStopLoss(new BigDecimal(dto.getStopLoss()));
		}
		if (StringUtils.isNotBlank(dto.getStopProfit())) {
			compactR.setStopProfit(new BigDecimal(dto.getStopProfit()));
		}
		compactR.setUpdateTime(new Date());
		compactR.setUpdateUser(member.getMemberId());
		futuresCompactService.updateById(compactR);
		jobService.refreshFinFuturesInfo(member.getMemberId(), compactR.getCoin());
		redisUtil.lSet(OP_FINFUTURES_DATA, member.getMemberId());
		return success(ApiStatus.OK);
	}

	/**
	 * 平仓
	 *
	 * @param token     token
	 * @param compactId compactId
	 * @param type
	 * @return 平仓
	 */
	@Transactional(rollbackFor = Exception.class)
	public Result outFuturesContract(String token, Long compactId, Integer type, BigDecimal number) {
		synchronized (this) {
			Member member = (Member) redisUtil.get(token);
			if (type <= 1) {
				FuturesCompact compactQ = new FuturesCompact();
				compactQ.setMemberId(member.getMemberId())
						.setFuturesCompactId(compactId)
						.setDel("N")
						.setStatus(CompactStatus.N.code);
				FuturesCompact compactR = futuresCompactService.getOne(new QueryWrapper<>(compactQ));
				if (compactR == null)
					fail(ApiStatus.NOT_OUT_CONTRACT);
				if (number.compareTo(BigDecimal.ZERO) <= 0) {
					return Result.fail(ApiStatus.MIN_ZERO_NUMBER);
				}
				if (number.compareTo(compactR.getNumbers()) > 0) {
					return Result.fail(ApiStatus.EXIT_NUMBER_NO);
				}
				dealOutFuturesContract(compactR, number, member);
				return success(ApiStatus.OK);
			}
			FuturesCompact compactQ = new FuturesCompact();
			compactQ.setMemberId(member.getMemberId())
					.setDel("N")
					.setStatus(CompactStatus.N.code);
			List<FuturesCompact> list = futuresCompactService.list(new QueryWrapper<>(compactQ));
			for (FuturesCompact compact : list) {
				dealOutFuturesContract(compact, compact.getNumbers(), member);
			}
			return success(ApiStatus.OK);
		}

	}

	@Transactional
	public void dealOutFuturesContract(FuturesCompact compactR, BigDecimal number, Member member) {
		boolean flag = true;
		if (compactR.getNumbers().compareTo(number) > 0) {
			flag = false;
		}
		//当前行情价
		BigDecimal currentUnit = getClosePrice(compactR.getSymbols(), KINE_FUTURES);
		//总盈亏
		BigDecimal totalPlPrice = BigDecimal.ZERO;
		//最新行情价
		BigDecimal close = getClosePrice(compactR.getSymbols(), KINE_FUTURES);
		//平仓价
		BigDecimal outDivTrade = close;
		//平仓价值数量
		BigDecimal handPrice = number.multiply(new BigDecimal(compactR.getHandNumber()));
		//买涨
		if (StrUtil.equals(compactR.getCompactType(), CompactType.BUY.code)) {
			//做多盈亏 =平仓
			BigDecimal tradeOutNum = (outDivTrade.subtract(compactR.getTradePrice()));
			totalPlPrice = tradeOutNum.multiply(handPrice);
		}
		//买跌
		if (StrUtil.equals(compactR.getCompactType(), CompactType.SELL.code)) {
			BigDecimal tradeOutNum = (compactR.getTradePrice().subtract(outDivTrade));
			totalPlPrice = tradeOutNum.multiply(handPrice);
		}

		Futures swap = new Futures();
		swap.setSymbol(compactR.getSymbols());
		swap.setDel("N");
		swap = this.futuresService.getOne(new QueryWrapper<>(swap));
		//平仓手续费比例
		BigDecimal outFeeRate = swap.getCloseFeePrice().divide(new BigDecimal(100));
		//平仓手续费=平仓手数*每手价值数量*平仓价格*平仓手续费率
		BigDecimal outFee = handPrice
				.multiply(outDivTrade)
				.multiply(outFeeRate);
		BigDecimal exitPostionPrice = number
				.multiply(compactR.getPositionPrice())
				.divide(compactR.getNumbers(), 8, BigDecimal.ROUND_HALF_UP);
		BigDecimal totalReturnPrice = exitPostionPrice.add(totalPlPrice).subtract(outFee);

		if (flag) {//表示该订单已完成，无需在创建
			compactR.setExitPrice(close)
					.setExitType(ExitType.HANDLE.code)
					.setStatus(CompactStatus.Y.code)
					.setExitTime(new Date())
					.setTpl(totalPlPrice)//盈亏额
					.setCloseNumber(number)//平仓手数
					.setPl(totalPlPrice)//盈亏
					.setExitPositionPrice(compactR.getPositionPrice())//平仓保证金
					.setCloseFeePrice(outFee)
					.setHandPrice(handPrice)
					.setUpdateUser(compactR.getMemberId())
			;
			futuresCompactService.updateById(compactR);
		} else {
			//生成订单
			FuturesCompact compact = new FuturesCompact();
			compact.setCompactType(compactR.getCompactType())
					.setDealWay(compactR.getDealWay())
					.setTradePrice(compactR.getTradePrice())
					.setFee(compactR.getFee())
					.setUnit(compactR.getUnit())
					.setOrderNo(cn.stylefeng.guns.modular.base.util.RandomUtil.code("F"))
					.setSymbols(compactR.getSymbols())
					.setNumbers(compactR.getNumbers())
					.setPositionPrice(compactR.getPositionPrice())
					.setMemberId(member.getMemberId())
					.setLeverName(compactR.getLeverName())
					.setLeverRate(compactR.getLeverRate())
					.setStatus(CompactStatus.Y.code)
					.setCoin(compactR.getCoin())
					.setHandNumber(compactR.getHandNumber())
					.setExitPrice(currentUnit)
					.setExitType(ExitType.HANDLE.code)
					.setExitTime(new Date())
					.setTpl(totalPlPrice)
					.setCloseNumber(number)
					.setPl(totalPlPrice)
					.setCloseFeePrice(outFee)
					.setHandPrice(handPrice)
					.setExitPositionPrice(exitPostionPrice)
					.setCreateUser(member.getMemberId());
			futuresCompactService.save(compact);

			//重新进行老的剩余的仓位保证金
			BigDecimal num = compactR.getNumbers().subtract(number);
			BigDecimal newPostionPrice = new BigDecimal(compactR.getHandNumber())
					.multiply(num)
					.multiply(compactR.getTradePrice())
					.divide(compactR.getLeverRate(), 8, BigDecimal.ROUND_HALF_UP);
			compactR.setNumbers(num);
			compactR.setPositionPrice(newPostionPrice);
			BigDecimal newFee = compactR.getUnit().multiply(compactR.getNumbers())
					.multiply(new BigDecimal(compactR.getHandNumber())).multiply(compactR.getOpenFeeRatio().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP));
			compactR.setFee(newFee);
			compactR.setUpdateTime(new Date());
			compactR.setUpdateUser(member.getMemberId());
			futuresCompactService.updateById(compactR);
		}

		String coin = compactR.getCoin();
		//配资账户
		FinFutures entrust = F.me().getFinFutures(member.getMemberId(), compactR.getCoin());
		BigDecimal usedPrice = entrust.getUsedPrice().add(totalReturnPrice).compareTo(BigDecimal.ZERO) < 0
				? BigDecimal.ZERO : entrust.getUsedPrice().add(totalReturnPrice);
		if (totalPlPrice.compareTo(BigDecimal.ZERO) > 0) {
			F.me().saveCashflow(compactR.getMemberId(), WalletBigType.FUTURES, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.HANDLE,
					totalPlPrice, coin, totalPlPrice, coin, outFee, coin,
					ItemCode.USED, coin, null, "盈利",
					entrust.getUsedPrice(), usedPrice,
					compactR.getMemberId(), compactR.getMemberId());
		} else if (totalPlPrice.compareTo(BigDecimal.ZERO) < 0) {
			F.me().saveCashflow(compactR.getMemberId(), WalletBigType.FUTURES, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.HANDLE,
					totalPlPrice.abs(), coin, totalPlPrice.abs(), coin, outFee, coin,
					ItemCode.USED, coin, null, "亏损",
					entrust.getUsedPrice(), usedPrice,
					compactR.getMemberId(), compactR.getMemberId());
		}

		entrust.setUsedPrice(usedPrice);
		entrust.setUpdateUser(-1L);
		entrust.setUpdateTime(new Date());
		while (true) {
			if (finFuturesService.updateWallet(entrust) > 0) {
				log.info("用户平仓,更新钱包:{}", member.getMemberId());
				break;
			}
		}
		log.info("用户平仓:{}", member.getMemberId());
	}

	//查询账户的收益，直属，非直属，总收益
	public Result earningsInfo(String token) {
		Member member = (Member) redisUtil.get(token);
		QueryWrapper<TeamEarnings> teamEarningsQueryWrapper = new QueryWrapper<>();
		teamEarningsQueryWrapper.eq("earnings_id", member.getMemberId());
		List<TeamEarnings> list = teamEarningsService.list(teamEarningsQueryWrapper);
		Map<String, Object> map = new HashMap<>();

		BigDecimal directY = BigDecimal.ZERO;
		BigDecimal directN = BigDecimal.ZERO;
		for (TeamEarnings teamEarnings : list) {
			if (teamEarnings.getDirect().equals(Constant.Y)) {
				directY = directY.add(teamEarnings.getPrice());
			} else {
				directN = directN.add(teamEarnings.getPrice());
			}
		}
		map.put("list", list);
		map.put("directN", directN);
		map.put("directY", directY);
		map.put("type", F.me().cfg(TEAM_BONUS_COIN));
		map.put("all", directY.add(directN));

		return Result.success(map);
	}

	public Result earningsPage(Page page, String token) {
		Member member = (Member) redisUtil.get(token);
		TeamEarnings teamEarnings = new TeamEarnings();
		teamEarnings.setEarningsId(member.getMemberId());
		IPage result = teamEarningsService.page(page, new QueryWrapper<>(teamEarnings).orderByDesc(CREATE_TIME));
		List<TeamEarnings> records = result.getRecords();
		for (TeamEarnings record : records) {
			String account = record.getRegistAccount();
			if (account.contains("@")) {
				record.setRegistAccount(account.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4"));
			} else {
				//第一个参数是正则表达式，$1匹配第一个括号，$2匹配第二个
				record.setRegistAccount(account.substring(0, 3) + "****" + account.substring(account.length() - 3, account.length() - 1));

				//record.setRegistAccount(account.replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1******$2"));
			}
		}
		return success(result);
	}

	public Result teamDetail(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		User u = userService.getByAccount(member.getAccount());
		Page<Map<String, Object>> mapPage = memberService.selectAllForTeam(null, member.getMemberId(), u.getUserId(), page);
		return Result.success(mapPage);
	}

	public Result freeTransferForWallet(String token, BigDecimal price) {
		Member member = (Member) redisUtil.get(token);
		QueryWrapper<TeamEarning> queryWrapper = new QueryWrapper<>(new TeamEarning());
		queryWrapper.eq("member_id", member.getMemberId());
		TeamEarning one = teamEarningService.getOne(queryWrapper);
		if (one == null) return Result.fail(ApiStatus.WALLET_LESS);
		if (one.getUsedPrice().subtract(price).compareTo(BigDecimal.ZERO) >= 0) {
//            String coin = "USDT";
			String coin = F.me().cfg(TEAM_BONUS_COIN);
			//划转到钱包
			Wallet usdt = F.me().getWallet(member, coin);
			usdt.setUsedPrice(usdt.getUsedPrice().add(price));
			walletService.updateById(usdt);
			F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.TEAMEARNING,
					price, coin, price, coin,
					BigDecimal.ZERO, coin,
					ItemCode.USED, coin, null, CashFlowTypeEnum.TEAMEARNING.getValue(),
					usdt.getUsedPrice().subtract(price), usdt.getUsedPrice(),
					member.getMemberId(), member.getMemberId());

			//增加日志 划转记录
			WalletTransfer walletTransfer = new WalletTransfer();
			walletTransfer.setDel(Constant.N).setAccount(member.getAccount())
					.setMemberId(member.getMemberId()).setCoin(coin)
					.setWalletType(WalletBigType.WALLET.code).setPrice(price).setCreateUser(1L);
			walletTransferService.save(walletTransfer);

			one.setWithdrawPrice(one.getWithdrawPrice().add(price));
			one.setUsedPrice(one.getUsedPrice().subtract(price)).setUpdateUser(1L);
			teamEarningService.updateById(one);
		} else {
			return Result.fail(ApiStatus.WALLET_LESS);
		}
		return Result.success();
	}

	public Result freeTransferForWalletLog(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		WalletTransfer teamEarnings = new WalletTransfer();
		teamEarnings.setMemberId(member.getMemberId());
		return success(walletTransferService.page(page, new QueryWrapper<>(teamEarnings).orderByDesc(CREATE_TIME)));

	}

	public Result myearnings(String token) {
		Member member = (Member) redisUtil.get(token);
		TeamEarning teamEarning = new TeamEarning();
		QueryWrapper<TeamEarning> teamEarningQueryWrapper = new QueryWrapper<>(teamEarning);
		teamEarningQueryWrapper.eq("member_id", member.getMemberId());
		TeamEarning one = teamEarningService.getOne(teamEarningQueryWrapper);
		if (one == null) {
			one = new TeamEarning();
			one.setMemberId(member.getMemberId()).setType(F.me().cfg(TEAM_BONUS_COIN))
					.setUsedPrice(BigDecimal.ZERO).setWithdrawPrice(BigDecimal.ZERO)
					.setAccount(member.getAccount()).setBlockedPrice(BigDecimal.ZERO)
					.setDel("N");
			one.setCreateTime(new Date());
			one.setCreateUser(-1L);
			//teamEarningService.save(one);
		}
		return Result.success(one);
	}

	public Result addCoinApply(String token, CoinApply coinApply, String code) {
		Member member = (Member) redisUtil.get(token);
		boolean equals = "N".equals(F.me().cfg(SMSSENDOPEN));
		boolean equals2 = "N".equals(F.me().cfg(EMAILSENDOPEN));

		if (!equals && !equals2) {
			String msg = (String) redisUtil.get(SMS + member.getPhone());
			if (msg == null) {
				msg = (String) redisUtil.get(SMS + member.getEmail());
			}
			if (!StrUtil.equals(msg, code)) {
				return fail(ApiStatus.MSG_ERROR);
			}
		}

		coinApply.setMemberId(member.getMemberId()).setAccount(member.getAccount()).setStatus("N").setCreateUser(1L);
		coinApplyService.addCoinApply(coinApply);
		redisUtil.del(SMS + member.getPhone(), SMS + member.getEmail());
		try {
			WebSocketService.sendInfo(ADDCOINAPPLYSTRING, SYSTEMSOCKETSENDNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success();
	}

	@Transactional
	public Result addMiningOrder(String token, Long id) {
		Member member = (Member) redisUtil.get(token);
		Mining mining = miningService.getById(id);
		if (mining.getStatus().equals("N") || mining.getDel().equals("Y") || (!mining.getStep().equals("1"))) {
			return Result.fail(ApiStatus.MINING_ERROR);
		}
		if (mining.getTotalSupply().intValue() <= mining.getTradedAmount().intValue()) {
			return Result.fail(ApiStatus.MINING_NUM_ERROR);
		}
		//新增

		MiningOrder mOQ = new MiningOrder();
		mOQ.setMiningId(id).setMemberId(member.getMemberId());
		int count = miningOrderService.count(new QueryWrapper<>(mOQ));
		if (count >= mining.getLimitTimes()) {
			return Result.fail(ApiStatus.MINING_NUM_UNENOUGH);
		}

		//校验钱包余额是否充足
//        FinMining finMiningFuel = F.me().getMining(member.getMemberId(), mining.getFuelEnergyCoin());
//        if(finMiningFuel==null || finMiningFuel.getUsedPrice().compareTo(mining.getFuelEnergyNumber())<0){
//            //燃料能源不足
//            return Result.fail(ApiStatus.MINING_FUEL_UNENOUGH);
//        }
		FinMining finMiningPri = F.me().getMining(member.getMemberId(), mining.getPriceCoin());
		if (finMiningPri == null || finMiningPri.getUsedPrice().compareTo(mining.getPrice()) < 0) {
			//合约价格不足
			return Result.fail(ApiStatus.MINING_PRI_UNENOUGH);
		}

//       扣除燃料能源 改为每启动一次扣除一次燃料能源
//        BigDecimal fuelUsed = finMiningFuel.getUsedPrice();
//        finMiningFuel.setUsedPrice(fuelUsed.subtract(mining.getFuelEnergyNumber()));
//        finMiningFuel.setUpdateTime(new Date());
//        finMiningFuel.setUpdateUser(SYS_PLATFORM);
//        finMiningService.updateById(finMiningFuel);
		//日志

//        F.me().saveCashflow(member.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.MINING_FUEL,
//                mining.getFuelEnergyNumber(), mining.getFuelEnergyCoin(), mining.getFuelEnergyNumber(), mining.getFuelEnergyCoin(), BigDecimal.ZERO, mining.getFuelEnergyCoin(),
//                ItemCode.USED, mining.getFuelEnergyCoin(), null, null,
//                fuelUsed, finMiningFuel.getUsedPrice(), member.getMemberId(), member.getMemberId());
//        扣除合约价格
		BigDecimal priUsed = finMiningPri.getUsedPrice();
		finMiningPri.setUsedPrice(priUsed.subtract(mining.getPrice()));
		finMiningPri.setUpdateUser(SYS_PLATFORM);
		finMiningPri.setUpdateTime(new Date());
		finMiningService.updateById(finMiningPri);
		//日志
		F.me().saveCashflow(member.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.MINING_PRI,
				mining.getPrice(), mining.getPriceCoin(), mining.getPrice(), mining.getPriceCoin(), BigDecimal.ZERO, mining.getPriceCoin(),
				ItemCode.USED, mining.getPriceCoin(), null, null,
				priUsed, finMiningPri.getUsedPrice(), member.getMemberId(), member.getMemberId());
		//生产MiningOrder
		MiningOrder miningOrder = new MiningOrder();
		miningOrder.setDel("N");

		miningOrder.setMiningStatus("0");//挖矿状态（0：待启动，1：挖矿中，2：已结束）
		miningOrder.setImage(mining.getImage()).setImage2(mining.getImage2())
				.setTitle(mining.getTitle())
				.setName(mining.getName())
				.setMemberId(member.getMemberId())
				.setCycleNumber(mining.getCycleNumber())
				.setCycleType(mining.getCycleType())
				.setMiningDays(0)
				.setMiningedDays(0)
				.setMiningCoin(mining.getMiningCoin())
				.setEstimatedCapacity(mining.getEstimatedCapacity())
				.setStaticCoin(mining.getStaticCoin())
				.setEstimatedStaticYield(mining.getEstimatedStaticYield())
				.setFuelEnergyCoin(mining.getFuelEnergyCoin())
				.setFuelEnergyNumber(mining.getFuelEnergyNumber())
				.setEnergyLossCoin(mining.getEnergyLossCoin())
				.setEnergyLossNumber(mining.getEnergyLossNumber())
				.setCalculatingPowerNumber(mining.getCalculatingPowerNumber())
				.setCalculatingPowerCoin(mining.getCalculatingPowerCoin())
				.setPriceCoin(mining.getPriceCoin()).setPrice(mining.getPrice())
		;

		//年月日
		Calendar calendar = Calendar.getInstance();
		miningOrder.setCreateTime(calendar.getTime());
		miningOrder.setCreateUser(-1L);
		if (mining.getCycleType().equals("1001")) {//计算结束时间
			calendar.add(Calendar.YEAR, mining.getCycleNumber());
		} else if (mining.getCycleType().equals("1002")) {
			calendar.add(Calendar.MONTH, mining.getCycleNumber());
		} else if (mining.getCycleType().equals("1003")) {
			calendar.add(Calendar.DAY_OF_MONTH, mining.getCycleNumber());
		}


		miningOrder.setMiningDaysprofit(BigDecimal.ZERO);//原始产出
		miningOrder.setCurrentDaysprofit(BigDecimal.ZERO)//当前日产出
				.setTotalProfit(BigDecimal.ZERO);//总产出
		miningOrder.setReceived(BigDecimal.ZERO);
		miningOrder.setAvailable(BigDecimal.ZERO);
		miningOrder.setMiningInvite(BigDecimal.ZERO).setMiningInvitelimit(BigDecimal.ZERO);
		miningOrder.setEndTime(String.valueOf(calendar.getTime().getTime()));
		miningOrder.setMiningId(mining.getId());
		miningOrderService.addMiningOrder(miningOrder);
		mining.setTradedAmount(mining.getTradedAmount().add(BigDecimal.ONE));
		mining.setUpdateTime(new Date());
		mining.setUpdateUser(SYS_PLATFORM);
		miningService.updateById(mining);
		// 分别为 合约价格是否足够，燃料能源是否足够
		return Result.success();
	}

	public Result miningOrder(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		MiningOrder miningOrder = new MiningOrder();
		miningOrder.setMemberId(member.getMemberId());

		return success(miningOrderService.page(page, new QueryWrapper<>(miningOrder).ne("mining_status", "2").orderByDesc(CREATE_TIME)));
	}

	public Result miningOrderDetail(String token, Page page) {
		Member member = (Member) redisUtil.get(token);
		MiningOrder miningOrder = new MiningOrder();
		miningOrder.setMemberId(member.getMemberId());
		IPage mining_status = miningOrderService.page(page, new QueryWrapper<>(miningOrder).eq("mining_status", "2").orderByDesc(CREATE_TIME));
		List<MiningOrder> records = mining_status.getRecords();
		for (MiningOrder miningOrder1 : records) {
			Long miningOrderId = miningOrder1.getMiningOrderId();
			List one = miningOrderDetailService.list(
					new QueryWrapper<>(
							new MiningOrderDetail())
							.eq("mining_order_id", miningOrderId)
							.eq("member_id", member.getMemberId()));
			miningOrder1.setMiningOrderDetail(one);

		}
		mining_status.setRecords(records);
		return success(mining_status);
	}

	@Transactional
	public Result startUpMiningOrder(String token, Long id) {
		Member member = (Member) redisUtil.get(token);
		MiningOrder miningOrder = miningOrderService.getById(id);
		if (miningOrder == null) {
			return fail(500);
		}
		if (!miningOrder.getMiningStatus().equals("0")) {
			return fail(ApiStatus.MINING_ON_START);
		}
		//校验钱包余额是否充足
		FinMining finMiningFuel = F.me().getMining(member.getMemberId(), miningOrder.getFuelEnergyCoin());
		if (finMiningFuel == null || finMiningFuel.getUsedPrice().compareTo(miningOrder.getFuelEnergyNumber()) < 0) {
			//燃料能源不足
			return Result.fail(ApiStatus.MINING_FUEL_UNENOUGH);
		}
		//  每次启动扣除燃料能源
		BigDecimal fuelUsed = finMiningFuel.getUsedPrice();
		finMiningFuel.setUsedPrice(fuelUsed.subtract(miningOrder.getFuelEnergyNumber()));
		finMiningFuel.setUpdateTime(new Date());
		finMiningFuel.setUpdateUser(SYS_PLATFORM);
		finMiningService.updateById(finMiningFuel);
		miningOrder.setEnergyToBeConsumed(miningOrder.getFuelEnergyNumber());
		//日志

		F.me().saveCashflow(member.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.MINING_FUEL,
				miningOrder.getFuelEnergyNumber(), miningOrder.getFuelEnergyCoin(), miningOrder.getFuelEnergyNumber(), miningOrder.getFuelEnergyCoin(), BigDecimal.ZERO, miningOrder.getFuelEnergyCoin(),
				ItemCode.USED, miningOrder.getFuelEnergyCoin(), null, null,
				fuelUsed, finMiningFuel.getUsedPrice(), member.getMemberId(), member.getMemberId());

		miningOrder.setMiningStatus("1");
		miningOrder.setUpdateTime(new Date());
		miningOrder.setUpdateUser(SYS_PLATFORM);
		miningOrderService.updateById(miningOrder);
		return success();
	}

	@Transactional
	public Result pickMiningOrder(String token, Long id, String type) {
		Member member = (Member) redisUtil.get(token);
		MiningOrder miningOrder = miningOrderService.getById(id);
		String miningCoin = miningOrder.getMiningCoin();
		BigDecimal price = BigDecimal.ZERO;
		if ("chan".equals(type)) {//每天都有挖矿
			miningCoin = miningOrder.getMiningCoin();
			price = miningOrder.getReceived();

		} else {
			miningCoin = miningOrder.getStaticCoin();
			price = miningOrder.getAvailable();
		}
		MiningOrderDetail mODQ = new MiningOrderDetail();
		mODQ.setDel("N").setMiningOrderId(id).setMemberId(member.getMemberId()).setMiningUnit(miningCoin);
		int count = miningOrderDetailService.count(new QueryWrapper<>(mODQ));
		if (count > 0) {
			return fail(ApiStatus.MINING_UNIT_OUT);
		}
		if (miningOrder == null) {
			return fail(500);
		}
		if (!miningOrder.getMiningStatus().equals("2")) {
			return fail(ApiStatus.MINING_NOT_END);
		}
		//校验钱包余额是否充足


		//领取记录
		MiningOrderDetail miningOrderDetail = new MiningOrderDetail();
		miningOrderDetail.setDel("N")
				.setMemberId(miningOrder.getMemberId())
				.setMiningOrderId(miningOrder.getMiningOrderId());


		miningOrderDetail.setMiningUnit(miningCoin).setOutput(price);


		FinMining finMining = F.me().getMining(member.getMemberId(), miningCoin);
		if (finMining == null) {
			return fail(500, "钱包不存在");
		}
		BigDecimal usedPrice = finMining.getUsedPrice();

		BigDecimal available = price;

		miningOrder.setTotalProfit(available);//领取

		finMining.setUsedPrice(usedPrice.add(available));
		finMiningService.updateById(finMining);
		F.me().saveCashflow(member.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.MINING_GET,
				available, miningCoin,
				available, miningCoin, BigDecimal.ZERO, miningCoin,
				ItemCode.USED, miningCoin, null, null,
				usedPrice, finMining.getUsedPrice(), member.getMemberId(), member.getMemberId());
		//miningOrder.setAvailable(BigDecimal.ZERO).setReceived(available);

		miningOrderDetail.setCreateTime(new Date());
		miningOrderDetail.setCreateUser(SYS_PLATFORM);
		miningOrderDetailService.addMiningOrderDetail(miningOrderDetail);

		miningOrder.setUpdateTime(new Date());
		miningOrder.setUpdateUser(SYS_PLATFORM);
		miningOrderService.updateById(miningOrder);
		return success();
	}

	public Result chenkDayConfig(String token) {
		HashMap map = new HashMap(8);
		String one_day_reward = F.me().cfg("ONE_DAY_REWARD");
		String three_day_reward = F.me().cfg("THREE_DAY_REWARD");
		String four_day_reward = F.me().cfg("FOUR_DAY_REWARD");
		String two_day_reward = F.me().cfg("TWO_DAY_REWARD");
		String five_day_reward = F.me().cfg("FIVE_DAY_REWARD");
		String six_day_reward = F.me().cfg("SIX_DAY_REWARD");
		String seven_day_reward = F.me().cfg("SEVEN_DAY_REWARD");
		String reward_coin = F.me().cfg("REWARD_COIN");
		map.put("one", one_day_reward);
		map.put("two", two_day_reward);
		map.put("three", three_day_reward);
		map.put("four", four_day_reward);
		map.put("five", five_day_reward);
		map.put("six", six_day_reward);
		map.put("seven", seven_day_reward);
		map.put("rewrdCoin", reward_coin);

		return success(map);

	}

	@Transactional
	public Result chatMsgSave(String msg, String token) {
		Member member = (Member) redisUtil.get(token);
		if (member == null) {
			return fail(ApiStatus.NOT_FIND_USER);
		}
		if ("Y".equals(member.getChatStatus())) {
			return fail(ApiStatus.ERROR_CHAT_NO);
		}
		ChatMsgDTO chatMsgDTO = null;
		try {
			chatMsgDTO = gson.fromJson(msg, ChatMsgDTO.class);
		} catch (Exception e) {
			File file = new File(msg);
			return fail(ApiStatus.ERROR_CHAT_NO);
		}

		String img = F.me().cfg(Constant.CHATIMG);
		chatMsgDTO = new ChatMsgDTO(chatMsgDTO.getName(), chatMsgDTO.getText().getText(), img, chatMsgDTO.getContentType());
		String account = member.getAccount();
		chatMsgDTO.setMemId(member.getMemberId());
		chatMsgDTO.setName(account.substring(0, 4) + "****" + account.substring(account.length() - 2, account.length() - 1));
		String chatCount = F.me().cfg(CHATMSGCOUNT);
		int count = CHATCOUNT;
		if (chatCount != null) {
			count = Integer.parseInt(chatCount);
		}
		Object o = redisUtil.get(CHATMSG);
		List<ChatMsgDTO> msgDTOS = new ArrayList<>();
		if (o != null) {
			msgDTOS = (List<ChatMsgDTO>) redisUtil.get(CHATMSG);
		}
		if (msgDTOS.size() >= count) {
			msgDTOS.remove(0);
		}
		msgDTOS.add(chatMsgDTO);
		redisUtil.set(CHATMSG, msgDTOS, EXPIRE_NORMAL);
		return success(chatMsgDTO);

	}

	public Result getChatConfigs() {
		HashMap<String, String> map = new HashMap<>(3);
		String chatDesc = F.me().cfg(CHATDESC);
		String chatName = F.me().cfg(CHATNAME);
		String chatOpen = F.me().cfg(CHATOPEN);
		map.put("chatDesc", chatDesc);
		map.put("chatName", chatName);
		map.put("chatOpen", chatOpen);
		return success(map);
	}

	public Result getChatList(String token) {
		Member member = (Member) redisUtil.get(token);
		Object o = redisUtil.get(CHATMSG);
		List<ChatMsgDTO> msgDTOS = new ArrayList<>();
		if (o != null) {
			msgDTOS = (List<ChatMsgDTO>) o;
		}
		for (ChatMsgDTO msgDTO : msgDTOS) {
			if (msgDTO.getMemId().equals(member.getMemberId()))
				msgDTO.setMine(true);
			else {
				msgDTO.setMine(false);
			}
		}

		return Result.success(msgDTOS);

	}

	public Result checkChatPwd(String token, String pwd) {
		Member member = (Member) redisUtil.get(token);
		String chatOpen = F.me().cfg(CHATOPEN);
		if (("N").equals(chatOpen)) {
			return success();
		}

		String cfg = F.me().cfg(CHATPWD);
		if (("Y").equals(member.getChatStatus())) {
			return fail(ApiStatus.ERROR_CHAT_NO);
		}
		if (cfg.equals(pwd)) {
			return success();
		} else {
			return fail(ApiStatus.ERROR_CHAT_PWD);
		}

	}

	/**
	 * 只负责赔付，修改订单在外遍
	 *
	 * @param contractOptionOrder
	 */
	public void doBack(ContractOptionOrder contractOptionOrder) {
		Long memberId = contractOptionOrder.getMemberId();
		String baseSymbol = contractOptionOrder.getBaseSymbol();
		//返还金额
//        BigDecimal betAmount = contractOptionOrder.getBetAmount();
		BigDecimal betAmount = contractOptionOrder.getRewardAmount().abs();

		BigDecimal fee = contractOptionOrder.getFee();
		FinOption finOption = F.me().getFinOption(memberId, baseSymbol);
		finOption.setUsedPrice(finOption.getUsedPrice().add(fee));
		F.me().saveCashflow(memberId, WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONFEE,
				fee, baseSymbol, fee, baseSymbol,
				BigDecimal.ZERO, baseSymbol,
				ItemCode.USED, baseSymbol, null, "后台返还",
				finOption.getUsedPrice().subtract(fee), finOption.getUsedPrice(),
				memberId, memberId);
		finOption.setUsedPrice(finOption.getUsedPrice().add(betAmount));
		F.me().saveCashflow(memberId, WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONBACK,
				betAmount, baseSymbol, betAmount, baseSymbol,
				BigDecimal.ZERO, baseSymbol,
				ItemCode.USED, baseSymbol, null, "后台返还",
				finOption.getUsedPrice().subtract(betAmount), finOption.getUsedPrice(),
				memberId, memberId);
		finOptionService.updateWallet(finOption);
	}

	public Result getWithdrawMinNum() {
		return success(F.me().cfg(WITHDRAWCOINMINNUM));
	}

	public Result getContractConfig() {
		HashMap<String, String> map = new HashMap<>(3);
		String conOpen = F.me().cfg(CONTRACT_OPEN);
		String contractFee = F.me().cfg(CONTRACT_FEE);
		String contractMarketPrice = F.me().cfg(CONTRACT_MARKET_PRICE);
		map.put("contractOpen", conOpen);
		map.put("contractFee", contractFee);
		map.put("contractMarketPrice", contractMarketPrice);
		return success(map);
	}

	public Result getInviteConfig() {
		HashMap<String, String> map = new HashMap<>(1);
		String inviteOpen = F.me().cfg(INVITEOPEN);
		map.put("inviteOpen", inviteOpen);
		return success(map);
	}

	/**
	 * 结算
	 *
	 * @param optionOrder 订单
	 * @param ctrlFlag    结果方向 true 赢 false 输
	 */
	@Transactional
	public void optionJieSuan(ContractOptionOrder optionOrder, boolean ctrlFlag) {
		FinOption finOption = F.me().getFinOption(optionOrder.getMemberId(), optionOrder.getBaseSymbol());
		if (finOption == null) {
			Member member = F.me().getMember(optionOrder.getMemberId());
			genOption(member);
		}
		String symbol = optionOrder.getSymbol();
		ContractOptionCoin contractOption1 = new ContractOptionCoin();
		contractOption1.setSymbol(symbol);
		ContractOptionCoin coin = optionCoinService.getOne(new QueryWrapper<>(contractOption1));
		if (coin == null) return;
		boolean flag = jiesuan(optionOrder, finOption, coin, ctrlFlag);
		if (flag) {
			//Y 需要返佣
			optionRecord(coin, optionOrder);
		}
		optionOrderService.updateById(optionOrder);
		finOptionService.updateWallet(finOption);
		optionCoinService.updateById(coin);

	}

	public void optionRecord(ContractOptionCoin optionCoin, ContractOptionOrder coo) {
		Member member = memberService.getById(coo.getMemberId());
		Long refereeId = member.getRefereeId();
		Member refereeMember = memberService.getById(refereeId);
		//用户不再
		if (refereeMember == null) return;
		FinOption finOption = F.me().getFinOption(refereeMember.getMemberId(), coo.getBaseSymbol());
		//没有钱包
		if (finOption == null) return;
		BigDecimal betAmount = coo.getBetAmount();
		BigDecimal rods = optionCoin.getRods();
//        BigDecimal oods = optionCoin.getOods();
//        BigDecimal num = betAmount.multiply(rods.divide(BigDecimal.valueOf(100))).multiply(oods.divide(BigDecimal.valueOf(100))); // 返佣金额
		BigDecimal num = betAmount.multiply(rods.divide(BigDecimal.valueOf(100))); // 返佣金额
//        BigDecimal num = optionCoin.getRods(); // 返佣金额
		//返佣为0
		optionRecordLevel(refereeId, coo.getBaseSymbol(), finOption, num);
		coo.setRecordNum(num);
		coo.setOrRecord("Y");
		finOptionService.updateWallet(finOption);


		//上上级用户
		Member refereeMember2 = memberService.getById(refereeMember.getRefereeId());
		//用户不再
		if (refereeMember2 == null) return;
		FinOption finOption2 = F.me().getFinOption(refereeMember2.getMemberId(), coo.getBaseSymbol());
		if (finOption2 == null) return;
		BigDecimal rodsTow = optionCoin.getRodsTow();
		BigDecimal num2 = betAmount.multiply(rodsTow.divide(BigDecimal.valueOf(100))); // 返佣金额
		optionRecordLevel(refereeMember2.getMemberId(), coo.getBaseSymbol(), finOption2, num2);
		coo.setRecordTowNum(num2);
		coo.setOrTowRecord("Y");
		finOptionService.updateWallet(finOption2);
	}

	public void optionRecordLevel(Long refereeId, String baseSymbol, FinOption finOption, BigDecimal num) {
		//返佣为0
		if (num.compareTo(BigDecimal.ZERO) < 1) return;
		BigDecimal usedPrice = finOption.getUsedPrice();
		finOption.setUsedPrice(usedPrice.add(num));
		F.me().saveCashflow(refereeId, WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONRECORD,
				num, baseSymbol, num, baseSymbol,
				BigDecimal.ZERO, baseSymbol,
				ItemCode.USED, baseSymbol, null, CashFlowTypeEnum.OPTIONRECORD.getValue(),
				usedPrice, finOption.getUsedPrice(),
				refereeId, refereeId);
	}

	/**
	 * 处理 钱包订单，系统盈利
	 *
	 * @param order     正在开奖的订单 update
	 * @param finOption 钱包  update
	 * @param coin      币种 update
	 * @param isCtrl    控制 输赢 null没用控制，true控制赢 false控制跌
	 */
	public boolean jiesuan(ContractOptionOrder order,
						   FinOption finOption, ContractOptionCoin coin, Boolean isCtrl) {
		//是否返佣
		boolean isRebate = false;

		//公共 必要的
		// 退回保证金
		if (finOption.getLockedPrice().compareTo(order.getBetAmount().add(order.getFee())) >= 0) {
			finOption.setLockedPrice(finOption.getLockedPrice().subtract(order.getBetAmount()));
			finOption.setUsedPrice(finOption.getUsedPrice().add(order.getBetAmount()));
		}

		//log.info("控制"+(isCtrl?"赢":"输"));

		if (null == isCtrl) {
			//平局 不收取手续费  退回
			F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONTUIHUI,
					order.getBetAmount(), order.getBaseSymbol(), order.getBetAmount(), order.getBaseSymbol(),
					BigDecimal.ZERO, order.getBaseSymbol(),
					ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONTUIHUI.getValue(),
					finOption.getUsedPrice().subtract(order.getBetAmount()), finOption.getUsedPrice(),
					order.getMemberId(), order.getMemberId());
			order.setRewardAmount(BigDecimal.ZERO);
			order.setResult(ContractOptionOrderResult.TIED.getCode());
			order.setWinFee(BigDecimal.ZERO);
		} else {
			//赢家有手续费以及抽水
			if (isCtrl) {
				// 扣除手续费  具体数额 1，2，3
				if (order.getFee().compareTo(BigDecimal.ZERO) > 0) { // 扣除开仓手续费
					finOption.setLockedPrice(finOption.getLockedPrice().subtract(order.getFee()));
					//finOption.setUsedPrice(finOption.getUsedPrice().subtract(order.getFee()));
					//增加记录
					F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OPTIONFEE,
							order.getFee(), order.getBaseSymbol(), order.getFee(), order.getBaseSymbol(),
							BigDecimal.ZERO, order.getBaseSymbol(),
							ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONFEE.getValue(),
							finOption.getUsedPrice().add(order.getFee()), finOption.getUsedPrice(),
							order.getMemberId(), order.getMemberId());

					// 平台手续费收益
					//temOption.setTotalPl(temOption.getTotalPl().add(order.getFee()));
					coin.setTotalProfit(coin.getTotalProfit().add(order.getFee()));
				}
				//系统赔钱 赔率为100
				BigDecimal reward = order.getBetAmount().multiply(coin.getOods().divide(BigDecimal.valueOf(100))).setScale(5, RoundingMode.DOWN);

				//平台收益
				//temOption.setTotalPl(temOption.getTotalPl().subtract(reward));
				coin.setTotalProfit(coin.getTotalProfit().subtract(reward));

				//计算抽水 百分比 千分之
				BigDecimal winFee = order.getWinFee();
				if (coin.getWinFeePercent().compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal usedPrice = finOption.getUsedPrice();
					finOption.setUsedPrice(usedPrice.subtract(winFee));
					//增加资产变更记录
					F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OPTIONCHOUSHUI,
							winFee, order.getBaseSymbol(), winFee, order.getBaseSymbol(),
							winFee, order.getBaseSymbol(),
							ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONCHOUSHUI.getValue(),
							usedPrice, finOption.getUsedPrice(),
							order.getMemberId(), order.getMemberId());
					coin.setTotalProfit(coin.getTotalProfit().add(winFee));
					//temOption.setTotalPl(temOption.getTotalPl().add(winFee));
				}
				//赢家
				finOption.setUsedPrice(finOption.getUsedPrice().add(reward));
				F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OPTIONY,
						reward, order.getBaseSymbol(), reward, order.getBaseSymbol(),
						winFee, order.getBaseSymbol(),
						ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONY.getValue(),
						finOption.getUsedPrice().subtract(reward), finOption.getUsedPrice(),
						order.getMemberId(), order.getMemberId());

				order.setRewardAmount(reward.subtract(winFee));
				order.setResult(ContractOptionOrderResult.WIN.getCode());
				order.setWinFee(winFee);
				isRebate = true;
			} else {
				//输家 不需要扣取手续费
				BigDecimal losePrice = order.getBetAmount().multiply(coin.getOous().divide(BigDecimal.valueOf(100)));
				finOption.setUsedPrice(finOption.getUsedPrice().subtract(losePrice));
				F.me().saveCashflow(order.getMemberId(), WalletBigType.OPTION, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OPTIONK,
						losePrice, order.getBaseSymbol(), losePrice, order.getBaseSymbol(),
						BigDecimal.ZERO, order.getBaseSymbol(),
						ItemCode.USED, order.getSymbol(), null, CashFlowTypeEnum.OPTIONK.getValue(),
						finOption.getUsedPrice().subtract(losePrice), finOption.getUsedPrice(),
						order.getMemberId(), order.getMemberId());
				//平台收益
				//temOption.setTotalPl(temOption.getTotalPl().add(shu));
				coin.setTotalProfit(coin.getTotalProfit().add(losePrice));

				order.setRewardAmount(losePrice.negate());
				order.setResult(ContractOptionOrderResult.LOSE.getCode());
				order.setWinFee(BigDecimal.ZERO);
			}
		}
		order.setStatus(ContractOptionOrderStatus.CLOSE.getCode());
		return isRebate;
	}

	public Result coinLoan(String token, Loan loan, String code) {

		Member member = (Member) redisUtil.get(token);
		boolean equals = "N".equals(F.me().cfg(SMSSENDOPEN));
		boolean equals2 = "N".equals(F.me().cfg(EMAILSENDOPEN));

		if (!equals && !equals2) {
			String msg = (String) redisUtil.get(SMS + member.getPhone());
			if (msg == null) {
				msg = (String) redisUtil.get(SMS + member.getEmail());
			}
			if (!StrUtil.equals(msg, code)) {
				return fail(ApiStatus.MSG_ERROR);
			}
		}

		loan.setMemberId(member.getMemberId());
		loan.setStatus("N").setCreateUser(1L);
		loanService.addLoan(loan);
		redisUtil.del(SMS + member.getPhone(), SMS + member.getEmail());
		try {
			WebSocketService.sendInfo(ADDLOANAPPLYSTRING, SYSTEMSOCKETSENDNAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success();
	}

	public Result coinLoanList(String token) {
		Member member = (Member) redisUtil.get(token);
		Loan loan = new Loan();
		loan.setDel("N").setMemberId(member.getMemberId());
		List<Loan> list = loanService.list(new QueryWrapper<>(loan));
		return Result.success(list);
	}
}







