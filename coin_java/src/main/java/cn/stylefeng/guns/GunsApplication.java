package cn.stylefeng.guns;

import cn.stylefeng.roses.core.config.WebAutoConfiguration;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot方式启动类
 *
 */
//@ComponentScan()

@EnableFileStorage //启用FileStorage
@SpringBootApplication(scanBasePackages={"cn.hutool.extra.spring","cn.stylefeng"},exclude = {WebAutoConfiguration.class})
@EnableScheduling
@EnableAsync
public class GunsApplication {

    private final static Logger logger = LoggerFactory.getLogger(GunsApplication.class);

    public static void main(String[] args) {
        // 设置环境变量，解决Es的netty与Netty服务本身不兼容问题
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(GunsApplication.class, args);
        logger.info(GunsApplication.class.getSimpleName() + " is success【^0^】!");
    }
}
