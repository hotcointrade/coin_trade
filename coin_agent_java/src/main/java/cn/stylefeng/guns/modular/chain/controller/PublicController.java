package cn.stylefeng.guns.modular.chain.controller;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.util.GoogleGenerator;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.chain.service.PublicService;
import cn.stylefeng.guns.modular.chain.wrapper.PublicWrapper;
import cn.stylefeng.guns.modular.chain.entity.Public;
import cn.stylefeng.guns.modular.chain.constant.PublicMap;

/**
 * 公账提币控制器
 *
 * @author yaying.liu
 * @Date 2020-01-17 18:55:07
 */
@Controller
@RequestMapping("/public")
public class PublicController extends BaseController {

    private String PREFIX = "/modular/chain/public/";

     @Autowired
     private PublicService publicService;

    /**
     * 跳转到公账提币首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "public.html";
    }

    /**
     * 跳转到添加公账提币
     */
    @RequestMapping("/public_add")
    public String publicAdd() {
        return PREFIX + "public_add.html";
    }

    /**
     * 跳转到修改公账提币
     */
    @RequestMapping("/public_edit")
    public String publicEdit(Long publicId, Model model) {
        Public condition = this.publicService.getById(publicId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "public_edit.html";
    }

    /**
     * 公账提币详情
     */
    @RequestMapping(value = "/detail/{publicId}")
    @ResponseBody
    public Object detail(@PathVariable("publicId") Long publicId) {
        Public entity = publicService.getById(publicId);
      //  PublicDto conditionDto = new PublicDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询公账提币列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition
            ,@RequestParam(required = false) String timeLimit
            ,@RequestParam(required = false) String txHash
            ,@RequestParam(required = false) String type
            ,@RequestParam(required = false) String toAddress
            ,@RequestParam(required = false) String status
    ){
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Map<String,Object> map=new HashedMap();
        map.put("condition",condition);
        map.put("txHash",txHash);
        map.put("type",type);
        map.put("toAddress",toAddress);
        map.put("status",status);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        //根据条件查询
        Page<Map<String, Object>> result = publicService.selectByCondition(map);
        Page wrapped = new PublicWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    @RequestMapping("/total")
    @ResponseBody
    public Object total(@RequestParam(required = false) String condition
            ,@RequestParam(required = false) String timeLimit
            ,@RequestParam(required = false) String txHash
            ,@RequestParam(required = false) String type
            ,@RequestParam(required = false) String toAddress
            ,@RequestParam(required = false) String status
    ){
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Map<String,Object> map=new HashedMap();
        map.put("condition",condition);
        map.put("txHash",txHash);
        map.put("type",type);
        map.put("toAddress",toAddress);
        map.put("status",status);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        return publicService.getBaseMapper().total(map);
    }



    /**
     * 编辑公账提币
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "publicId", dict = PublicMap.class)
    @ResponseBody
    public ResponseData edit(Public entity) {
        publicService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加公账提币
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加公账提币", key = "name", dict = PublicMap.class)
    @ResponseBody
    public ResponseData add(Public entity, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (entity == null) {
            return ResponseData.error("参数不能为空");
        }
        if (StringUtils.isBlank(entity.getCode())){
            return ResponseData.error("请输入谷歌验证码");
        }
        GoogleGenerator ga = new GoogleGenerator();
        long time = System.currentTimeMillis ();
        boolean flag = ga.check_code(F.me().getSysConfigValueByCode("GOOGLE_KEY"), entity.getCode(), time);
        if (!flag){
            return ResponseData.error("输入的谷歌验证码有误");
        }
        this.publicService.addPublic(entity);
        return SUCCESS_TIP;
    }

    public static void main(String[] args) {
        System.out.println(GoogleGenerator.generateSecretKey());
    }

    /**
     * 删除公账提币
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除公账提币", key = "publicId", dict = PublicMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long publicId) {
        if (ToolUtil.isEmpty(publicId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.publicService.deletePublic(publicId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{publicId}")
    @ResponseBody
    public Object content(@PathVariable("publicId") Long id) {
        Public entity = publicService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(entity);
        return super.warpObject(new PublicWrapper(stringObjectMap));
    }

    @RequestMapping(value = "/accountBalance")
    @ResponseBody
    public Object accountBalance(String type) {
        return publicService.accountBalance(type);
    }


    @RequestMapping(value = "/findMainAccount")
    @ResponseBody
    public Object findMainAccount(String type) {
        return publicService.findMainAccount(type);
    }



}