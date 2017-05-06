<!DOCTYPE html>
<html lang="en" ng-app>
<head>
<meta charset="utf-8">
<title>Japode</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<style>
/* Make an input blend into its parent */
input.inline-edit{
  /* Eliminate borders and padding */
  border: none;
  padding: 0;
  margin: 0;

  /* Inherit the parent element's typography */
  font: inherit;
 /* color: inherit; */
  line-height: inherit;
  font-size: inherit;
  text-align: inherit;

  /* Seems to help alignment in headers */
  vertical-align: top;
}

/* Add interaction cues on hover and focus */
input.inline-edit:hover,
input.inline-edit:focus{
  /* Change the background to a light yellow */
  background-color: #FFD;

  /* A subtle transition never hurts */
  -webkit-transition: background-color 0.5s;
     -moz-transition: background-color 0.5s;
      -ie-transition: background-color 0.5s;
          transition: background-color 0.5s;
}
</style>
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="js/angular.min.js"></script>
<script src="js/controllers.js"></script>
<script src="http://code.jquery.com/jquery.js"></script>
<script src="js/bootstrap.min.js"></script>
<style type="text/css">
body {
	padding-top: 0px;
	padding-bottom: 40px;
}
.jpcontent:hover { 
border-style:dashed;
border-color: red; }

#jpeditcontent {
	display: none;
}
.jpformcontent {
	display: none;
}
.jpcontent:hover #jpeditcontent {
	display: block;

}
</style>
<link href="css/bootstrap-responsive.css" rel="stylesheet">
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
      <![endif]-->
</head>
<body>
<div class="container">
<?php include 'menuheader.php' ?> 
<img src="https://fbcdn-profile-a.akamaihd.net/hprofile-ak-prn1/565114_745168347_804327172_q.jpg">&nbsp;&nbsp;&nbsp;
<div class="btn-group">
	<div class="btn-group">
		<button class="btn dropdown-toggle" data-toggle="dropdown">My Profiles <span class="caret"></span></button>
		<ul class="dropdown-menu">
    		<li><a href="#">Alexandre Oliveira...</a></li>
    		<li><a href="#">Alexandre Oli...</a></li>
    		<li><a href="#">Alê: o Surfista...</a></li>
    		<li><a href="#">O Capitalista..</a></li>
    		<li class="divider"></li>
    		<li><a href="#">Add other profile..</a></li>    		    		    		
  		</ul>
  	</div>
<button class="btn btn-primary">Alexandre Oliveira</button>
<button class="btn-large btn-info disabled">Alexandre Oli</button>
<button class="btn btn-success">Alê: o Surfista</button>
<button class="btn btn-inverse">...</button>
<button class="btn btn-inverse">+</button>
</div>
<div class="pull-right">
	  <a href="home.php" class="btn"><i class="icon-align-justify"></i></a>
        <a href="fullscreen.php" class="btn disabled"><i class="icon-fullscreen"></i></a>
    </div>
<div>
<hr>
<table>
<tr>
<td valign="top" width="80px" height="170px">
</td>
  <td>
  <textarea rows="4" class="field span7" placeholder="What you think?"></textarea>
  <br>
  <button type="submit" class="btn btn-primary">Post</button>
  <button type="button" class="btn">Post and Publish</button>
</td>
<td width="200px">  <a href="#" id="ret" data-toggle="popover" title="" data-content="Aqui, outros tipos de posts serão exibidos, além de expandir e mostrar os campos opcionais do conteúdo atual" data-original-title="Post Types"><img src="http://felipegodoy.files.wordpress.com/2011/07/reticencia.jpg" width="50%" height="50%"></a>
<br>
<br>
<br>

</td>
</tr>
</table>
<div class="row">
  <div class="span12">
  <ul class="nav nav-tabs">
  <li class="active">
	<a href="#">Published</a></li>
  <li><a href="#">Unpublished (3)</a></li>
</ul>
    <div ng-controller="PostListCtrl">
        	   			      			
    	<div class="row" ng-repeat="post in posts | filter:query | orderBy:orderProp">
    		<div class="span1">
    		</div>
   			<div class="span8">

      			<div id="jpcontent" class="jpcontent well">

			      <div id="jpeditcontent">
      				<a href="javascript:myFunc();"><i class="icon-pencil close"></i></a>
      			</div>
      			<div class="pull-right">
   					<em><input class='inline-edit' value='{{post.date}}'></em>
   				</div>
   				<table>
   				<tr>
   				<td>
   				<img src="https://fbcdn-profile-a.akamaihd.net/hprofile-ak-prn1/565114_745168347_804327172_q.jpg">&nbsp;&nbsp;&nbsp;
   				</td>
   				<td>
      			<h3><input class='inline-edit' value='{{post.content}}'></h3>
      			</td>
      			</tr>
   				</table>
      			<div id="example-one" contenteditable="true">
					<style scoped>
				  		#example-one { margin-bottom: 10px; }
				  		[contenteditable="true"] { padding: 10px;}
				  		[contenteditable="true"]:hover { background-color: #FFD;

  /* A subtle transition never hurts */
  -webkit-transition: background-color 0.5s;
     -moz-transition: background-color 0.5s;
      -ie-transition: background-color 0.5s;
          transition: background-color 0.5s; }
					</style>
		      		<p>{{post.description}}</b></p>   
		      	</div>
      		</div>
      		<div id="jpformcontent" class="jpformcontent well">
				<form action="home.php">
			    	<label>Title</label>
    				<input type="text" id="field1" name="field1" value="{{post.content}}">
    				<label>Descrição</label>
    				<textarea>{{post.description}}</textarea>
    				<label>Date</label>
    				<input type="text" id="field1" name="field1" value="{{post.date}}">
					<div class="form-actions">
    	  				<button type="submit" onClick="javascript:finishEdit();" class="btn btn-primary">Save changes</button>
      					<button type="button" onClick="javascript:finishEdit();" class="btn">Cancel</button>
    				</div>
    			</form>
  			</div>
      	</div>
	</div>
  </div>
  <!-- /span9 -->
</div>
<!-- /container -->
<script>
/*
$(function() {
   var links = $('a').click(function() {
  $('#jpformcontent').css('display', 'block');
   });
});
*/
function myFunc()
{
$('#jpformcontent').css('display', 'block');
$('#jpcontent').css('display', 'none');
}

function finishEdit()
{
$('#jpcontent').css('display', 'block');
$('#jpformcontent').css('display', 'none');
}
</script>
<script>  
$(function ()  
{ $("#ret").popover();  
});  
</script>  
</body>
</html>