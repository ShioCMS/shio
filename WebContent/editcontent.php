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
<?php include 'menuheader.php' ?> 
<div class="page-header">
    <h1>Alexandre, <small>model your CTD1</small></h1>
  </div>
  <div class="row">
    <div class="span2">
	<?php include 'modelMenuLeft.php' ?>
    </div>
    <div class="span10">
	
	<?php 
	if (is_null($_GET["section"]))
		include 'modelBasic.php';
	else 
		include htmlspecialchars($_GET["section"]) .'.php'; ?>
  </div>
</div>
<!-- /container -->
<script src="http://code.jquery.com/jquery.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>
