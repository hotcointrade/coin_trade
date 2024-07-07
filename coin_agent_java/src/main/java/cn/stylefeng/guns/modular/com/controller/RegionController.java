package cn.stylefeng.guns.modular.com.controller;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.com.service.RegionService;
import cn.stylefeng.guns.modular.com.wrapper.RegionWrapper;
import cn.stylefeng.guns.modular.com.entity.Region;
import cn.stylefeng.guns.modular.com.constant.RegionMap;

/**
 * 地区控制器
 *
 * @author yaying.liu
 * @Date 2019-12-06 11:32:41
 */
@Controller
@RequestMapping("/region")
public class RegionController extends BaseController {

    private String PREFIX = "/modular/com/region/";

     @Autowired
     private RegionService regionService;

    /**
     * 跳转到地区首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "region.html";
    }

    /**
     * 跳转到添加地区
     */
    @RequestMapping("/region_add")
    public String regionAdd() {
        return PREFIX + "region_add.html";
    }

    /**
     * 跳转到修改地区
     */
    @RequestMapping("/region_edit")
    public String regionEdit(Long regionId, Model model) {
        Region condition = this.regionService.getById(regionId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "region_edit.html";
    }

    /**
     * 地区详情
     */
    @RequestMapping(value = "/detail/{regionId}")
    @ResponseBody
    public Object detail(@PathVariable("regionId") Long regionId) {
        Region entity = regionService.getById(regionId);
      //  RegionDto conditionDto = new RegionDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询地区列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = regionService.selectByCondition(condition);
        Page wrapped = new RegionWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑地区
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "regionId", dict = RegionMap.class)
    @ResponseBody
    public ResponseData edit(Region region) {
        regionService.updateById(region);
        return SUCCESS_TIP;
    }

    /**
     * 修改状态
     * @param status
     * @param regionId
     * @return
     */
    @RequestMapping("/status/{status}")
    @ResponseBody
    public ResponseData status(@PathVariable("status") String status,@RequestParam Long regionId) {
        Region entity = regionService.getById(regionId);
        entity.setStatus(status);
        regionService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加地区
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加地区", key = "name", dict = RegionMap.class)
    @ResponseBody
    public ResponseData add(Region region, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (region == null) {
            return ResponseData.error("参数不能为空");
        }
        this.regionService.addRegion(region);
        return SUCCESS_TIP;
    }

    /**
     * 删除地区
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除地区", key = "regionId", dict = RegionMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long regionId) {
        if (ToolUtil.isEmpty(regionId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.regionService.deleteRegion(regionId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{regionId}")
    @ResponseBody
    public Object content(@PathVariable("regionId") Long id) {
        Region region = regionService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(region);
        return super.warpObject(new RegionWrapper(stringObjectMap));
    }

    @RequestMapping("/getTypeList")
    @ResponseBody
    public List<Map<String, String>> getLevel() {
        List<Region> list = regionService.list();
        List<Map<String,String>> result = new ArrayList<>();
        for (Region entity : list) {
            if(entity.getLevelType().longValue()<3)
            {
                Map map = new HashMap();
                map.put("code", entity.getRegionId());
                map.put("value", entity.getName());
                result.add(map);
            }

        }
        return result;
    }


}