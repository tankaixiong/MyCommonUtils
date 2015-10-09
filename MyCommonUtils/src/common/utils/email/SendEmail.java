package common.utils.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author tank
 *
 */
public class SendEmail {
	private static final Logger log = LoggerFactory.getLogger(SendEmail.class);
	// 邮箱服务器
	private static String host = "smtp.163.com";
	// 发送方邮箱账户
	private static String mail_from = "servertip@163.com";
	// 发送方邮箱密码
	private static String emailpassword = "tanktank";
	// 发送方显示名称
	private static String personalName = "监控者1号";

	/**
	 * 发送电子邮件
	 * 
	 * @param subject
	 *            邮件主题
	 * @param body
	 *            邮件内容
	 * @param Tomail
	 *            目标邮件地址
	 */
	public void send(String subject, String body, String Tomail) {
		try {
			if (Tomail != null && Tomail.length() > 0) {
				Properties props = new Properties(); // 获取系统环境
				Authenticator auth = new Email_Autherticator(); // 进行邮件服务器用户认证
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "25");
				Session session = Session.getDefaultInstance(props, auth);
				// 设置session,和邮件服务器进行通讯
				MimeMessage message = new MimeMessage(session);
				message.setSubject(subject); // 设置邮件主题
				message.setContent(body, "text/html");
				message.setText(body); // 设置邮件正文
				message.setHeader("Content-Type", "text/html");
				message.setSentDate(new Date());// 设置邮件发送日期
				Address address = new InternetAddress(mail_from, personalName);
				message.setFrom(address); // 设置邮件发送者的地址
				Address toAddress = new InternetAddress(Tomail); // 设置邮件接收方的地址
				message.addRecipient(Message.RecipientType.TO, toAddress);
				try {
					Transport.send(message); // 发送邮件
					log.info("邮件发送成功....");
				} catch (Exception e) {
					log.error("发送邮件出现异常:{}", e);
				}
			}
		} catch (Exception ex) {
			log.error("{}", ex);
		}
	}

	/**
	 * 用来进行服务器对用户的认证
	 */
	public class Email_Autherticator extends Authenticator {
		public Email_Autherticator() {
			super();
			log.info("正在验证用户名和密码 ..");
		}

		public Email_Autherticator(String user, String pwd) throws Exception {
			super();
			mail_from = user;
			emailpassword = pwd;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			log.info("正在发送.. 请稍候..");
			return new PasswordAuthentication(mail_from, emailpassword);
		}
	}
}
