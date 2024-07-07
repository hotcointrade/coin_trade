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
import java.util.Map;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.guns.modular.com.service.MediaService;
import cn.stylefeng.guns.modular.com.wrapper.MediaWrapper;
import cn.stylefeng.guns.modular.com.entity.Media;
import cn.stylefeng.guns.modular.com.constant.MediaMap;

/**
 * 视频和音频控制器
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:44:00
 */
@Controller
@RequestMapping("/media")
public class MediaController extends BaseController {

    private String PREFIX = "/modular/com/media/";

     @Autowired
     private MediaService mediaService;

    /**
     * 跳转到视频和音频首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "media.html";
    }

    /**
     * 跳转到添加视频和音频
     */
    @RequestMapping("/media_add")
    public String mediaAdd() {
        return PREFIX + "media_add.html";
    }

    /**
     * 跳转到修改视频和音频
     */
    @RequestMapping("/media_edit")
    public String mediaEdit(Long mediaId, Model model) {
        Media condition = this.mediaService.getById(mediaId);
        model.addAllAttributes(BeanUtil.beanToMap(condition));
        LogObjectHolder.me().set(condition);
        return PREFIX + "media_edit.html";
    }

    /**
     * 视频和音频详情
     */
    @RequestMapping(value = "/detail/{mediaId}")
    @ResponseBody
    public Object detail(@PathVariable("mediaId") Long mediaId) {
        Media entity = mediaService.getById(mediaId);
      //  MediaDto conditionDto = new MediaDto();
      //  BeanUtil.copyProperties(entity, conditionDto);
      //  return conditionDto;
        return entity;
    }


    /**
     * 查询视频和音频列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        //根据条件查询
        Page<Map<String, Object>> result = mediaService.selectByCondition(condition);
        Page wrapped = new MediaWrapper(result).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 编辑视频和音频
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "编辑参数", key = "mediaId", dict = MediaMap.class)
    @ResponseBody
    public ResponseData edit(Media media) {
        mediaService.updateById(media);
        return SUCCESS_TIP;
    }

    /**
     * 添加视频和音频
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加视频和音频", key = "name", dict = MediaMap.class)
    @ResponseBody
    public ResponseData add(Media media, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (media == null) {
            return ResponseData.error("参数不能为空");
        }
        this.mediaService.addMedia(media);
        return SUCCESS_TIP;
    }

    /**
     * 删除视频和音频
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除视频和音频", key = "mediaId", dict = MediaMap.class)
    @ResponseBody
    public ResponseData delete(@RequestParam Long mediaId) {
        if (ToolUtil.isEmpty(mediaId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.mediaService.deleteMedia(mediaId);
        return SUCCESS_TIP;
    }

    /**
     * 查询内容详情
     */
    @RequestMapping("/content/{mediaId}")
    @ResponseBody
    public Object content(@PathVariable("mediaId") Long id) {
        Media media = mediaService.getById(id);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(media);
        return super.warpObject(new MediaWrapper(stringObjectMap));
    }
}