package com.fanshuai;

import com.fanshuai.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootMailApplicationTests {

	@Autowired
	private MailService mailService;
	//注入模板引擎
	@Autowired
	private TemplateEngine templateEngine;
	//设置收件账号
	private String to = "11111111@qq.com";
	//设置邮件主题
	private String subject = "测试邮件";
	//设置简单邮件内容
	private String simpleContent = "测试邮件的发送！";
	//设置HTML邮件内容
	private String htmlContent = "<html>" + "<body>" + "<h3>这是HTML邮件的内容</h3>" + "</body>" + "</html>";
	//图片路径
	private String filePath = "static/images/picture.jpg";
	//静态资源（此处为图片）唯一标识id
	private String rscId = "001";
	//带静态资源邮件内容
	private String inlineContent = "<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\'></img></body></html>";

	/**
	 * 测试简单邮件的发送
	 */
	@Test
	public void testSendSimpleMailTest() {
		mailService.sendSimpleMail(to, subject, simpleContent);
	}

	/**
	 * 测试HTML邮件的发送
	 */
	@Test
	public void testSendHTMLMailTest() {
		mailService.sendHTMLMail(to, subject, htmlContent);
	}

	/**
	 * 测试带附件邮件的发送
	 */
	@Test
	public void testSendAttachmentMail() {
		mailService.sendAttachmentMail(to, subject, simpleContent, filePath);
	}

	/**
	 * 测试带静态资源邮件的发送
	 */
	@Test
	public void testSendInlineMail() {
		mailService.sendInlineMail(to, subject, inlineContent, filePath, "001");
	}

	/**
	 * 测试模板邮件的发送
	 */
	@Test
	public void testSendTemplateMailTest() {
		Context context = new Context();
		//设置模板页面中的参数
		context.setVariable("id","0010");
		//设置模板页面中的参数
		context.setVariable("name","zhangsan");
		//设置模板邮件的内容
		String emailContent = templateEngine.process("MailTemplate",context);
		//模板邮件的发送依然使用的是HTML邮件发送，因为本身模板就是HTML页面
		mailService.sendHTMLMail(to, subject, emailContent);
	}

	
}
