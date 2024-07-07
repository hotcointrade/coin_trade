package cn.stylefeng.guns.modular.coin.controller;

import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.entity.FinMining;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.service.CurrencyService;
import cn.stylefeng.guns.modular.fin.service.FinMiningService;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.coin.service.SpotService;
import cn.stylefeng.guns.modular.coin.wrapper.SpotWrapper;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.constant.SpotMap;

/**
 * 现货交易对控制器
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
@Controller
@RequestMapping("/spot")
public class SpotController extends BaseController {

    private String PREFIX = "/modular/coin/spot/";

     @Autowired
     private SpotService spotService;

     @Autowired
     private WalletService walletService;

     @Autowired
     private CurrencyService currencyService;

     @Autowired
     private MemberService memberService;
     private FinMiningService finMiningService;

    /**
     * 跳转到现货交易对首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "spot.html";
    }

    /**
     * 跳转到添加现货交易对
     */
    @RequestMapping("/spot_add")
    public String spotAdd() {
        return PREFIX + "spot_add.html";
    }

    /**
     * 跳转到修改现货交易对
     */
    @RequestMapping("/spot_edit")
    public String spotEdit(Long spotId, Model model) {
        Spot condition = this.spotService.getById(spotId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "spot_edit.html";
    }

    /**
     * 现货交易对详情
     */
    @RequestMapping(value = "/detail/{spotId}")
    @ResponseBody
    public Object detail(@PathVariable("spotId") Long spotId) {
        Spot entity = spotService.getById(spotId);
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
        Spot entity = spotService.getById(id);
        if (entity != null && (entity.getSymbol().equals("USDT-OMNI/USDT")
                ||entity.getSymbol().equals("USDT/USDT")
                || entity.getSymbol().equals("USDT-ERC20/USDT")
                ||entity.getSymbol().equals("USDT-TRC20/USDT") ) && "N".equals(status)){
            return ResponseData.error("该币种不能禁用");
        }
        entity.setStatus(status);
        spotService.updateById(entity);
        return SUCCESS_TIP;
    }


    /**
     * 查询现货交易对列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition, @RequestParam(required = false) String timeLimit) {
        //根据条件查询
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Page<Map<String, Object>> result = spotService.selectByCondition(condition,beginTime,endTime);
        Page wrapped = new SpotWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑现货交易对
     */
    @RequestMapping("/edit")
    //@BussinessLog(value = "编辑参数", key = "spotId", dict = SpotMap.class)
    @ResponseBody
    public ResponseData edit(Spot spot) {
        spotService.updateById(spot);
        return SUCCESS_TIP;
    }

    /**
     * 添加现货交易对
     */
    @RequestMapping("/add")
  //  @BussinessLog(value = "添加现货交易对", key = "name", dict = SpotMap.class)
    @ResponseBody
    public ResponseData add(Spot spot, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (spot == null) {
            return ResponseData.error("参数不能为空");
        }
        if (!spot.getSymbol().contains("/")){
            return ResponseData.error("币种格式有误，请以BTC/USDT这样格式来");
        }
        Spot spot1 = new Spot();
        spot1.setSymbol(spot.getSymbol());
        spot1.setDel("N");
        int count = this.spotService.count(new QueryWrapper<>(spot1));
        if (count >0){
            return ResponseData.error("该币种已存在");
        }
        this.spotService.addSpot(spot);
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
         Spot spot =  this.spotService.getById(spotId);
         if (spot != null && ("USDT-OMNI/USDT".equals(spot.getCode())
                || "USDT-ERC20/USDT".equals(spot.getCode())
                ||"USDT-TRC20/USDT".equals(spot.getCode()) )){
            return ResponseData.error("该币种不能删除");
        }
        this.spotService.deleteSpot(spotId);
         //删除对应的币种钱包
        ThreadPoolUtil.execute(
                ()->{
                    updateWalletAndCurrency("delete",spot.getSymbol());
                }
        );
        return SUCCESS_TIP;
    }

    private void updateWalletAndCurrency(String type,String coin) {
        Spot spot = new Spot();
        coin = coin.split("/")[0];
        if ("delete".equals(type)) {


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

        }else if ("add".equals(type) && StringUtils.isNotBlank(coin)){
            Member entity = new Member();
            entity.setDel("N");
            List<Member> memberList = memberService.list(new QueryWrapper<>(entity));
            for (Member m :memberList){
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
                FinMining finMining = F.me().getMining(m.getMemberId(),coin);
                if (finMining == null){
                    finMining = new FinMining();
                    finMining.setMemberId(m.getMemberId())
                            .setType(coin)
                            .setCreateUser(m.getMemberId());
                    finMining.setCreateTime(new Date());
                    finMining.setUpdateUser(m.getMemberId());
                    finMiningService.save(finMining);
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
        Spot spot = spotService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(spot);
        return super.warpObject(new SpotWrapper(stringObjectMap));
    }
}
