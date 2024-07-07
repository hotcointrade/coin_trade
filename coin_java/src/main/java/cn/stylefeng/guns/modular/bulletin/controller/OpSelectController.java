package cn.stylefeng.guns.modular.bulletin.controller;

import cn.stylefeng.guns.modular.bulletin.entity.Bank;
import cn.stylefeng.guns.modular.bulletin.service.BankService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态下拉框-数据控制器
 */
@RestController
@RequestMapping({"/OpSelect"})
public class OpSelectController extends BaseController {


    @Autowired
    BankService bankService;

    /**
     * 获取银行列表
     */
    @RequestMapping("/getBankList")
    public List<Map<String, String>> getLevel() {
        List<Bank> list = bankService.list();
        List<Map<String,String>> result = new ArrayList<>();
        for (Bank entity : list) {
            if(entity.getDel().equals("Y"))
            {
                continue;
            }
            Map map = new HashMap();
            map.put("code", entity.getCode());
            map.put("value", entity.getName());
            result.add(map);
        }
        return result;
    }

}
