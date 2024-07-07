package cn.stylefeng.guns.core.common.constant;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

/**
 * 系统常量
 */
public interface Const {

    /**
     * 默认管理系统的名称
     */
    String DEFAULT_SYSTEM_NAME = "Guns管理系统";

    /**
     * 默认欢迎界面的提示
     */
    String DEFAULT_WELCOME_TIP = "欢迎使用Guns管理系统!";

    /**
     * 系统默认的管理员密码
     */
    String DEFAULT_PWD = "123456";

    /**
     * 系统默认的资金密码
     */
    String DEFAULT_PAY_PWD = "123456";

    /**
     * 管理员角色的名字
     */
    String ADMIN_NAME = "administrator";

    /**
     * 管理员id
     */
    Long ADMIN_ID = 1L;

    /**
     * 超级管理员角色id
     */
    Long ADMIN_ROLE_ID = 1L;

    /**
     * 接口文档的菜单名
     */
    String API_MENU_NAME = "接口文档";

    /**
     * 不需要权限验证的资源表达式
     */
    List<String> NONE_PERMISSION_RES = CollectionUtil.newLinkedList("/ueditor/**","/assets/**", "/gunsApi/**", "/login", "/global/sessionError", "/kaptcha", "/error", "/global/error","/quartzApi/**","/commonApi/**","/api/**","/uploads/**","/reg/**");

}
