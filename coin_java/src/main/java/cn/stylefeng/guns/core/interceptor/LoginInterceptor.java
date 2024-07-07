package cn.stylefeng.guns.core.interceptor;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * app 登录拦截器 token验证
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MemberService memberService;




    /**
     * 预处理回调方法，实现处理器的预处理（如登录检查）。
     * 第三个参数为响应的处理器，即controller。
     * 返回true，表示继续流程，调用下一个拦截器或者处理器。
     * 返回false，表示流程中断，通过response产生响应。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        try {
            //验证用户是否登陆
            Object obj = request.getHeader("token");
            if (obj == null || !(obj instanceof String)) {
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(403);
                Map<String, Object> map = new HashMap<>();
                map.put("code", 403);
                map.put("message", "未登录");
                response.getWriter().print(map);
                return false;
            }
            Member c = (Member) redisUtil.get(obj.toString());

            if (redisUtil.get(obj.toString()) == null || !(redisUtil.get(obj.toString()) instanceof Member)) {
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(403);
                Map<String, Object> map = new HashMap<>();
                map.put("code", 403);
                map.put("message", "登录时间已过");
                response.getWriter().print(map);
                return false;
            }

            String token = obj.toString();
//            logger.error(JSONObject.toJSONString(redisUtil.get(token)));
            Member memberQ = (Member) redisUtil.get(token);
            Member member = memberService.getById(memberQ.getMemberId());
            if (member == null) {
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(403);
                Map<String, Object> map = new HashMap<>();
                map.put("code", 403);
                map.put("message", "登录时间已过");
                response.getWriter().print(map);
                return false;
            }
            if (member.getStatus().equals("N")) {
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(403);
                Map<String, Object> map = new HashMap<>();
                map.put("code", 403);
                map.put("message", "用户已封禁");
                response.getWriter().print(map);
                return false;
            }
            if (member.getDel().equals("Y")) {
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(403);
                Map<String, Object> map = new HashMap<>();
                map.put("code", 403);
                map.put("message", "用户不存在");
                response.getWriter().print(map);
                return false;
            }
            redisUtil.set(token, member, Long.parseLong(F.me().getSysConfigValueByCode(Constant.TOKEN_EXPIRE)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 当前请求进行处理之后，也就是Controller 方法调用之后执行，
     * 但是它会在DispatcherServlet 进行视图返回渲染之前被调用。
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

//        System.out.println("-------------------postHandle");

    }

    /**
     * 方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。
     * 这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
//        System.out.println("-------------------afterCompletion");
    }
}
