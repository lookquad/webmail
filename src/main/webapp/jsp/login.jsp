<<<<<<< HEAD
<%@page contentType="text/html"%> 
<%@ page import="com.zaidsoft.webmail.*" %>
<%
    boolean adv = request.getParameter("advMode") != null;
    String email = request.getParameter("email");
    if (email == null ) email = "";
    String pass = request.getParameter("pass");
    if (pass == null ) pass = "";
    String host = request.getParameter("host");
    if (host == null ) host = "";
    String user = request.getParameter("user");
    if (user == null ) user = "";
    String submit = request.getParameter("ok");
    String msg = "";
    if (submit != null ) {
        try{
          WebMailAuthenticator.authenticate(request, response);
          response.sendRedirect("view_mail_list.jsp");
          return;
       }
       catch (Exception e){ 
            msg = e.getMessage();
       }
    } 

%>
=======
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
>>>>>>> refs/remotes/origin/NewLogin
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>WebMail :: Login</title>

<link rel=stylesheet type="text/css" href="../WM-UI-Resources/WM-Resources/css/login.css">
<link rel=stylesheet type="text/css" href="../WM-UI-Resources/bootstrap/css/bootstrap.min.css">
<link rel=stylesheet type="text/css" href="../WM-UI-Resources/bootstrap/css/bootstrap-theme.min.css">


<script type="text/javascript" src="../WM-UI-Resources/plugins/jQuery/jquery.min.js"></script>
<script type="text/javascript" src="../WM-UI-Resources/plugins/jQuery/jquery.validate.min.js"></script>
</head>


<body>

<div class="container">
    <div class="row complete-page">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center ">WebMail</h1>
            <h4 class="text-center login-title">Sign in to continue to WebMail</h4>
            <div class="account-wall">
                <!-- <img class="profile-img" src="" alt=""> -->
                <form class="form-signin" action="WMAuth" method = "POST">
                 
    			<label class="lab" for="exampleInputEmail">Email address</label>
    			
    			<div class="form-group has-feedback">
            <input type="email" class="form-control" name="email" placeholder="yourname@example.com" required  autofocus>
            <span class="glyphicon glyphicon-envelope form-control-feedback glyph-color"></span>
          </div>
    			
                <!-- <input type="email" class="form-control" name="email" placeholder="yourname@example.com" required  autofocus >
                 <span class="glyphicon glyphicon-envelope form-control-feedback"></span> -->
                
                <div class="collapse out" id="adv-login">
                <label class="lab" for="exampleInputEmailServer">Mail server</label>
                
                <div class="form-group has-feedback">
            <input type="text" class="form-control" name="host" placeholder="imap.example.com">
            <span class="glyphicon glyphicon-globe form-control-feedback glyph-color"></span>
          </div>
                
                <!-- <input type="text" class="form-control" name="host" placeholder="ex: imap.example.com"> -->
                <label class="lab" for="exampleInputUserName">User Name</label>
                
                <div class="form-group has-feedback">
            <input type="email" class="form-control" name="user" placeholder="yourname@example.com">
            <span class="glyphicon glyphicon-user form-control-feedback glyph-color"></span>
          </div>
                
                <!-- <input type="text" class="form-control" name="user" placeholder="yourname@example.com"> -->
                </div>   <!-- adv-login ends -->
                
                <div class="form-group has-feedback">
                <label class="lab" for="exampleInputPassword">Password</label>
            <input type="password" class="form-control" name="pass" placeholder="*************" required>
            <span class="glyphicon glyphicon-lock form-control-feedback glyph-color"></span>
          </div>
                
                
                <!-- <label for="exampleInputPassword">Password</label>
                <input type="password" class="form-control" name="pass" placeholder="Password" required > -->
                <button class="btn btn-lg btn-primary btn-block" type="submit"> Sign in</button>
               <!--  <label class="checkbox pull-left">
                    <input type="checkbox" value="remember-me">
                    Remember me
                </label> 
                <a href="#" class="pull-right need-help">Need help? </a><span class="clearfix"></span>-->
                </form>
            </div>
		
		<a class="pull-left" id="adv" role="button" data-toggle="collapse" href="#adv-login" aria-expanded="false" aria-controls="collapseExample">Advanced Login</a>
		<!-- <button class="btn btn-primary" id="adv" type="button" data-toggle="collapse" data-target="#adv-login" aria-expanded="false" aria-controls="collapseExample">Advanced Login</button> -->
		<script type="text/javascript">
		 $("#adv").click(function () {
	         $(this).text(function(i, v){
	            return v === 'Advanced Login' ? 'Go Back to Intelligent Login' : 'Advanced Login'
	         })
	     });
		
		
		</script>
	
</div>
<%-- <div style="color:red">${errorMessage}</div> --%>


</div>
<!--  Error Show  -->
<% if(request.getAttribute("errorMessage") != null)
	{%>
	<br><br>
	<div class="col-sm-6 col-md-4 col-md-offset-4">
	<div class="alert alert-danger alert-dismissible text-center" role="alert">
     <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
     <strong>Error :: </strong> ${errorMessage} <!-- JSP EL expression language  -->
    </div></div>
	<% }%>
<!--  END Error Show  -->

</div>

<div class="footer navbar-default">
<jsp:include page="footer.jsp"></jsp:include></div>

<script type="text/javascript" src="../WM-UI-Resources/bootstrap/js/bootstrap.min.js"></script>

</body>
</html>