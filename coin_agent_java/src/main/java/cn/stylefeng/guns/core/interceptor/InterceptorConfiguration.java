package cn.stylefeng.guns.core.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截配置
 */
@SpringBootConfiguration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration ir = registry.addInterceptor(loginInterceptor());
        // 配置拦截的路径
        ir.addPathPatterns("/api/**");
        // 配置不拦截的路径
        ir.excludePathPatterns("/api/login");//登录
        ir.excludePathPatterns("/api/checkSmsCode");//校验账号发送验证码是否正确
        ir.excludePathPatterns("/api/register");//注册
        ir.excludePathPatterns("/api/forgetPwd");//忘记密码
        ir.excludePathPatterns("/api/getMsg");//短信验证码
        ir.excludePathPatterns("/api/version");//版本
        ir.excludePathPatterns("/api/upload/**");//图片上传
        ir.excludePathPatterns("/api/personal/**");//处理外部回调api
        ir.excludePathPatterns("/api/common/**");//公共api
        ir.excludePathPatterns("/api/visitor/**");//游客访问api

        InterceptorRegistration irn = registry.addInterceptor(IpInterceptor());
        irn.addPathPatterns("/**");
        irn.excludePathPatterns("/api/**");
        //提现拦截器
//        InterceptorRegistration irW = registry.addInterceptor(withdrawInterceptor());
        //提币审核、公账提币
//        irW.addPathPatterns("/withdraw/pass").addPathPatterns("/public/add");
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public IpInterceptor IpInterceptor() {
        return new IpInterceptor();
    }

    @Bean
    public WithdrawInterceptor withdrawInterceptor(){return new WithdrawInterceptor();}

}
