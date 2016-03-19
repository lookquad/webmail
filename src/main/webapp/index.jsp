<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>WebMail :: Login</title>

<link rel=stylesheet type="text/css" href="jsp/ui-resources/css/login.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">


<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.js"></script>
</head>


<body>
<!-- Showing the loader icon -->
	<div class="show-loader"><label> loading .... </label></div>
<div class="container">
    <div class="row complete-page">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center ">WebMail</h1>
            <h4 class="text-center login-title">Sign in to continue to WebMail</h4>
            <div class="account-wall">
                <!-- <img class="profile-img" src="" alt=""> -->
                 <form class="form-signin" action="WMAuthenticateUser" method = "POST">
                 
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
		
		
		
	
</div>


</div>

<!--  Error Show  -->
<% if(request.getAttribute("errorMessage") != null)
	{%>
	<br><br>
	<div class="col-sm-6 col-md-4 col-md-offset-4">
	<div class="alert alert-danger alert-dismissible text-center" role="alert">
     <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
     <strong>Error :: </strong> ${errorMessage}
    </div></div>
	<% }%>
<!--  END Error Show  -->

</div>

<div class="footer navbar-default">
<jsp:include page="jsp/footer.jsp"></jsp:include>
</div>

<script type="text/javascript">
		 $("#adv").click(function () {
	         $(this).text(function(i, v){
	            return v === 'Advanced Login' ? 'Go Back to Intelligent Login' : 'Advanced Login'
	         })
	     });
		
		
		</script>
				
		
		<script>
		$(document).ready(function(){
			$('.show-loader').hide();
			$( "form" ).submit(function() {
				$('.container').hide();
				$('.show-loader').show();
	
			})
		});
</script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>

</body>
</html>