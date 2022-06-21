package com.example.report_generator_spring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class REST_Controller {
	
	@Autowired
	user_repo ur;
	
	@Autowired
	product_repo pr;
	
	@Autowired
	product_revenue_repo prr;
	
	@EventListener(ApplicationReadyEvent.class)
	public void start() throws Exception
	{
		String[] data;
		product pt;
		Scanner f=new Scanner(new File("src/main/resources/products.csv"));
		f.nextLine();
		while(f.hasNextLine())
		{
			data=f.nextLine().split(",");
			pt=new product();
			pt.set_all(Integer.parseInt(data[0]),data[1],data[2],data[3],data[4],data[5],data[6]);
			pr.save(pt);
		}
		product_revenue ptr;
		f=new Scanner(new File("src/main/resources/product_revenue.csv"));
		f.nextLine();
		while(f.hasNextLine())
		{
			data=f.nextLine().split(",");
			ptr=new product_revenue();
			ptr.set_all(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(data[3]),Integer.parseInt(data[4]),Integer.parseInt(data[5]),data[6]);
			prr.save(ptr);
		}
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam String username,String password)
	{
		int i;
		ArrayList<user> user_list=(ArrayList<user>) ur.findAll();
		for(i=0;i<user_list.size();i++)
			if(user_list.get(i).username.equals(username))
			{
				if(user_list.get(i).password.equals(password))
					return "Logged in";
				return "Invalid Username/password";
			}
		return "User not found";
	}
	@RequestMapping("/register")
	public String register(@RequestParam String first_name,String last_name, String email,String username,String password) {
		int i;
		ArrayList<user> user_list=(ArrayList<user>) ur.findAll();
		for(i=0;i<user_list.size();i++)
		{
			if(user_list.get(i).username.equals(username))
				return "Username not available";
			if(user_list.get(i).email.equals(email))
				return "Email not available";
		}
		user new_user=new user();
		new_user.set_user((int)ur.count()+1,first_name,last_name,email,username,password);
		ur.save(new_user);
		return "Successful registration";
	}
	
	@RequestMapping("/full_report")
	public String report() throws Exception
	{
		Scanner f=new Scanner(new File("src/main/resources/product_revenue.csv"));
		String content="";
		while (f.hasNextLine()) 
		      content+=f.nextLine()+"\n";
		return content;
	}
	
	@RequestMapping("/send_mail")
	public String send_mail(@RequestParam String to,String subject,String body,String attachment) throws Exception
	{
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,new javax.mail.Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication("noreplyreport455@gmail.com","qwertyuiop@");
        	}
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("noreplyreport455@gmail.com"));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
        message.setSubject(subject);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Report.csv"));
        writer.write(attachment);
        writer.close();
        DataSource source = new FileDataSource("src/main/resources/Report.csv");
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("Report.csv");
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        Transport.send(message);
		return "Email sent";
	}
	
	@RequestMapping("/dashboard")
	public String dashboard(@RequestParam String product)
	{
		if(product.equals("All Products")) {
			List<Integer> year = prr.allyear();
			String fin="";
			for(Integer i: year)
				fin=fin+i+","+prr.allrevenue(i,1)+","+prr.allrevenue(i,2)+","+prr.allrevenue(i,3)+","+prr.allrevenue(i,4)+"\n";
			fin=fin.replaceAll("null","0");
			return fin;			
		}
		Integer id=pr.ProductId(product);
		List<Integer> year = prr.productyear(id);
		String fin="";
		for(Integer i: year)
			fin=fin+i+","+prr.revenue(i,1,id)+","+prr.revenue(i,2,id)+","+prr.revenue(i,3,id)+","+prr.revenue(i,4,id)+"\n";
		fin=fin.replaceAll("null","0");
		return fin;
	}
	
	@RequestMapping("/products")
	public String products()
	{
		String content="";
		ArrayList<product> p=(ArrayList<product>)pr.findAll();
		for(product prod:p)
			content+=prod.productname+",";
		return content.substring(0, content.length()-1);
	}
	
	@RequestMapping("/years")
	public String year(@RequestParam String product){
		List<Integer> year=prr.productyear(pr.ProductId(product));
		String content="";
		for(Integer i:year)
			content+=i+",";
		return content.substring(0, content.length()-1);
	}
	
	@RequestMapping("/generatereport")
	public String report(@RequestParam String product,String year,String quarter) {
		int id=pr.ProductId(product);
		int qt=Integer.parseInt(quarter.substring(8));
		int yr=Integer.parseInt(year);
		String result="Month,Revenue\n";
		String[] month= {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		result+=month[(qt-1)*3+1]+","+prr.revenuemonth(yr, qt,(qt-1)*3+1, id)+"\n";
		result+=month[(qt-1)*3+2]+","+prr.revenuemonth(yr, qt,(qt-1)*3+2, id)+"\n";
		result+=month[(qt-1)*3+3]+","+prr.revenuemonth(yr, qt,(qt-1)*3+3, id)+"\n";
		result+="Sum,"+prr.revenue(yr, qt, id)+"\n";
		return result;
	}
}
