package cn.stylefeng.guns.modular.meta_data.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.exception.BussinessException;
import cn.stylefeng.guns.core.template.ContextConfig;
import cn.stylefeng.guns.core.template.engine.SimpleTemplateEngine;
import cn.stylefeng.guns.core.template.engine.base.GunsTemplateEngine;
import cn.stylefeng.guns.modular.base.util.SqlUtil;
import cn.stylefeng.guns.modular.meta_data.entity.LyyGen;
import cn.stylefeng.guns.modular.meta_data.model.TableDto;
import cn.stylefeng.guns.modular.meta_data.service.CodeService;
import cn.stylefeng.guns.modular.system.entity.Menu;
import cn.stylefeng.guns.modular.system.service.MenuService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成控制器
 *
 * @author yaying.liu
 * @Date 2019-08-08 00:03
 * @Update DESC: 新增 菜单sql脚本功能
 * @Update :2019-09-24 11:41
 */
@Controller
@RequestMapping("/code")
public class CodeController extends BaseController {

    @Autowired
    MenuService menuService;
    @Autowired
    private CodeService codeService;


    private String PREFIX = "/modular/system/code/";

    /**
     * 跳转到代码生成首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "code.html";
    }

    /**
     * 代码生成
     */
    @ApiOperation("生成代码")
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ResponseBody
    @Permission(Const.ADMIN_NAME)
    public Object add(LyyGen lyyGen) {
        String bizChName = lyyGen.getBizChName();
        String bizEnName = lyyGen.getBizEnName();
        String moduleName = lyyGen.getModuleName();
        String path = lyyGen.getPath();
        if (ToolUtil.isOneEmpty(bizChName, bizEnName)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        ContextConfig contextConfig = new ContextConfig();
        contextConfig.setTableNamePrefix(moduleName);
        contextConfig.setBizChName(bizChName);
        contextConfig.setBizEnName(bizEnName);
        contextConfig.setModuleName(moduleName);
        contextConfig.setTopMenuName(lyyGen.getTopMenuName());
        contextConfig.setSort(lyyGen.getSort());
        contextConfig.setTableSchema(lyyGen.getTableSchema());
        contextConfig.setBizTableName(lyyGen.getBizTableName());

        List<TableDto> tmpList = codeService.getBaseMapper().getTableList(lyyGen.getTableSchema(), lyyGen.getBizTableName());
        List<TableDto> tableFieldList =new ArrayList<>();
        for (TableDto entity : tmpList) {
            TableDto dto=new TableDto();
            BeanUtil.copyProperties(entity,dto);
            dto.setPropertyType(SqlUtil.toSqlToJava(dto.getDataType()))
                    .setPropertyName(SqlUtil.column2HumpName(dto.getColumnName()));
            tableFieldList.add(dto);
        }
        contextConfig.setTableFieldList(tableFieldList);

        if (ToolUtil.isNotEmpty(path)) {
            contextConfig.setProjectPath(path);
        }

        GunsTemplateEngine gunsTemplateEngine = new SimpleTemplateEngine();
        gunsTemplateEngine.setContextConfig(contextConfig);
        gunsTemplateEngine.start();

        return super.SUCCESS_TIP;
    }

    @RequestMapping("/getTypeList")
    @ResponseBody
    public List<Map<String, String>> getLevel() {
        Menu menuQ = new Menu();
        menuQ.setPcode("0");
        List<Menu> list = menuService.list(new QueryWrapper<>(menuQ));
        List<Map<String, String>> result = new ArrayList<>();
        for (Menu entity : list) {
            Map map = new HashMap();
            map.put("code", entity.getCode());
            map.put("value", entity.getName());
            result.add(map);
        }
        return result;
    }

    @RequestMapping("/getTableList")
    @ResponseBody
    public List<Map<String, String>> getTableList(String tableSchema) {
        List<TableDto> list = this.codeService.getBaseMapper().getTableList(tableSchema, null);
        List<Map<String, String>> result = new ArrayList<>();
        for (TableDto entity : list) {
            Map map = new HashMap();
            map.put("code", entity.getTableName());
            map.put("value", entity.getTableName());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取数据源
     *
     * @return
     */
    @RequestMapping("/showDateBase")
    @ResponseBody
    public List<Map<String, String>> showDateBase() {
        List<String> list = this.codeService.getBaseMapper().showDateBase();
        List<Map<String, String>> result = new ArrayList<>();
        for (String entity : list) {
            Map map = new HashMap();
            map.put("code", entity);
            map.put("value", entity);
            result.add(map);
        }
        return result;
    }


}