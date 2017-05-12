<?php
$gmtDate = gmdate("D, d M Y H:i:s");

header("Expires: {$gmtDate} GMT");
header("Last-Modified: {$gmtDate} GMT");
header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");

session_start();
/*
if (!$_SESSION['usu_nick']) {
    print 'Erro ao ler usuários, por favor reinicie o chat.';
    exit();
}

$nick = $_SESSION['usu_nick'];
 * 
 */
$sql = new Mysql;

//deleta usuários antigos
$sql->Consulta("DELETE FROM $tabela_usu  WHERE tempo < $tempovida");
//deleta mensagens antigas
$sql->Consulta("DELETE FROM $tabela_msg  WHERE tempo < $tempovida");

//seleciona usuários
$query = $sql->Consulta("SELECT id,nick,frase,cor FROM $tabela_usu ORDER BY id ASC");

$allusers = array();
// Todos os usuarios
$user = array('name' => "Todos", 'id' => "0", 'blocked' => FALSE);
array_push($allusers, $user);

//Lista de usuarios
while ($linha = mysql_fetch_array($query)) {

    $usuario = $linha['nick'];
    $id = $linha['id'];

    $usuario = htmlentities($usuario);

    $user = array('name' => $usuario, 'id' => $id, 'blocked' => TRUE);
    
    array_push($allusers, $user);
}

$arr = array('users' => $allusers);

echo json_encode($arr);

ob_end_flush();
?>
