package cn.stylefeng.guns.core.interceptor;

import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.HttpUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WithdrawInterceptor extends HandlerInterceptorAdapter
{


    @Autowired
    RedisUtil redisUtil;


    /**
     * 预处理回调方法，实现处理器的预处理（如登录检查）。
     * 第三个参数为响应的处理器，即controller。
     * 返回true，表示继续流程，调用下一个拦截器或者处理器。
     * 返回false，表示流程中断，通过response产生响应。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception
    {
        try
        {
            String ip = HttpUtil.getIpAddr(request);
            log.info("访问ip：{}", ip);

            Object objIp = F.me().cfg("IP");

            //验证ip是否存在
            if (ip == null || !(ip instanceof String)||!ip.equals(objIp.toString())){
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(403);
                response.setHeader("message", "非授权ip");
                Map<String, Object> map = new HashMap<>();
                map.put("code", 403);
                map.put("message", "非授权ip");
                response.getWriter().print(map);
                return false;
            }
            return true;
        } catch (Exception e)
        {
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
                           Object handler, ModelAndView modelAndView) throws Exception
    {

//        System.out.println("-------------------postHandle");

    }

    /**
     * 方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。
     * 这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception
    {
//        System.out.println("-------------------afterCompletion");
    }
}
