package cn.stylefeng.guns.modular.promotion.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HtmlUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.promotion.constant.DeclaresMap;
import cn.stylefeng.guns.modular.promotion.entity.Declares;
import cn.stylefeng.guns.modular.promotion.service.DeclaresService;
import cn.stylefeng.guns.modular.promotion.wrapper.DeclaresWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 用户协议控制器
 *
 * @author yaying.liu
 * @Date 2019-10-12 19:31:14
 */
@Controller
@RequestMapping("/declares")
public class DeclaresController extends BaseController {

    private String PREFIX = "/modular/promotion/declares/";

     @Autowired
     private DeclaresService declaresService;

    /**
     * 跳转到用户协议首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "declares.html";
    }

    /**
     * 跳转到添加用户协议
     */
    @RequestMapping("/declares_add")
    public String declaresAdd() {
        return PREFIX + "declares_add.html";
    }

    /**
     * 跳转到修改用户协议
     */
    @RequestMapping("/declares_edit")
    public String declaresEdit(Long declaresId, Model model) {
        Declares condition = this.declaresService.getById(declaresId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "declares_edit.html";
    }

    /**
     * 用户协议详情
     */
    @RequestMapping(value = "/detail/{declaresId}")
    @ResponseBody
    public Object detail(@PathVariable("declaresId") Long declaresId) {
        Declares entity = declaresService.getById(declaresId);
      //  DeclaresDto conditionDto = new DeclaresDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询用户协议列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = declaresService.selectByCondition(condition);
        Page wrapped = new DeclaresWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑用户协议
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "declaresId", dict = DeclaresMap.class)
    @ResponseBody
    public ResponseData edit(Declares declares) {
        Declares entity= declaresService.getById(declares.getDeclareId());
        //对传入数据进行反转义处理
        String tempContent = declares.getContent().replace("& ", "&");
        tempContent = HtmlUtil.unescape(tempContent);
        entity.setContent(tempContent);
        declaresService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加用户协议
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加用户协议", key = "name", dict = DeclaresMap.class)
    @ResponseBody
    public ResponseData add(Declares declares, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (declares == null) {
            return ResponseData.error("参数不能为空");
        }
        this.declaresService.addDeclares(declares);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户协议
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除用户协议", key = "declaresId", dict = DeclaresMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long declaresId) {
        if (ToolUtil.isEmpty(declaresId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.declaresService.deleteDeclares(declaresId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{declaresId}")
    @ResponseBody
    public Object content(@PathVariable("declaresId") Long id) {
        Declares declares = declaresService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(declares);
        return super.warpObject(new DeclaresWrapper(stringObjectMap));
    }
}