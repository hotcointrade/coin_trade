package cn.stylefeng.guns.modular.bulletin.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.guns.modular.base.util.TimeUtil;
import cn.stylefeng.guns.modular.bulletin.constant.ContactMap;
import cn.stylefeng.guns.modular.bulletin.entity.Contact;
import cn.stylefeng.guns.modular.bulletin.model.ContactDto;
import cn.stylefeng.guns.modular.bulletin.service.ContactService;
import cn.stylefeng.guns.modular.bulletin.wrapper.ContactWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/contact")
public class ContactController extends BaseController {

    private static String PREFIX = "/modular/promotion/contact/";

    @Autowired
    private ContactService contactService;

    /**
     * 跳转到管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "contact.html";
    }


    /**
     * 跳转到添加
     */
    @RequestMapping("/contact_add")
    public String contactAdd() {
        return PREFIX + "contact_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/contact_edit")
    public String contactEdit(Long contactId, Model model) {
        Contact condition = this.contactService.getById(contactId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX+"contact_edit.html";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{contactId}")
    @ResponseBody
    public Object detail(@PathVariable("contactId") Long contactId) {
        Contact contact = contactService.getById(contactId);
        ContactDto conditionDto = new ContactDto();
        BeanUtil.copyProperties(contact, conditionDto);
        conditionDto.setStartTime(DateTimeUtil.dateToStr(contact.getStart(),"HH:mm:ss") );
        conditionDto.setEndTime(DateTimeUtil.dateToStr(contact.getEnd(),"HH:mm:ss") );
        return conditionDto;
    }

    /**
     * 查询列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {

        //根据条件查询
        Page<Map<String, Object>> result = contactService.selectByCondition(condition);
        Page wrapped = new ContactWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
//    @BussinessLog(value = "编辑客服", key = "contactId", dict = ContactMap.class)
    @ResponseBody
    public ResponseData edit(ContactDto dto) {
        Contact contact=new Contact();
        BeanUtil.copyProperties(dto,contact);
//        if(dto.getStartTime()!=null&&dto.getEndTime()!=null)
//        {
//            contact.setStart(DateTimeUtil.strToDate(dto.getStartTime(),"HH:mm:ss"));
//            contact.setEnd(DateTimeUtil.strToDate(dto.getEndTime(),"HH:mm:ss"));
//        }
        contactService.updateById(contact);
        return SUCCESS_TIP;
    }


    /**
     * 添加客服
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加客服", key = "contactId", dict = ContactMap.class)
    @ResponseBody
    public ResponseData add(Contact contact, BindingResult result) {

        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (contact == null) {
            return ResponseData.error("参数不能为空");
        }
        contact.setStatus("DISABLE");
        this.contactService.addContact(contact);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除参数", key = "contactId", dict = ContactMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long contactId) {
        if (ToolUtil.isEmpty(contactId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.contactService.deleteContact(contactId);
        return SUCCESS_TIP;
    }

    /**
     * 修改状态
     * @param status
     * @param contactId
     * @return
     */
    @RequestMapping("/status/{status}")
    @BussinessLog(value = "修改客服信息状态", key = "contactId", dict = ContactMap.class)
    @ResponseBody
    public ResponseData status(@PathVariable("status") String status,@RequestParam Long contactId) {
        Contact contact = contactService.getById(contactId);
        contact.setStatus(status);
        contactService.updateById(contact);
        return SUCCESS_TIP;
    }

}
