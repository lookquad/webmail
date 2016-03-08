package com.zaidsoft.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.zaidsoft.webmail.IMAPBean;
import com.zaidsoft.webmail.POP3MailBean;
import com.zaidsoft.webmail.ResourceProvider;
import com.zaidsoft.webmail.SMTPBean;

/**
 * Servlet implementation class WM_Authenticate_User
 */

public class WM_Authenticate_User extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String errMsg = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WM_Authenticate_User() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		
		String login_mode;
		String flag =null;
		String id = null;
	    int nTry = 4;
	    String[] users = new String[nTry];
	    String mx=null;
	    
		try{
		HttpSession ses = request.getSession(true);
	     IMAPBean b = new IMAPBean();				// changed pop3 to imap bean to fix the bug:  com.zaidsoft.webmail.IMAPBean cannot be cast to com.zaidsoft.webmail.POP3MailBean in list.jsp
	     SMTPBean s = new SMTPBean();
	     
	     String email = request.getParameter("email");
	     String pass = request.getParameter("pass");
	     String host = request.getParameter("host");
	     String user = request.getParameter("user");
	     
	     
	     if(host == "" && user == "")
	     	{ login_mode = "intelligent"; }
	     else 
	    	{ login_mode = "advanced"; }
	     	
		
		
		if(login_mode == "intelligent")   // Normal login
		{
			System.out.println("Trying  INTELLIGENT MODE");
            int k = email.indexOf("@");
            if (k == -1) {
                return;
            }
            id = email.substring(0, k);
            host = email.substring(k + 1);
            
			try {
				mx = lookupDNS(host, "MX");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				flag = "DNS resolve Issue";
			}
            if (host.equals("gmail.com") || mx.contains("google")) {
                host = "imap.gmail.com";
            }
            else {
                 host = mx;
            }

            users[0] = email;
            users[1] = id + "." + host;
            users[2] = id + "-" + host;
            users[3] = id;
			
		}
		else if(login_mode == "advanced")    //Advance Login Mode
		{
			 System.out.println("Trying  ADVANCED MODE");
	         nTry = 1;
	         users[0] = user;
		}
		else
		{
			flag = "unknown error";
		}
		
		 
	        b.setStoreLocation(ResourceProvider.getResourceFilePath("mail", email));
	        s.setFromAddress(email);
	        for (int i = 0; i < nTry; i++) {
	            try {
	                user = users[i];
	                System.out.println("Mail Server: " + host + " User: " + user + " Pass: " + pass);
	                b.setServerInfo(host, user, pass);
	                //login is successfull return
	                ses.setAttribute("b", b);
	                ses.setAttribute("s", s);
	                ses.setAttribute("zaidsoft.webmail.UserLoggedIn", "true");
	                flag = "success";
	             //  response.sendRedirect("view_mail_list.jsp");   //lookquad
	              //  return;    // 
	            } catch (javax.mail.AuthenticationFailedException e) {
	                //e.printStackTrace(); // remove later 
	                errMsg = e.getMessage();
	                errMsg = "Authentication Failed.<br> Please check your email address and password. " + e.getMessage();
	                flag = "auth-fail";
	                //continue so that we can retry using altered data
	            } catch (MessagingException e) {
	                //e.printStackTrace(); // remove later
	                errMsg = "Could NOT connect to your mail server. Please provide correct server details <br> "+e.getMessage()+" <br> or contact your technical support provider." ;
	                flag = "connection-error";
	                //continue so that we can retry using altered data
	            }
	        }
		}catch(Exception e){flag = "error";}
	        
	    	if(flag == "success")
	    	{
	    		response.sendRedirect("jsp/list.jsp");
	    	}else if(flag == "auth-fail")
	    	{
	    		 request.setAttribute("errorMessage", errMsg);
	    		 RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    		 rd.forward(request,response);
	    		 
	    	}else if(flag == "connection-error")
	    	{
	    		 request.setAttribute("errorMessage", errMsg);
	    		 RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    		 rd.forward(request,response);
	    	}else
	    	{
	    		request.setAttribute("errorMessage", "Some Problem occered");
	    		RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
	    		 rd.forward(request,response);
	    	}
		
	}
	
	
	 private static String lookupDNS(String name, String recType) throws NamingException {
	        Hashtable environment = new Hashtable();
	        environment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
	        DirContext dnsCtx = new InitialDirContext(environment);
	        Attributes attribs = dnsCtx.getAttributes(name, new String[]{recType});
	        Attribute a = attribs.get(recType);
	        if (a == null) {
	            return null;
	        }
	        // return the first record
	        String s = a.get(0).toString();
	        return s.split(" ")[1].trim();
	    }
	 


}
