package cn.stylefeng.guns.modular.extension.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 枚举验证
 */
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface EnumValid
{
    String message() default "";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * 目标枚举类
     * @return
     */
    Class<?> target() default Class.class;

    /**
     * 固定值
     * @return
     */
    String[] fixed() default {};


    /**
     * 是否忽略空值
     * @return
     */
    boolean ignoreEmpty() default true;

}
