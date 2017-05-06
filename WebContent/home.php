<!DOCTYPE html>
<html lang="en" ng-app>
<head>
<meta charset="utf-8">
<title>Japode</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="js/angular.min.js"></script>
<script src="js/controllers.js"></script>
<style type="text/css">
body {
	padding-top: 00px;
	padding-bottom: 40px;
	background-color: #f5f5f5;
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
<div class="navbar">
  <div class="navbar-inner">
    <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".navbar-responsive-collapse"> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </a> <a class="brand" href="home.php">Japode</a>
      <div class="nav-collapse collapse navbar-responsive-collapse">
        <ul class="nav">
          <li class="active"><a href="home.php">Home</a></li>
          <li><a href="#">Sites</a></li>
          <li><a href="#">Stats</a></li>
        </ul>
        <form class="navbar-search pull-left" action="">
          <input type="text" class="search-query span2" placeholder="Search">
        </form>
        <ul class="nav pull-right">
          <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown">Settings <b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#">Action</a></li>
              <li><a href="#">Another action</a></li>
              <li><a href="#">Something else here</a></li>
              <li class="divider"></li>
              <li><a href="#">Separated link</a></li>
            </ul>
          </li>
        </ul>
      </div>
      <!-- /.nav-collapse -->
    </div>
  </div>
  <!-- /navbar-inner -->
</div>
<div class="pull-right">
	  <a href="home.php" class="btn disabled"><i class="icon-align-justify"></i></a>
        <a href="fullscreen.php" class="btn"><i class="icon-fullscreen"></i></a>
    </div>
<div class="page-header">
  <h1>Alexandre,<small> your content</small></h1>
</div>
<div class="alert alert-info">
  <button type="button" class="close" data-dismiss="alert">&times;</button>
  Welcome Alexandre, <br/>
  It is your first access, so you can create your own post and create your site too!<br/>
  <br/>
  Enjoy,<br/>
  Japode Team </div>
<div class="row">
  <div class="span2"> <a href="selectctd.php" class="btn btn-danger">New Post</a>
    <ul class="nav nav-list">
      <li><a href="#">Home</a></li>
      <li><a href="#">Starred <i class="icon-star"></i></a></li>
      <li><a href="#">All Posts</a></li>
      <li><a href="#">Trash <i class="icon-trash"></i></a></li>
    </ul>
    <ul class="nav nav-list" ng-controller="LabelListCtrl">
      <li class="nav-header">Label</li>
      <li ng-repeat="label in labels | filter:query | orderBy:orderProp"> <a href="#">{{label.title}}</a> </li>
    </ul>
  </div>
  <!-- /span2 -->
  <div class="span10">
    <ul class="pager">
      <li class="previous disabled"> <a href="#">&larr; Newer</a> </li>
      0-50 of 1,033 posts
      <li class="next"> <a href="#">Older &rarr;</a> </li>
    </ul>
    <table class="table table-hover" ng-controller="PostListCtrl">
      <tr ng-repeat="post in posts | filter:query | orderBy:orderProp">
        <td>{{post.content}}</td>
        <td><span class="label label-warning">{{post.label}}</span></td>
        <td>{{post.description}}</td>
        <td>{{post.date}}</td>
      </tr>
    </table>
    <ul class="pager">
      <li class="previous disabled"> <a href="#">&larr; Newer</a> </li>
      0-50 of 1,033 posts
      <li class="next"> <a href="#">Older &rarr;</a> </li>
    </ul>
  </div>
  <!-- /span9 -->
</div>
<!-- /container -->
<script src="http://code.jquery.com/jquery.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>