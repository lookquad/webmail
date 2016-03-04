<%@page contentType="text/html"%>
<html>
<head>
<%@ page import="com.zaidsoft.webmail.*" %>
<%
     session.invalidate();
%>
      
    <title>WebMail :: Logout</title>
    
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>WebMail :: Login</title>

<link rel=stylesheet type="text/css" href="../UI-Resources/webmail-ui/css/login.css">
<link rel=stylesheet type="text/css" href="../UI-Resources/webmail-ui/css/WebmailBST.css">
<link rel=stylesheet type="text/css" href="../UI-Resources/bootstrap/css/bootstrap.min.css">
<link rel=stylesheet type="text/css" href="../UI-Resources/bootstrap/css/bootstrap-theme.min.css">


<script type="text/javascript" src="../UI-Resources/plugins/jQuery/jquery.min.js"></script>

</head>
  <body class="hold-transition default-skin sidebar-mini">
    <div class="container">

		
		<div class="jumbotron">
        <h1>WebMail</h1>
        <p class="lead">You have successfully logged out.</p>
        <p><a class="btn btn-lg btn-success" href="../index.jsp" role="button">Log in again</a></p>
      </div>
      
    </div><!-- ./wrapper -->
    <div class="footer navbar-default">
<jsp:include page="footer.jsp"></jsp:include>
</div>
    

    <!-- jQuery -->
     <script src="plugins/jQuery/jQuery-2.1.4.min.js"></script> 
    <!-- Bootstrap -->
    <script src="bootstrap/js/bootstrap.min.js"></script>



  </body>
</html>

