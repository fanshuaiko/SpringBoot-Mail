package com.fanshuai.service;

import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService  {

	private Logger log = LoggerFactory.getLogger(MailService.class);
	//发件账号
	@Value("${spring.mail.username}")
	private String from;
	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * 发送简单邮件
	 * @param to 收件人账号
	 * @param subject 主题
	 * @param content 内容
	 */
	public void sendSimpleMail(String to,String subject,String content) {
		log.info("开始发送简单邮件：{}，{}，{}，{}",from,to,subject,content);
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(to);
		/* String[] adds = {"xxx@qq.com","yyy@qq.com"}; //同时发送给多人
        message.setTo(adds);*/
		simpleMailMessage.setFrom(from);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		try {
			javaMailSender.send(simpleMailMessage);
			log.info("邮件发送成功！");
		} catch (Exception e) {
			log.error("邮件发送异常！", e);
		}
	}
	/**
	 * 发送HTML邮件
	 * @param to
	 * @param subject
	 * @param content
	 */
	public void sendHTMLMail(String to, String subject,String content) {
		log.info("开始发送HTML邮件：{}，{}，{}，{}",from,to,subject,content);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);//true表示需要创建一个multipart message
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content,true);//true代表有附件
			javaMailSender.send(mimeMessage);
			log.info("邮件发送成功！");
		} catch (MessagingException e) {
			log.error("邮件发送失败！", e);
			
		}
	}
	/**
	 * 发送带附件的邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @param filePath 附件所在路径
	 */
	public void sendAttachmentMail(String to, String subject,String content,String filePath) {
		log.info("开始发送附件邮件：{}，{}，{}，{}，{}",from,to,subject,content,filePath);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		 try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content,true);//true代表有附件
			//添加附件的操作，如果需要添加多个附件，将下面两条语句便利执行即可
			FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath)); 
			mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
			javaMailSender.send(mimeMessage);
			log.info("发送成功！");
		} catch (MessagingException e) {
			log.error("发送失败！",e);
		}
	}
	/**
	 *  发送带静态资源的邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @param rscPath 静态资源所在路径
	 * @param rscId 标志静态资源的唯一ID
	 */
	public void sendInlineMail(String to, String subject,String content,String rscPath,String rscId) {
		log.info("开始发送带静态资源邮件：{}，{}，{}，{}，{}，{}",from,to,subject,content,rscPath,rscId);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content,true);//true代表有附件
			FileSystemResource fileSystemResource = new FileSystemResource(new File(rscPath));
			//添加内联资源，一个id对应一个资源，最终通过id来找到该资源
			//添加多个图片可以使用多条 <img src='cid:" + rscId + "' > 和 helper.addInline(rscId, fileSystemResource) 来实现
			mimeMessageHelper.addInline(rscId, fileSystemResource);
			javaMailSender.send(mimeMessage);
			log.info("发送成功！");
		} catch (MessagingException e) {
			log.error("发送失败！",e);
		}
	}
	
	
	
	
}
