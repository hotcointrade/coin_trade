package cn.stylefeng.guns.modular.e.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.common.page.LayuiPageInfo;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import cn.stylefeng.guns.modular.coin.service.SwapService;
import cn.stylefeng.guns.modular.coin.wrapper.SwapWrapper;
import cn.stylefeng.guns.modular.fin.entity.Contract;
import cn.stylefeng.guns.modular.fin.service.ContractService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合约交易对控制器
 *
 * @author yaying.liu
 * @since 2020-08-25 10:49:35
 */
@Controller
@RequestMapping("/market")
public class MarketController extends BaseController {

    private String PREFIX = "/modular/e/market/";

    @Autowired
    private SwapService swapService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ContractService contractService;

    @Resource
    private MemberService memberService;

    /**
     * 行情首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "market.html";
    }

    /**
     * 编辑页面
     * 1一口价,    2价格涨跌,  3比例涨跌
     */
    @RequestMapping("/market_edit")
    public String marketEdit(Long swapId, Model model) {
        Swap condition = this.swapService.getById(swapId);
        model.addAttribute(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "market_edit.html";
    }

    /**
     * 编辑页面-时间区间
     */
    @RequestMapping("/market_date_rate_edit")
    public String marketDateRateEdit(Long swapId, Model model) {
        Swap condition = this.swapService.getById(swapId);
        model.addAttribute(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "market_date_rate_edit.html";
    }

    /**
     * 查询合约,和行情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail(Long swapId, Integer type) {
        Swap entity = swapService.getById(swapId);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(entity);
        //调整价格回显
        String price = "";
        String key;
        switch (type) {
            case 1:
                key = Constant.MARKET_ONE.replace("$symbol$", entity.getSymbol());
                price = redisUtil.getStr(key);
                break;
            case 2:
                key = Constant.MARKET_PRICE.replace("$symbol$", entity.getSymbol());
                price = redisUtil.getStr(key);
                break;
            case 3:
                key = Constant.MARKET_RATE.replace("$symbol$", entity.getSymbol());
                price = redisUtil.getStr(key);
                break;
        }
        stringObjectMap.put("price", price);
        stringObjectMap.put("type", type);
        setMarket(stringObjectMap);
        return stringObjectMap;
    }

    /**
     * @param swapId 交易对
     * @param price  价格
     * @param type   1一口价,    2价格涨跌,  3比例涨跌
     * @return
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Object edit(@RequestParam String swapId, @RequestParam BigDecimal price, @RequestParam String type) {
        Swap swap = swapService.getById(swapId);
        String symbol = swap.getSymbol();
        String key1 = Constant.MARKET_ONE.replace("$symbol$", symbol);
        String key2 = Constant.MARKET_PRICE.replace("$symbol$", symbol);
        String key3 = Constant.MARKET_RATE.replace("$symbol$", symbol);
        redisUtil.del(key1, key2, key3);
        switch (type) {
            case "1":
                redisUtil.set(key1, price.toString());
                break;
            case "2":
                redisUtil.set(key2, price.toString());
                break;
            case "3":
                if (price.compareTo(BigDecimal.valueOf(-5)) < 0 ||
                        price.compareTo(BigDecimal.valueOf(5)) > 0) {
                    return ResponseData.error("范围只能在-5 ~ 5区间");
                }
                redisUtil.set(key3, price.toString());
                break;
            default:
                return ResponseData.error("type错误");
        }

        return SUCCESS_TIP;
    }

    /**
     * 合约行情列表
     */
    @RequestMapping("reset")
    @ResponseBody
    public Object reset(Long swapId) {
        Swap swap = swapService.getById(swapId);
        String symbol = swap.getSymbol();
        String marketOne = Constant.MARKET_ONE.replace("$symbol$", symbol);
        String marketPrice = Constant.MARKET_PRICE.replace("$symbol$", symbol);
        String marketRate = Constant.MARKET_RATE.replace("$symbol$", symbol);
        String marketRateDate = Constant.MARKET_RATE_DATE.replace("$symbol$", symbol);
        redisUtil.del(marketOne, marketPrice, marketRate, marketRateDate);
        return SUCCESS_TIP;
    }

    /**
     * 合约行情列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list() {
        Page<Map<String, Object>> result = swapService.selectByCondition(null, null, null);
        Page<Map<String, Object>> wrapped = new SwapWrapper(result).wrap();
        wrapped.getRecords().forEach(item -> setMarket(item));
        LayuiPageInfo pageInfo = LayuiPageFactory.createPageInfo(wrapped);
        return pageInfo;
    }


    /**
     * 设置行情数据
     *
     * @param item
     */
    private void setMarket(Map<String, Object> item) {
        String symbol = (String) item.get("symbol");
        String marketOne = redisUtil.getStr(Constant.MARKET_ONE.replace("$symbol$", symbol));
        String marketPrice = redisUtil.getStr(Constant.MARKET_PRICE.replace("$symbol$", symbol));
        String marketRate = redisUtil.getStr(Constant.MARKET_RATE.replace("$symbol$", symbol));
        String marketRateDate = redisUtil.getStr(Constant.MARKET_RATE_DATE.replace("$symbol$", symbol));
        item.put("marketOne", marketOne);
        item.put("marketPrice", marketPrice);
        item.put("marketRate", marketRate);
        item.put("marketRateDate", marketRateDate);
    }

}
