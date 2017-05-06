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
 <div class="navbar">
              <div class="navbar-inner">
                <div class="container">
                  <a class="btn btn-navbar" data-toggle="collapse" data-target=".navbar-responsive-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                  </a>
                  <a class="brand" href="home.php">Japode</a>
                  <div class="nav-collapse collapse navbar-responsive-collapse">
                    <ul class="nav">
                      <li class="active"><a href="home.php">Home</a></li>
                      <li><a href="#">Reader</a></li>
                      <li><a href="#">Stats</a></li>
                    </ul>
                    <form class="navbar-search pull-left" action="">
                      <input type="text" class="search-query span2" placeholder="Search">
                    </form>
                    <ul class="nav pull-right">
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Settings <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                          <li><a href="#">Action</a></li>
                          <li><a href="#">Another action</a></li>
                          <li><a href="#">Something else here</a></li>
                          <li class="divider"></li>
                          <li><a href="#">Separated link</a></li>
                        </ul>
                      </li>
                    </ul>
                  </div><!-- /.nav-collapse -->
                </div>
              </div><!-- /navbar-inner -->
            </div>
<div class="pull-right">
	  <a href="editcontent.php" class="btn">Or model a new Post Type</a>
    </div>
<div class="page-header">
  <h1>Alexandre, <small>What would you like to post?</small></h1>
</div>

<div class="row">
   <div class="span2">
      <div class="well"><a href="newcontent.php"><h3>Text</h3></a></div>

  </p>
   </div>
   <div class="span2">
      <div class="well"><a href="newcontent.php"><h3>Photo</h3></div>
   </div>
   <div class="span2">
	   <div class="well"><a href="newcontent.php"><h3>Video</h3></div>
   </div>
    <div class="span2">
	   <div class="well"><a href="newcontent.php"><h3>Quote</h3></a></div>
   </div>
   <div class="span2">
	   <div class="well"><a href="newcontent.php"><h3>Link</h3></a></div>
   </div>
   <div class="span2">
	   <div class="well"><a href="newcontent.php"><h3>File</h3></a></div>
   </div>    
</div>

<div class="row">
   <div class="span4">
      <div class="well">
       <a href="editcontent.php"><i class="icon-pencil close"></i></a>
      <a href="newcontent.php"><h1>CTD1<h1><small> Conteúdo Simples</small></a></div>

  </p>
   </div>
   <div class="span4">
      <div class="well">
      <a href="editcontent.php"><i class="icon-pencil close"></i></a>
      <a href="newcontent.php"><h1>Notícia<h1><small>Notícias para seu site</small></a></div>
   </div>
   <div class="span4">
	   <div class="well">
	   <a href="editcontent.php"><i class="icon-pencil close"></i></a>
	   <a href="newcontent.php"><h1>FAQ<h1><small>Perguntas e respostas</small></a></div>
   </div>
</div>
<div class="row">
   <div class="span4">
	   
	   <div class="well">
	   <a href="editcontent.php"><i class="icon-pencil close"></i></a>
	   <a href="newcontent.php"><h1>Video<h1><small>Videos do Youtube</small></a></div>
   </div>
   <div class="span4">

	    <div class="well">
		<a href="editcontent.php"><i class="icon-pencil close"></i></a>
	    <a href="newcontent.php"><h1>Fornecedor<h1><small>Cadastre seus fornecedores</small></a></div>
   </div>
   <div class="span4">
	    <div class="well">
		<a href="editcontent.php"><i class="icon-pencil close"></i></a>   	    
	    <a href="newcontent.php"><h1>Cliente<h1><small>Clientes da Empresa</small></a></div>
   </div>
</div>
</div>
  <script src="http://code.jquery.com/jquery.js"></script>
  <script src="js/bootstrap.min.js"></script>
</body>
</html>