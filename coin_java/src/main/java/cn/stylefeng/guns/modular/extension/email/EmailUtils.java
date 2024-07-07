package cn.stylefeng.guns.modular.extension.email;

//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
//import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.exceptions.ServerException;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;

/** 邮箱发送工具类 */
public class EmailUtils {

  private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

//  public static void sendEmail(String toUser, String content, String accessKeyId, String secret) {
//    IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
//    IAcsClient client = new DefaultAcsClient(profile);
//    SingleSendMailRequest request = new SingleSendMailRequest();
//    try {
//      // request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
//      request.setAccountName("hdx@hdx.xunzirj.com");
//      request.setFromAlias("FDC");
//      request.setAddressType(1);
//      request.setReplyToAddress(true);
//      request.setToAddress(toUser);
//      // 可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
//      request.setSubject("FDC");
//      request.setHtmlBody(content);
//      SingleSendMailResponse httpResponse = client.getAcsResponse(request);
//    } catch (ServerException e) {
//      e.printStackTrace();
//    } catch (ClientException e) {
//      e.printStackTrace();
//    }
//  }

  public static void sendEmailUser(
      String toUser, String content, String accessKeyId, String secret) {
    sendEmailUser(toUser,content,accessKeyId,secret,"Email","验证");
  }

  /**
   *
   * @param toUser  邮箱
   * @param content 内容
   * @param accessKeyId key
   * @param secret 秘钥
   * @param from  发信人昵称,
   * @param title 邮件主题
   */
  public static void sendEmailUser(
      String toUser, String content, String accessKeyId, String secret,String from,String title) {
    try {
      // 请根据实际 accesskey 和 secretkey 进行开发，以下只作为演示 sdk 使用
      String accesskey = accessKeyId;
      String secretkey = secret;

      // 44,73

      // 邮件类型，0 事务投递，其他的为商业投递量
      int type = 0;
      // 拓展字段
      String ext = "";
      // 必须 管理控制台中配置的发信地址(登陆后台查看发信地址)
      String fromEmail = "mail@service.shuojianghu.com";
      //// 必须 ,如果为true是的时候，replyEmail必填，false的时候replyEmail可以为空
      Boolean needToReply = false;
      // 如果needToReply为true是的时候,则为必填,false的时候replyEmail可以为空
      String replyEmail = "";

      // 必须 目标邮件地址
      String toEmail = toUser;

      //// 可选 发信人昵称,
      String fromAlias = from;
      // 必须 邮件主题。
      String subject = title;
      // 必须 邮件 html 正文。
      String htmlBody = content;
      // 可选 取值范围 0~1: 1 为打开数据跟踪功能; 0 为关闭数据跟踪功能。该参数默认值为
      String clickTrace = "0";

      EmailSingleSender singleSender = new EmailSingleSender(accesskey, secretkey);
      EmailSingleSenderResult singleSenderResult =
          singleSender.send(
              type,
              fromEmail,
              toEmail,
              fromAlias,
              needToReply,
              replyEmail,
              subject,
              htmlBody,
              clickTrace,
              ext);
      System.out.println(singleSenderResult);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param toUser  邮箱
   * @param content 内容
   * @param accessKeyId key
   * @param secret 秘钥
   * @param from  发信人昵称,
   * @param title 邮件主题
   */
  public static void sendEmailUser(String emailAccount,
          String toUser, String content, String accessKeyId, String secret,String from,String title) {
    try {
      // 请根据实际 accesskey 和 secretkey 进行开发，以下只作为演示 sdk 使用
      String accesskey = accessKeyId;
      String secretkey = secret;

      // 44,73

      // 邮件类型，0 事务投递，其他的为商业投递量
      int type = 0;
      // 拓展字段
      String ext = "";
      // 必须 管理控制台中配置的发信地址(登陆后台查看发信地址)
      String fromEmail = emailAccount;
      //// 必须 ,如果为true是的时候，replyEmail必填，false的时候replyEmail可以为空
      Boolean needToReply = false;
      // 如果needToReply为true是的时候,则为必填,false的时候replyEmail可以为空
      String replyEmail = "";

      // 必须 目标邮件地址
      String toEmail = toUser;

      //// 可选 发信人昵称,
      String fromAlias = from;
      // 必须 邮件主题。
      String subject = title;
      // 必须 邮件 html 正文。
      String htmlBody = content;
      // 可选 取值范围 0~1: 1 为打开数据跟踪功能; 0 为关闭数据跟踪功能。该参数默认值为
      String clickTrace = "0";

      EmailSingleSender singleSender = new EmailSingleSender(accesskey, secretkey);
      EmailSingleSenderResult singleSenderResult =
              singleSender.send(
                      type,
                      fromEmail,
                      toEmail,
                      fromAlias,
                      needToReply,
                      replyEmail,
                      subject,
                      htmlBody,
                      clickTrace,
                      ext);
      System.out.println(singleSenderResult);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *
   *@param host 服务器
   * 	 * @param acount 账户
   * 	 * @param password 授权码
   * 	 * @param protocol 邮件协议
   *     * @param toUser  发送邮件给谁
   * 	 * @param title   邮件的标题
   * 	 * @param emailMsg  邮件信息
   */
  public static int sendEmail(String host,String account,String password,String protocol , String toUser,String title, int emailMsg){
    MailUtils.setConfig(host,account,password,protocol);
    try {
      MailUtils.sendMail(toUser,title,"此次的验证码："+emailMsg);
      logger.info("邮件发送成功");
      return 200;
    } catch (MessagingException e) {
      e.printStackTrace();
      logger.info("邮件发送失败");
      return 500;
    }
  }



  public static void main(String[] args) {}

  private static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
    Calendar date = Calendar.getInstance();
    date.setTime(nowTime);
    // 设置开始时间
    Calendar begin = Calendar.getInstance();
    begin.setTime(beginTime);
    // 设置结束时间
    Calendar end = Calendar.getInstance();
    end.setTime(endTime);
    // 处于开始时间之后，和结束时间之前的判断
    if (date.after(begin)) {
      return true;
    } else if (date.before(end)) {
      return true;
    } else {
      return false;
    }
  }
}
