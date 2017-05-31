package com.andreistraut.freelancerwar.sbfw;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author straut
 */
public class ContactServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	
	response.setContentType("text/plain;charset=UTF-8");
	
	PrintWriter out = response.getWriter();
	try {
	    Properties contactProperties = new Properties();
	    contactProperties.load(this.getClass().getResourceAsStream(
		    "/com/andreistraut/freelancerwar/sbfw/config/config.properties"));
	    String requestName = request.getParameter("name");
	    String requestPhone = request.getParameter("phone");
	    String requestEmail = request.getParameter("email");
	    String requestMessage = request.getParameter("message");
	    
	    String host = contactProperties.getProperty("host");
	    String mailSubject = String.format(contactProperties.getProperty("emailSubject"), requestName);
	    String mailBody = String.format(contactProperties.getProperty("emailBody"), 
		    requestName, requestEmail, requestPhone, requestMessage);
	    String from = "no-reply@andreistraut.info";
	    
	    Properties mailProperties = new Properties();
	    mailProperties.setProperty("mail.smtp.host", host);
	    mailProperties.setProperty("mail.smtp.port", "25");
	    Session session = Session.getInstance(mailProperties, null);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "NoReply"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(contactProperties.getProperty("to"), "Mr. Recipient"));
            msg.setSubject(mailSubject);
            msg.setText(mailBody);
            Transport.send(msg);
	    
	    response.setStatus(200);
	} catch (IOException | MessagingException e) {
	    Logger.getLogger(com.andreistraut.freelancerwar.sbfw.ContactServlet.class.getName())
		    .log(Level.SEVERE, "Error occured", e);
	    out.println(String.format("An unknown exception has occurred: %s", e.getMessage()));
	    response.setStatus(500);
	}
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	processRequest(request, response);
    }

}
