package cn.stylefeng.guns.modular.app.controller;

import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.base.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 游客Api
 * @description: api下接口，无需token访问
 */
@RequestMapping("/api/visitor")
@RestController
public class VisitorApi {

    @Autowired
    HomeService homeService;


}
