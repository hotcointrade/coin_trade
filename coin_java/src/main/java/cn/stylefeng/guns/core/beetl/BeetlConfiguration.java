package cn.stylefeng.guns.core.beetl;

import cn.stylefeng.guns.core.util.KaptchaUtil;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.util.ToolUtil;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

import java.util.HashMap;
import java.util.Map;

import static cn.stylefeng.guns.core.common.constant.Const.DEFAULT_SYSTEM_NAME;
import static cn.stylefeng.guns.core.common.constant.Const.DEFAULT_WELCOME_TIP;

/**
 * beetl拓展配置,绑定一些工具类,方便在模板中直接调用
 */
public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    @Override
    public void initOther() {

        //全局共享变量
        Map<String, Object> shared = new HashMap<>();
        shared.put("systemName", DEFAULT_SYSTEM_NAME);
        shared.put("welcomeTip", DEFAULT_WELCOME_TIP);
        groupTemplate.setSharedVars(shared);

        //全局共享方法
        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
        groupTemplate.registerFunctionPackage("tool", new ToolUtil());
        groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());

        //入侵框架比较严重，慎用！！！
        groupTemplate.registerFunctionPackage("factory", F.me());


    }
}
