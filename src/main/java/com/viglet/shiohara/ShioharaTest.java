package com.viglet.shiohara;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/api/v2/test")
@Api(value = "/api/v2/test", tags = "Heartbeat", description = "Heartbeat")
public class ShioharaTest {


	@Autowired
    private JavaMailSender javaMailSender;

	@GetMapping
	public String test() {
		try {

			 SimpleMailMessage msg = new SimpleMailMessage();
		        msg.setTo("alexandre.oliveira@gmail.com");

		        msg.setSubject("Testing from Spring Boot");
		        msg.setText("Hello World \n Spring Boot Email");
		
		        javaMailSender.send(msg);
		} catch (Exception e) {
			System.out.println("Test Connection Email failed");
		}
		
		

		return null;

	}
}
