package cn.stylefeng.guns.core.common.annotion;

import java.lang.annotation.*;

/**
 * Api网关拦截
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiGateway {

    /**
     * 网关编码
     */
    String code() default "";



}
