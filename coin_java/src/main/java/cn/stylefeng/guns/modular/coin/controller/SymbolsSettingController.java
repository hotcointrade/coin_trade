package cn.stylefeng.guns.modular.coin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import cn.stylefeng.guns.modular.coin.entity.SymbolsSetting;
import cn.stylefeng.guns.modular.coin.service.SpotService;
import cn.stylefeng.guns.modular.coin.service.SwapService;
import cn.stylefeng.guns.modular.coin.service.SymbolsSettingService;
import cn.stylefeng.guns.modular.coin.wrapper.SpotWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/symbolsSetting")
public class SymbolsSettingController extends BaseController {

    private String PREFIX = "/modular/coin/symbolsSetting/";

     @Autowired
     private SymbolsSettingService symbolsSettingService;
    @Autowired
    private RedisUtil redisUtil;

     @Resource
     private SpotService spotService;
     @Resource
     private SwapService swapService;

    /**
     * 跳转到现货交易对首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "symbols_setting.html";
    }

    /**
     * 跳转到添加现货交易对
     */
    @RequestMapping("/symbols_setting_add")
    public String spotAdd() {
        return PREFIX + "symbols_setting_add.html";
    }

    /**
     * 跳转到修改现货交易对
     */
    @RequestMapping("/symbols_setting_edit")
    public String spotEdit(Long spotId, Model model) {
        SymbolsSetting condition = this.symbolsSettingService.getById(spotId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "symbols_setting_edit.html";
    }

    /**
     * 现货交易对详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        SymbolsSetting entity = symbolsSettingService.getById(id);
        return entity;
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
        Page<Map<String, Object>> result = symbolsSettingService.selectByCondition(condition,beginTime,endTime);
//        Page wrapped = new SpotWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(result);
    }

    /**
     * 编辑现货交易对
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(SymbolsSetting spot) {
        if (spot.getId() == null || spot.getId() <=0){
            return ResponseData.error("修改失败");
        }
        if (BigDecimal.ZERO.compareTo(spot.getP8LossRatio())==0) {
            Spot queue = new Spot();
            queue.setSymbol(spot.getSymbol());
            queue.setType("0");
            queue = this.spotService.getOne(new QueryWrapper<>(queue));
            if (queue == null) return ResponseData.error("该币种不存在");
        }else{
            Swap queue = new Swap();
            queue.setSymbol(spot.getSymbol());
            queue.setType("0");
            queue = this.swapService.getOne(new QueryWrapper<>(queue));
            if (queue == null) return ResponseData.error("该币种不存在");
        }
//        SymbolsSetting symbolsSetting = new SymbolsSetting();
//        symbolsSetting.setDay(spot.getDay());
//        symbolsSetting.setSymbol(spot.getSymbol());
//        int count = this.symbolsSettingService.count(new QueryWrapper<>(symbolsSetting).notIn("id",spot.getId()));
//        if (count >0)  return ResponseData.error("选中的星期已存在");
        symbolsSettingService.updateById(spot);
        return SUCCESS_TIP;
    }
    /**
     * 删除合约交易对
     */
    @RequestMapping("/delete")
    // @BussinessLog(value = "删除合约交易对", key = "swapId", dict = SwapMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        SymbolsSetting spot = symbolsSettingService.getById(id);
        boolean b = BigDecimal.ZERO.compareTo(spot.getP8LossRatio())!=0 ;
//        String redisStr=b?Constant.KINE_PERPETUAL:Constant.KINE;
//        String tradeStr =   b?Constant.TRADE_PERPETUAL:Constant.TRADE;
//        String depthStr =   b?Constant.DEPTH_PERPETUAL:Constant.DEPTH;
//        String minuteStr =  b?"KINE_PERPETUAL_MINUTE":"KINE_MINUTE";
//
//        String key = redisStr + spot.getSymbol()+Constant._NEW;
//        redisUtil.del(key);
//
//        key = redisStr + spot.getSymbol()+ "_1day"+Constant.JIA;
//        redisUtil.del(key);
//        //删除数据
//        for (ProConst.PeriodType periodType : ProConst.PeriodType.values()){
//            key = redisStr + spot.getSymbol() + "_" + periodType.code;
//            redisUtil.del(key);
//        }
//
//        key = tradeStr+spot.getSymbol();
//        redisUtil.del(key);
//        key = depthStr+spot.getSymbol();
//        redisUtil.del(key);
//        key = depthStr+spot.getSymbol()+Constant.JIA;
//        redisUtil.del(key);
//        key = minuteStr+":"+spot.getSymbol()+":";
//        redisUtil.delPreFixKey(key);
        symbolsSettingService.removeById(id);
        return SUCCESS_TIP;
    }
    /**
     * 添加现货交易对
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(SymbolsSetting spot, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (spot == null) {
            return ResponseData.error("参数不能为空");
        }
        if (BigDecimal.ZERO.compareTo(spot.getP8LossRatio())==0) {
            Spot queue = new Spot();
            queue.setSymbol(spot.getSymbol());
            queue.setType("0");
            queue = this.spotService.getOne(new QueryWrapper<>(queue));
            if (queue == null) return ResponseData.error("该币种不存在");
        }else{
            Swap queue = new Swap();
            queue.setSymbol(spot.getSymbol());
            queue.setType("0");

            queue = this.swapService.getOne(new QueryWrapper<>(queue));
            if (queue == null) return ResponseData.error("该币种不存在");
        }
        SymbolsSetting symbolsSetting = new SymbolsSetting();
        //symbolsSetting.setDay(spot.getDay());
        symbolsSetting.setSymbol(spot.getSymbol());
        symbolsSetting.setP8LossRatio(spot.getP8LossRatio());
        int count = this.symbolsSettingService.count(new QueryWrapper<>(symbolsSetting));
        if (count >0){
            return ResponseData.error("选中的币种已存在");
        }
        boolean b = BigDecimal.ZERO.compareTo(spot.getP8LossRatio())!=0;
//        String redisStr = b?Constant.KINE_PERPETUAL:Constant.KINE;
//        String tradeStr =   b?Constant.TRADE_PERPETUAL:Constant.TRADE;
//        String depthStr =   b?Constant.DEPTH_PERPETUAL:Constant.DEPTH;
//        String minuteStr =  b?"KINE_PERPETUAL_MINUTE":"KINE_MINUTE";
//
//        String key = redisStr + spot.getSymbol()+Constant._NEW;
//        redisUtil.del(key);
//
//        key = redisStr + spot.getSymbol()+ "_1day"+Constant.JIA;
//        redisUtil.del(key);
//        //删除数据
//        for (ProConst.PeriodType periodType : ProConst.PeriodType.values()){
//            key = redisStr + spot.getSymbol() + "_" + periodType.code;
//            redisUtil.del(key);
//        }
//        key = tradeStr+spot.getSymbol();
//        redisUtil.del(key);
//        key = depthStr+spot.getSymbol();
//        redisUtil.del(key);
//        key = depthStr+spot.getSymbol()+Constant.JIA;
//        redisUtil.del(key);
//        key = minuteStr+":"+spot.getSymbol()+":";
//        redisUtil.delPreFixKey(key);
        //生成事件，使用队列 kline处理
        redisUtil.publish("robotMatchKline",spot);
        return ResponseData.success("预计5分钟,请稍等,正在生成......");
    }



    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{id}")
    @ResponseBody
    public Object content(@PathVariable("id") Long id) {
        SymbolsSetting spot = symbolsSettingService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(spot);
        return super.warpObject(new SpotWrapper(stringObjectMap));
    }
}
