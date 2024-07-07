package cn.stylefeng.guns.modular.promotion.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.promotion.entity.InformationCategory;
import cn.stylefeng.guns.modular.promotion.service.InformationCategoryService;
import cn.stylefeng.guns.modular.promotion.wrapper.ProblemWrapper;
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
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/informationCategory")
public class InformationCategoryController extends BaseController {

    private String PREFIX = "/modular/promotion/informationCategory/";

     @Autowired
     private InformationCategoryService informationCategoryService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "information_category.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/information_category_add")
    public String InformationCategoryAdd() {
        return PREFIX + "information_category_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/information_category_edit/{id}")
    public String informationCategoryEdit(@PathVariable("id")Long id, Model model) {
        InformationCategory condition = this.informationCategoryService.getById(id);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "information_category_edit.html";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable("id") Long id) {
        InformationCategory entity = informationCategoryService.getById(id);
      //  ProblemDto conditionDto = new ProblemDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = informationCategoryService.selectByCondition(condition);
        Page wrapped = new ProblemWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseData edit(InformationCategory dto) {
        InformationCategory entity = informationCategoryService.getById(dto.getId());
        entity.setTitle(dto.getTitle()).setRemark(dto.getRemark());
        entity.setSort(dto.getSort());
        informationCategoryService.updateById(entity);
        return SUCCESS_TIP;
    }

    /**
     * 添加
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseData add(InformationCategory problem, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (problem == null) {
            return ResponseData.error("参数不能为空");
        }
        problem.setCreateTime(new Date());
        problem.setCreateUser(1L);
        problem.setDel("N");
        problem.setStatus("Y");
        this.informationCategoryService.save(problem);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        InformationCategory entity = informationCategoryService.getById(id);
        entity.setDel("Y");
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(1l);
        this.informationCategoryService.updateById(entity);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/getList")
    @ResponseBody
    public ResponseData getList() {
        InformationCategory informationCategory = new InformationCategory();
        informationCategory.setDel("N");
        informationCategory.setStatus("Y");
        List<InformationCategory> list = 	this.informationCategoryService.list(new QueryWrapper<>(informationCategory));
        return  ResponseData.success(list);
    }



}