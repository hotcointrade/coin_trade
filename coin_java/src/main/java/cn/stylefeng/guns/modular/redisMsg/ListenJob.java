package cn.stylefeng.guns.modular.redisMsg;

import cn.stylefeng.guns.modular.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ListenJob
{
    @Autowired
    private RedisTemplate redisTemplate;


//    @Scheduled(fixedRate = 3000)
    public void sendMessage(){
        User user = new User();
        user.setName("aaa");
//        user.setName("zhibi"+i);
//        user.setUserId(i);
        redisTemplate.convertAndSend("index",user);
    }
}
