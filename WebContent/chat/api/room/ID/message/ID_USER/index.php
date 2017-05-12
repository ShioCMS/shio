<?php
require($_SERVER['DOCUMENT_ROOT']. '/chat/class.mysql.php');
require($_SERVER['DOCUMENT_ROOT']. '/chat/config.inc.php');

$gmtDate = gmdate("D, d M Y H:i:s"); 

header("Expires: {$gmtDate} GMT"); 
header("Last-Modified: {$gmtDate} GMT"); 
header("Cache-Control: no-cache, must-revalidate"); 
header("Pragma: no-cache"); 
header('Content-type: application/json');

session_start();
if(!$_SESSION[usu_nick]){
	exit();	
}
	
$mensagem = $_POST['mensagem'];
$reser = $_POST['reserved'];
$falacom = $_POST['falacom'];
$cor = $_POST['cor'];
$nick = $_SESSION[usu_nick];

$falacom = utf8_decode($falacom);

$mensagem = strip_tags($mensagem);

//decodifica smiles
for($i = 1; $i < 27; $i++){
	$mensagem = str_replace(':'.$i.':', '<img src="smiles/'.$i.'.gif">', $mensagem);
}

//cadastra a mensagem
$sql = new Mysql;

$sql->Consulta("INSERT INTO $tabela_msg
(reservado,usuario,cor,msg,falacom,tempo)
VALUES 
('$reser','$nick','$cor','$mensagem','$falacom','$tempo_msg')"); 

$arr = array ('successful'=>TRUE);

echo json_encode($arr);
?>

