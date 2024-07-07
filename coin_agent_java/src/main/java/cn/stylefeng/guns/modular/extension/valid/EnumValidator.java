package cn.stylefeng.guns.modular.extension.valid;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 枚举验证器
 */
public class EnumValidator implements ConstraintValidator<EnumValid, String>
{
    // 枚举校验注解
    private EnumValid enumValid;

    /**
     * 初始化
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(EnumValid constraintAnnotation)
    {
        enumValid=constraintAnnotation;
    }



    /**
     * 验证
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        boolean result=false;



        Class<?> cls=enumValid.target();
        boolean  ignoreEmpty=enumValid.ignoreEmpty();
        // target为枚举，并且value有值，或者不忽视空值，才进行校验
        if (cls.isEnum() && (value != null || !ignoreEmpty)) {

            Object[] objects = cls.getEnumConstants();
            for (Object obj : objects) {
                if (obj.toString().equals(value)) {
                    result = true;
                    break;
                }
            }



        } else {
            result = true;
        }

        //是否有固定值，如果深入校验
        String[] fixed=enumValid.fixed();
        if(fixed!=null)
        {
            for (String s : fixed)
            {
                if(s.equals(value))
                    return true;
            }
            return false;
        }

        return result;
    }
}
