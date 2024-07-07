package cn.stylefeng.guns.core.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SmsUtil {

    @Autowired
    private RedisUtil redisUtil;

    final static private String API_ACCESS_KEY = "4aa99a27f3ac4cf28e0b3a767569ce9b";

    final static private String API_SECRET_KEY = "12228dadf7094188b74dfb251051b4ce";

    final static private String REQUEST_URL = "http://api.kmicloud.com/sms/send/v1/otp";

    /**
     * 手机发送短信
     * @param type 区号
     * @param phone 手机号
     * @return
     */
    public boolean sendSms(String type, String phone){
        int verificationCode = RandomUtil.randomInt(1000, 9999);
        String smsCodeKey = Constant.VERIFICATION_CODE_SMS + phone;
        if(redisUtil.hasKey(smsCodeKey)){
            return false;
        }
        JSONObject params = new JSONObject();
        params.set("accessKey", API_ACCESS_KEY);
        params.set("secretKey", API_SECRET_KEY);
        params.set("to", type+phone);
        params.set("message", verificationCode+"");
        String resp = HttpUtil.post(REQUEST_URL, params.toString());
        JSONObject respJson = JSONUtil.parseObj(resp);
        Integer code = respJson.getInt("code");
        if(200 != code){
            log.error("[短信发送错误] " + respJson.toString());
            return false;
        }
        redisUtil.set(smsCodeKey, verificationCode, 60);
        redisUtil.set(smsCodeKey + "_" + verificationCode, verificationCode+"", 300);
        return true;
    }

    /**
     * 校验验证码
     * @param phone
     * @param code
     * @return
     */
    public boolean vaildCode(String phone, String code){
        String smsCodeKey = Constant.VERIFICATION_CODE_SMS + phone;
        return redisUtil.hasKey(smsCodeKey + "_" + code);
    }

}
