package cn.stylefeng.guns.modular.chain.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
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
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.chain.service.CoinService;
import cn.stylefeng.guns.modular.chain.wrapper.CoinWrapper;
import cn.stylefeng.guns.modular.chain.entity.Coin;
import cn.stylefeng.guns.modular.chain.constant.CoinMap;

/**
 * 用户钱包地址控制器
 *
 * @author yaying.liu
 * @Date 2019-12-10 18:24:36
 */
@Controller
@RequestMapping("/coin")
public class CoinController extends BaseController {

    private String PREFIX = "/modular/chain/coin/";

     @Autowired
     private CoinService coinService;

    /**
     * 跳转到用户钱包地址首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "coin.html";
    }

    /**
     * 跳转到添加用户钱包地址
     */
    @RequestMapping("/coin_add")
    public String coinAdd() {
        return PREFIX + "coin_add.html";
    }

    /**
     * 跳转到修改用户钱包地址
     */
    @RequestMapping("/coin_edit")
    public String coinEdit(Long coinId, Model model) {
        Coin condition = this.coinService.getById(coinId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "coin_edit.html";
    }

    /**
     * 用户钱包地址详情
     */
    @RequestMapping(value = "/detail/{coinId}")
    @ResponseBody
    public Object detail(@PathVariable("coinId") Long coinId) {
        Coin entity = coinService.getById(coinId);
      //  CoinDto conditionDto = new CoinDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询用户钱包地址列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = coinService.selectByCondition(condition);
        Page wrapped = new CoinWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑用户钱包地址
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "coinId", dict = CoinMap.class)
    @ResponseBody
    public ResponseData edit(Coin coin) {
        coinService.updateById(coin);
        return SUCCESS_TIP;
    }

    /**
     * 添加用户钱包地址
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加用户钱包地址", key = "name", dict = CoinMap.class)
    @ResponseBody
    public ResponseData add(Coin coin, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (coin == null) {
            return ResponseData.error("参数不能为空");
        }
        this.coinService.addCoin(coin);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户钱包地址
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除用户钱包地址", key = "coinId", dict = CoinMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long coinId) {
        if (ToolUtil.isEmpty(coinId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.coinService.deleteCoin(coinId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{coinId}")
    @ResponseBody
    public Object content(@PathVariable("coinId") Long id) {
        Coin coin = coinService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(coin);
        return super.warpObject(new CoinWrapper(stringObjectMap));
    }
}