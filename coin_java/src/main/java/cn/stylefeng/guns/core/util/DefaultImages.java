package cn.stylefeng.guns.core.util;

import cn.stylefeng.guns.core.listener.ConfigListener;

/**
 * 获取默认图片地址
 */
public class DefaultImages {

    /**
     * 默认的登录页面背景
     *
     */
    public static String loginBg() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/login-register.jpg";
    }

    /**
     * 默认的用户图片的base64编码
     */
    public static String defaultAvatarUrl() {
        return ConfigListener.getConf().get("contextPath") + "/system/previewAvatar";
    }

    /**
     * 默认的404错误页面背景
     */
    public static String error404() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/error-bg.jpg";
    }
}
