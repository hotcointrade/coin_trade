package cn.stylefeng.guns.modular.base.util;


import cn.stylefeng.guns.core.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;


import javax.mail.internet.InternetAddress;


import javax.mail.internet.MimeMessage;


import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Slf4j
public class SampleMail {
    private static final String ALIDM_SMTP_HOST = "smtpdm.aliyun.com";
    private static final String ALIDM_SMTP_PORT = "80";//25或"80"
    private static final String ALIDM_SMTP_SSL_PORT = "465";
    private static  String TO = "";
    private static  String FROM_USERNAME = "";
    private static  String FROM_PASSWORD = "";
    private static  String FROM_SUBJECT= "";
    private static  String FROM_CONTENT= "";
    private static  String FROM_NICKNAME= "";

    public static String sendMail(String user,String password, String to ,String subject ,String content ,String nickname){
//        ThreadPoolUtil.execute(
//                ()->{
//                    setConfig(user , password ,to,subject,content,nickname);
//                    Execute();
//                }
//        );
        Future<String> submit = ThreadPoolUtil.submit(
                () -> {
                    setConfig(user, password, to, subject, content, nickname);
                    return Execute();
                }
        );
        String s =null;
        try {
            s=submit.get();
        }catch (ExecutionException e ){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static void setConfig(String user,String password, String to ,String subject ,String content ,String nickname){
        FROM_USERNAME = user;
        FROM_PASSWORD = password;
        TO = to;
        FROM_SUBJECT = subject;
        FROM_CONTENT = content;
        FROM_NICKNAME = nickname;
    }
    public static  String Execute(){
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
        props.put("mail.smtp.port", ALIDM_SMTP_PORT);
        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
//        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.socketFactory.port", ALIDM_SMTP_SSL_PORT);
//        props.put("mail.smtp.port", ALIDM_SMTP_SSL_PORT);

        // 发件人的账号，填写控制台配置的发信地址,比如xxx@xxx.com
        props.put("mail.user", FROM_USERNAME);
        // 访问SMTP服务时需要提供的密码(在控制台选择发信地址进行设置)


        props.put("mail.password", FROM_PASSWORD);
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession){
            //@Override
            //protected void updateMessageID() throws MessagingException {
            //设置自定义Message-ID值
            //setHeader("Message-ID", messageIDValue);
            //}
        };
        try {
            // 设置发件人邮件地址和名称。填写控制台配置的发信地址,比如xxx@xxx.com。和上面的mail.user保持一致。名称用户可以自定义填写。
            InternetAddress from = new InternetAddress(FROM_USERNAME, FROM_NICKNAME);

            message.setFrom(from);

            // 设置收件人邮件地址，比如yyy@yyy.com
            InternetAddress to = new InternetAddress(TO);
            message.setRecipient(MimeMessage.RecipientType.TO, to);
            // 设置邮件标题
            message.setSubject(FROM_SUBJECT);
            // 设置邮件的内容体
            message.setContent(FROM_CONTENT, "text/html;charset=UTF-8");
            log.info("发送内容{}",message);
            // 发送邮件
            Transport.send(message);
            log.info("发送成功"+message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            String err = e.getMessage();
            // 在这里处理message内容， 格式是固定的
            System.out.println(e);
            return "1";
        }
        return null;
    }







    public static void main(String[] args) {
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
        props.put("mail.smtp.port", ALIDM_SMTP_PORT);
        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
         props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         props.put("mail.smtp.socketFactory.port", ALIDM_SMTP_SSL_PORT);
         props.put("mail.smtp.port", ALIDM_SMTP_SSL_PORT);

        // 发件人的账号，填写控制台配置的发信地址,比如xxx@xxx.com
        props.put("mail.user", FROM_USERNAME);
        // 访问SMTP服务时需要提供的密码(在控制台选择发信地址进行设置)


        props.put("mail.password", FROM_PASSWORD);
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession){
            //@Override
            //protected void updateMessageID() throws MessagingException {
                //设置自定义Message-ID值
                //setHeader("Message-ID", messageIDValue);
            //}
        };
        try {
        // 设置发件人邮件地址和名称。填写控制台配置的发信地址,比如xxx@xxx.com。和上面的mail.user保持一致。名称用户可以自定义填写。
        InternetAddress from = new InternetAddress(FROM_USERNAME, FROM_NICKNAME);

        message.setFrom(from);

        // 设置收件人邮件地址，比如yyy@yyy.com
        InternetAddress to = new InternetAddress(TO);
        message.setRecipient(MimeMessage.RecipientType.TO, to);
        // 设置邮件标题
        message.setSubject(FROM_SUBJECT);
        // 设置邮件的内容体
        message.setContent(FROM_CONTENT, "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
            System.out.println("发送成功");
        } catch (MessagingException | UnsupportedEncodingException e) {
            String err = e.getMessage();
            // 在这里处理message内容， 格式是固定的
            System.out.println(err);
        }


    }






}

