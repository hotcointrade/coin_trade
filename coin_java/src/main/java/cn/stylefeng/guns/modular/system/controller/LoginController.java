package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.common.node.MenuNode;
import cn.stylefeng.guns.core.log.LogManager;
import cn.stylefeng.guns.core.log.factory.LogTaskFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.shiro.ShiroUser;
import cn.stylefeng.guns.core.shiro.ShiroUtils;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.stylefeng.roses.core.util.HttpContext.getIp;

/**
 * 登录控制器
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    SessionDAO sessionDAO;
    @Value("${system.security.code}")
    String systemSafe;

    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        //获取当前用户角色列表
        ShiroUser user = ShiroKit.getUserNotNull();
        List<Long> roleList = user.getRoleList();

        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }

        List<MenuNode> menus = userService.getUserMenuNodes(roleList);
        model.addAttribute("menus", menus);

        return "/index.html";
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {

        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali(Model model) {

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String safe = super.getPara("safe").trim();
        String remember = super.getPara("remember");

        Subject currentUser = ShiroKit.getSubject();
//        List<Session> loginedList = getLoginedSession(currentUser,username);
//        for (Session session : loginedList) {
//            session.stop();
//        }

        if(!systemSafe.equals(safe)){
            currentUser.logout();
            model.addAttribute("tips", "安全码错误");
            return "/login.html";
        }
//        if(currentUser.isAuthenticated()){
//            Session session = currentUser.getSession();
//            session.stop();
//        }
        delSession(currentUser,username);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        //如果开启了记住我功能
        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        //执行shiro登录操作
        currentUser.login(token);
//        Collection<Session> sessions =  sessionDAO.getActiveSessions();
//        if (currentUser.isAuthenticated()){
//            for (Session session:sessions){
//                if(username.equals(session.getAttribute("loginedUser"))){
//                    currentUser.logout();
//                    throw  new CredentialsException("用户已登录");
//                }
//            }
//        }
        //登录成功，记录登录日志
        ShiroUser shiroUser = ShiroKit.getUserNotNull();

        if (!ShiroKit.isAdmin()){
            currentUser.logout();
            model.addAttribute("tips", "非管理员，禁止进入");
            return "/login.html";
        }

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);
        ShiroKit.getSession().setAttribute("loginedUser",username);
        return REDIRECT + "/";
    }
    public String getIp() {
        final String UNKNOWN = "unknown";
        final String[] matchOptions = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        final int size = matchOptions.length;
        try {

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

            String ip = UNKNOWN;

            int index = 0;
            while(index < size && (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip))){
                ip = request.getHeader(matchOptions[index]);
                index++;
            }

            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            if (!StringUtils.isEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip) && ip.length() > 15) {
                String[] ips = ip.split(",");
                int len = ips.length;
                for (int i = 0; i < len; i++) {
                    String strIp = ips[index];
                    if (!(UNKNOWN.equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }

            return ip;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {

        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUserNotNull().getId(), getIp()));
        ShiroKit.getSubject().logout();
        deleteAllCookie();
        return REDIRECT + "/login";
    }
    //删除一个账户的session
    private List<Session> delSession(Subject currentUser ,String account) {
        SessionDAO sessionDAO = ((DefaultSessionManager) ((DefaultSecurityManager) SecurityUtils
                .getSecurityManager()).getSessionManager()).getSessionDAO();
        Collection<Session> list = sessionDAO.getActiveSessions();
        List<Session> loginedList = new ArrayList<Session>();
        ShiroUser loginUser =  (ShiroUser)currentUser.getPrincipal();
        for (Session session : list) {

            Subject s = new Subject.Builder().session(session).buildSubject();

            if (s.isAuthenticated()) {
                ShiroUser user = (ShiroUser) s.getPrincipal();//ShiroUser
                //System.out.println(user.getAccount());
                if (user.getAccount().equalsIgnoreCase(account)) {
                    if (!session.getId().equals(
                            currentUser.getSession().getId())) {
                        sessionDAO.delete(session);
                    }
                }
            }
        }
        return loginedList;
    }
}
