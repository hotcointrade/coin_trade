package cn.stylefeng.guns.modular.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.dictmap.DeptDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.node.TreeviewNode;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.system.entity.Dept;
import cn.stylefeng.guns.modular.system.model.DeptDto;
import cn.stylefeng.guns.modular.system.service.DeptService;
import cn.stylefeng.guns.modular.system.warpper.DeptTreeWrapper;
import cn.stylefeng.guns.modular.system.warpper.DeptWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.treebuild.DefaultTreeBuildFactory;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 */
@Controller
@RequestMapping("/dept")
public class DeptController extends BaseController {

    private String PREFIX = "/modular/system/dept/";

    @Autowired
    private DeptService deptService;

    /**
           * 跳转到部门管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dept.html";
    }

    /**
            * 跳转到添加部门
     *
     */
    @RequestMapping("/dept_add")
    public String deptAdd() {
        return PREFIX + "dept_add.html";
    }

    /**
             * 跳转到修改部门
     */
    @Permission
    @RequestMapping("/dept_update")
    public String deptUpdate(@RequestParam("deptId") Long deptId) {

        if (ToolUtil.isEmpty(deptId)) {
            throw new RequestEmptyException();
        }

        //缓存部门修改前详细信息
        Dept dept = deptService.getById(deptId);
        LogObjectHolder.me().set(dept);

        return PREFIX + "dept_edit.html";
    }

    /**
              * 获取部门的tree列表，ztree格式
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.deptService.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
             * 获取部门的tree列表，treeview格式
     */
    @RequestMapping(value = "/treeview")
    @ResponseBody
    public List<TreeviewNode> treeview() {
        List<TreeviewNode> treeviewNodes = this.deptService.treeviewNodes();

        //构建树
        DefaultTreeBuildFactory<TreeviewNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("0");
        List<TreeviewNode> results = factory.doTreeBuild(treeviewNodes);

        //把子节点为空的设为null
        DeptTreeWrapper.clearNull(results);

        return results;
    }

    /**
              * 新增部门
     */
    @BussinessLog(value = "添加部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/add")
    @Permission
    @ResponseBody
    public ResponseData add(Dept dept) {
        this.deptService.addDept(dept);
        return SUCCESS_TIP;
    }

    /**
             * 获取所有部门列表
     */
    @RequestMapping(value = "/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(value = "condition", required = false) String condition,
                       @RequestParam(value = "deptId", required = false) String deptId) {
        Page<Map<String, Object>> list = this.deptService.list(condition, deptId);
        Page<Map<String, Object>> wrap = new DeptWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
             * 部门详情
     */
    @RequestMapping(value = "/detail/{deptId}")
    @Permission
    @ResponseBody
    public Object detail(@PathVariable("deptId") Long deptId) {
        Dept dept = deptService.getById(deptId);
        DeptDto deptDto = new DeptDto();
        BeanUtil.copyProperties(dept, deptDto);
        deptDto.setpName(ConstantFactory.me().getDeptName(deptDto.getPid()));
        return deptDto;
    }

    /**
     * 修改部门
     */
    @BussinessLog(value = "修改部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/update")
    @Permission
    @ResponseBody
    public ResponseData update(Dept dept) {
        deptService.editDept(dept);
        return SUCCESS_TIP;
    }

    /**
     * 删除部门
     */
    @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public ResponseData delete(@RequestParam Long deptId) {

        //缓存被删除的部门名称
        LogObjectHolder.me().set(ConstantFactory.me().getDeptName(deptId));

        deptService.deleteDept(deptId);

        return SUCCESS_TIP;
    }

}
