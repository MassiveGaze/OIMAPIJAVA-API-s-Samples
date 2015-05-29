package com.massiveGaze.mail;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class TestMail {
  public static void main(String[] args) throws Exception {
    Properties props = new Properties();
    // props.put("mail.smtp.host", "localhost");
    props.put("far05160k01.usdc2.oraclecloud.com", "no");
    Session sess = Session.getInstance(props, null);
    sess.setDebug(true);
    Message msg = new MimeMessage(sess);
    msg.setFrom(new InternetAddress("manjunath.banappagoudar@oracle.com"));
    msg.setRecipients(Message.RecipientType.TO,
          new InternetAddress[] {
              new InternetAddress("manjunath.banappagoudar@oracle.com") });
    msg.setSubject("This is message subject");
    msg.setContent("This is message body", "text/plain");
    Transport t = sess.getTransport("smtp");
    t.connect("far05160k01.usdc2.oraclecloud.com",25,"x","x");
    t.sendMessage(msg, msg.getAllRecipients());
  }
}