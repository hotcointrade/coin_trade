package cn.stylefeng.guns.core.aop;

import cn.stylefeng.guns.core.common.annotion.ApiGateway;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.promotion.entity.GatewayRecord;
import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 网关记录
 */
@Aspect
@Component
@Order(10)
public class GatewayAop {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut(value = "@annotation(cn.stylefeng.guns.core.common.annotion.ApiGateway)")
    public void cutService() {
    }

    @Around(value = "cutService()")
    public Object recordInfo(ProceedingJoinPoint point) throws Throwable {

        
        Object result = null;

        try {
            if(handle(point))
            {//执行业务
                result = point.proceed();
            }

        } catch (Exception e) {
            log.error("网关出错!", e);
        }
        return result;
    }

    private boolean handle(ProceedingJoinPoint point) throws Exception {
        //获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

        String classType = point.getTarget().getClass().getName();

        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();
        String methodName = point.getSignature().getName(); //获取方法名称  


        //获取拦截方法的参数
        String className = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();

        //获取操作名称
        ApiGateway annotation = currentMethod.getAnnotation(ApiGateway.class);
        String apiGatewayCode = annotation.code();

        if(!F.me().existGateWay(apiGatewayCode))
        {
            throw new Exception("网关未注册，请注册网关");
        }

        /**
         * 是否开放网关
         */
        if(!isEnableGateway(apiGatewayCode))
        {
            throw new Exception("网关未开放");
        }


        /**
         * 是否开放日志记录
         */
        if(isOpenLog(apiGatewayCode)){
            StringBuilder stringBuilder=new StringBuilder();
            //获取参数名称和值
            Map<String, Object> nameAndArgs = this.getFieldsName(this.getClass(), clazzName, methodName, params);
            Iterator<Map.Entry<String, Object>> entries = nameAndArgs.entrySet().iterator();
            while (entries.hasNext())
            {
                Map.Entry<String, Object> entry = entries.next();
                stringBuilder.append("【"+entry.getKey()+":"+entry.getValue()+"】");
            }
            GatewayRecord gatewayRecord = new GatewayRecord();
            gatewayRecord.setInterfaceCode(apiGatewayCode)
                    .setRequestData(stringBuilder.toString());
            F.me().gatewayRecord(gatewayRecord);
        }

        return true;
    }

    /**
     * 是否开放日志记录
     */
    private boolean isOpenLog(String apiGatewayCode) {
        return F.me().isOpenLog(apiGatewayCode);
    }

    /**
     * 是否启用 网关
     * @param apiGatewayCode
     * @return
     */
    private boolean isEnableGateway(String apiGatewayCode) {
        return F.me().isEnableGateway(apiGatewayCode);
    }

    /**
     * @return Map
     * @Description 获取字段名和字段值
     */
    private Map<String, Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();


        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);
        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception    
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);//paramNames即参数名    
        }
        return map;
    }


}
