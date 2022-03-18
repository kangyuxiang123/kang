package com.bdqn.controller;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * JavaMail Demo
 *
 */
public class App 
{
	public static String account = "kyxiang@162.com";			//替换为发件人账号
    public static String password = "Kyx321851";					//替换为发件人账号密码
    
    public static String receiveMailAccount = "kyxiang@162.com";	//替换为收件人账号
    public static void main( String[] args ) throws Exception
    {
    	// 1. 使用Properties对象封装连接所需的信息
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议
        props.setProperty("mail.smtp.host", "smtp.126.com");   	// 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        
        // 2. 获取Session对象
        Session session = Session.getDefaultInstance(props);           
        // 3. 封装Message对象
        MimeMessage message = createMimeMessage(session, account, receiveMailAccount);
        // 4. 使用Transport发送邮件
        Transport transport = session.getTransport();
        transport.connect(account, password);   
        transport.sendMessage(message, message.getAllRecipients());
        // 5. 关闭连接
        transport.close();
    }
    
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) 
    		throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "爱旅行", "UTF-8"));
        // 3. To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, 
        		new InternetAddress(receiveMail, "XX用户", "UTF-8"));
        // 4. Subject: 邮件主题
        message.setSubject("邮件测试略略略", "UTF-8");
        // 5. Content: 邮件正文
        message.setContent("用户你好。。。哈哈哈哈", "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
    }
}
