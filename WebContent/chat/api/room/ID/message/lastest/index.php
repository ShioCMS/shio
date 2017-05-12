<?php
$gmtDate = gmdate("D, d M Y H:i:s"); 

header('Content-type: application/json');
header("Expires: {$gmtDate} GMT"); 
header("Last-Modified: {$gmtDate} GMT"); 
header("Cache-Control: no-cache, must-revalidate"); 
header("Pragma: no-cache"); 

session_start();
if(!$_SESSION['usu_nick']){
	print 'Erro ao ler mensagens, por favor reinicie o chat.';
	exit();	
}

if(empty($_SESSION['Ultimo_id'])){
	$_SESSION['Ultimo_id'] = 0;
}

$ultimo_id = $_SESSION['Ultimo_id'];
$nick = $_SESSION['usu_nick'];

$sql = new Mysql;

//atualiza tempo usuário
$sql->Consulta("UPDATE $tabela_usu SET tempo='$tempo_usu' WHERE nick = '$nick' LIMIT 1"); 

//ler as mensagens
$query = $sql->Consulta("SELECT id,reservado,usuario,msg,falacom,cor
FROM $tabela_msg
 WHERE usuario != '$nick' AND id > $ultimo_id ORDER BY id ASC");

$allmessages = array();
while ($linha = mysql_fetch_array($query)){  

	$usuario = $linha['usuario'];
	$mensagem = $linha['msg'];
	$reservado = $linha['reservado'];
	$cor = $linha['cor'];
	$falacom = $linha['falacom'];
	
	$_SESSION['Ultimo_id'] = $linha['id'];
	
	$mensagem = ereg_replace("[[:alpha:]]+://[^<>[:space:]]+[[:alnum:]/]", "<a href=\"\\0\" target='_blank'>\\0</a>", $mensagem);
		
	$usuario = htmlentities($usuario);
        if ($reservado != 0)
            $breservado = TRUE;
        else 
            $breservado = FALSE;
        
	$message = array ('name'=>$usuario,'id' =>'ID','message' =>$mensagem,'reserved' => $breservado);				
        array_push($allmessages, $message);        
}

$arr = array('messages' => $allmessages);
echo json_encode($arr);

ob_end_flush();
?>