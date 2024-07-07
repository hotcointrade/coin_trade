package cn.stylefeng.guns.modular.coin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionCoin;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionOrder;
import cn.stylefeng.guns.modular.coin.service.ContractOptionCoinService;
import cn.stylefeng.guns.modular.coin.service.ContractOptionOrderService;
import cn.stylefeng.guns.modular.coin.wrapper.OptionWrapper;
import cn.stylefeng.guns.modular.coin.wrapper.SpotWrapper;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.service.CurrencyService;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.MemberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 期权交易对控制器
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
@Controller
@RequestMapping("/contractOptionOrder")
public class ContractOptionOrderController extends BaseController {

    private String PREFIX = "/modular/coin/option/";

    @Resource
    RedisUtil redisUtil;
    @Autowired
    private ContractOptionOrderService optionService;

     @Autowired
     private WalletService walletService;

     @Autowired
     private CurrencyService currencyService;

     @Autowired
     private MemberService memberService;

     @Autowired
     private HomeService homeService;


    /**
     * 跳转到期权交易对首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "contract_option_order.html";
    }

    /**
     * 跳转到添加期权交易对
     */
    @RequestMapping("/option_add")
    public String spotAdd() {
        return PREFIX + "contract_option_order_add.html";
    }

    /**
     * 跳转到修改期权交易对
     */
    @RequestMapping("/option_edit")
    public String spotEdit(Long spotId, Model model) {
        ContractOptionOrder condition = this.optionService.getById(spotId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "contract_option_coin_edit.html";
    }

    /**
     * 期权交易对详情
     */
    @RequestMapping(value = "/detail/{spotId}")
    @ResponseBody
    public Object detail(@PathVariable("spotId") Long spotId) {
        ContractOptionOrder entity = optionService.getById(spotId);
      //  SpotDto conditionDto = new SpotDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }
    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    @RequestMapping("/status/{status}")
    @ResponseBody
    public ResponseData status(@PathVariable("status") String status,@RequestParam Long id) {
        ContractOptionOrder entity = optionService.getById(id);
        if (entity != null && (entity.getSymbol().equals("USDT-OMNI/USDT")
                || entity.getSymbol().equals("USDT-ERC20/USDT")
                ||entity.getSymbol().equals("USDT-TRC20/USDT") ) && "N".equals(status)){
            return ResponseData.error("该币种不能禁用");
        }
        //entity.setStatus(status);
        optionService.updateById(entity);
        return SUCCESS_TIP;
    }
    /**
     * 修改状态
     * @param id
     * @param id
     * @return
     */
    @RequestMapping("/back")
    @ResponseBody
    public ResponseData back(@RequestParam Long id) {
        ContractOptionOrder entity = optionService.getById(id);
        if (entity == null || entity.getResult()!=2 || entity.getBack()==1){
            return ResponseData.error("该订单结果不为负或已赔付过");
        }
        homeService.doBack(entity);
        entity.setBack(1);
        optionService.updateById(entity);
        return SUCCESS_TIP;
    }
    /**
     * 修改状态
     * @param id
     * @param id
     * @return
     */
    @RequestMapping("/jiesuan")
    @ResponseBody
    public ResponseData jiesuan(@RequestParam Long id,@RequestParam String ctrlNum) {
        ContractOptionOrder entity = optionService.getById(id);
        if (entity == null || entity.getResult()!=0 ){
            return ResponseData.error("该订单已结算");
        }
        homeService.optionJieSuan(entity,"Y".equalsIgnoreCase(ctrlNum));
        return SUCCESS_TIP;
    }


    /**
     * 查询现货交易对列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false)String symbol,
                       @RequestParam(required = false)BigDecimal betAmount,
                       @RequestParam(required = false)String account,
                       @RequestParam(required = false)BigDecimal rewardAmount) {
//        Member member = (Member) redisUtil.get(token);
        Long memberId = null;
        if(StringUtils.isNotBlank(account)){
            Member oneByAccount = memberService.getOneByAccount(account);
            if(oneByAccount!=null){
                memberId = oneByAccount.getMemberId();
            }else{
                return LayuiPageFactory.createPageInfo(LayuiPageFactory.defaultPage());

            }
        }
        Page<Map<String, Object>> result = optionService.selectByCondition( symbol, memberId, betAmount, rewardAmount);
        Page wrapped = new OptionWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 获取当前币种指定期数ID的参与记录
     * @param
     * @param symbol
     * @param optionId
     * @return
     */
    @RequestMapping("current")
    public ResponseData current(@RequestHeader("token") String token,String symbol, /* 交易对符号*/
                                Long optionId /* 参与对象*/) {
        Member member = (Member) redisUtil.get(token);
        ContractOptionOrder contractOptionOrder = new ContractOptionOrder();
        contractOptionOrder.setSymbol(symbol);
        contractOptionOrder.setOptionId(optionId);
        contractOptionOrder.setMemberId(member.getMemberId());
        List<ContractOptionOrder> orderList = optionService.list(new QueryWrapper<>(contractOptionOrder));
        return ResponseData.success(orderList);
    }
    /**
     * 获取当前币种历史参与记录

     * @param symbol
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("history")
    public ResponseData history(@RequestHeader("token") String token,
                                 @RequestParam(value = "symbol" ,required = false) String symbol,
                                 @RequestParam(value = "pageNo" ,required = false) int pageNo,
                                 @RequestParam(value = "pageSize" ,required = false) int pageSize
    ) {
        Member member = (Member) redisUtil.get(token);
        ContractOptionOrder contractOptionOrder = new ContractOptionOrder();
        contractOptionOrder.setSymbol(symbol);
        Page<Map<String, Object>> list = optionService.selectByCondition(symbol,member.getMemberId(), null, null);
        return ResponseData.success(list);
    }

    /**
     * 编辑现货交易对
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "spotId", dict = SpotMap.class)
    @ResponseBody
    public ResponseData edit(ContractOptionOrder spot) {
        optionService.updateById(spot);
        return SUCCESS_TIP;
    }

    /**
     * 添加现货交易对
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加现货交易对", key = "name", dict = SpotMap.class)
    @ResponseBody
    public ResponseData add(ContractOptionOrder spot, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (spot == null) {
            return ResponseData.error("参数不能为空");
        }
        if (!spot.getSymbol().contains("/")){
            return ResponseData.error("币种格式有误，请以BTC/USDT这样格式来");
        }
        ContractOptionOrder spot1 = new ContractOptionOrder();
        spot1.setSymbol(spot.getSymbol());
        //spot1.setDel("N");
        int count = this.optionService.count(new QueryWrapper<>(spot1));
        if (count >0){
            return ResponseData.error("该币种已存在");
        }
        this.optionService.addSpot(spot);
        ThreadPoolUtil.execute(
                ()->{
                    updateWalletAndCurrency("add",spot.getSymbol());
                }
        );
        return SUCCESS_TIP;
    }

    /**
     * 删除现货交易对
     */
    @RequestMapping("/delete")
   // @BussinessLog(value = "删除现货交易对", key = "spotId", dict = SpotMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long spotId) {
        if (ToolUtil.isEmpty(spotId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ContractOptionOrder spot =  this.optionService.getById(spotId);
        //if (spot != null && (spot.getCode().equals("USDT-OMNI/USDT")
        //       || spot.getCode().equals("USDT-ERC20/USDT")
        //       ||spot.getCode().equals("USDT-TRC20/USDT") )){
        //   return ResponseData.error("该币种不能删除");
        //
        this.optionService.deleteSpot(spotId);
         //删除对应的币种钱包
        ThreadPoolUtil.execute(
                ()->{
                    updateWalletAndCurrency("delete",null);
                }
        );
        return SUCCESS_TIP;
    }

    private void updateWalletAndCurrency(String type,String coin) {
        ContractOptionOrder spot = new ContractOptionOrder();
        if ("delete".equals(type)) {
            List<ContractOptionOrder> spotList = this.optionService.list(new QueryWrapper<>(spot));
            for (ContractOptionOrder entity : spotList) {
                coin = entity.getSymbol().split("/")[0];
                Wallet wallet = new Wallet();
                wallet.setType(coin);
                wallet.setDel("N");
                List<Wallet> walletList = walletService.list(new QueryWrapper<>(wallet));
                for (Wallet wallet1 : walletList) {
                    wallet1.setDel("Y");
                    wallet1.setUpdateTime(new Date());
                    wallet1.setUpdateUser(1l);
                    walletService.updateById(wallet1);
                }
                Currency currency = new Currency();
                currency.setDel("N");
                currency.setType(coin);
                List<Currency> currencyList = currencyService.list(new QueryWrapper<>(currency));
                for (Currency currency1 : currencyList) {
                    currency1.setDel("Y");
                    currency1.setUpdateTime(new Date());
                    currency1.setUpdateUser(1l);
                    currencyService.updateById(currency1);
                }
            }
        }else if ("add".equals(type) && StringUtils.isNotBlank(coin)){
            Member entity = new Member();
            entity.setDel("Y");
            List<Member> memberList = memberService.list(new QueryWrapper<>(entity));
            for (Member m :memberList){
                coin = coin.split("/")[0];
               Wallet wallet = F.me().getWallet(m.getMemberId(),coin);
               if (wallet == null){
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
               Currency currency = F.me().getCurrency(m.getMemberId(),coin);
               if (currency == null){
                   currency = new Currency();
                   currency.setMemberId(m.getMemberId())
                           .setType(coin)
                           .setCreateUser(m.getMemberId());
                   currency.setCreateTime(new Date());
                   currency.setUpdateUser(m.getMemberId());
                   currencyService.save(currency);
               }
            }
        }
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{spotId}")
    @ResponseBody
    public Object content(@PathVariable("spotId") Long id) {
        ContractOptionOrder spot = optionService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(spot);
        return super.warpObject(new SpotWrapper(stringObjectMap));
    }
}
