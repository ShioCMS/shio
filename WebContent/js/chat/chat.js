var conteudo = '';
var flood = 0;
var imgenvia = new Image();
imgenvia.src = 'icones/enviar.jpg';
var imgflood = new Image();
imgflood.src = 'icones/at.jpg';

//atualiza o estado atual do usuario
obj_estado = new montaXMLHTTP();
function setaestado() {

    var estado = document.getElementById("estado").value;

    obj_estado.open("POST", "estado.php", true);
    obj_estado.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    obj_estado.send("estado=" + estado);
    if (estado == '1') {
        document.getElementById("imgst").src = 'icones/on.gif';
    }
    if (estado == '2') {
        document.getElementById("imgst").src = 'icones/voltoja.gif';
    }
    if (estado == '3') {
        document.getElementById("imgst").src = 'icones/ausente.gif';
    }

    mensagem.value = "";
    mensagem.focus();

}

//atualiza o estilo atual do usuario
obj_estilo = new montaXMLHTTP();
function setaestilo() {

    var estilo = document.getElementById("estilo").value;

    obj_estilo.open("POST", "estilo.php", true);
    obj_estilo.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    obj_estilo.send("estilo=" + estilo);
    document.getElementById("estilo").value;
    mensagem.value = "";
    mensagem.focus();

}

function inicia() {
    document.getElementById("load").style.visibility = 'hidden';
    document.getElementById('mensagem').focus();
}

function VerificaMsg() {
    var mensagem = document.getElementById("mensagem").value;
    var espacos = mensagem.split(' ');

    if (espacos.length - 1 == mensagem.length) {
        setTimeout('Limpar()', 10);
    } else {
        Enviar();
    }

}
function Limpar() {
    document.getElementById("mensagem").value = '';
    document.getElementById("mensagem").focus();
}

obj_envia = new montaXMLHTTP();
function Enviar() {

    if (flood == 0) {
        var mensagem = document.getElementById("mensagem").value;
        var reservado = document.getElementById("reservado").value;
        var falacom = document.getElementById("falacom").value;
        var nick = document.getElementById("nick").value;
        var cor = document.getElementById("cor").value;
        var classe = 'separador';
        var textoreser = '';
        //limpa o campo
        setTimeout('Limpar()', 10);
        //smiles
        var mensagem_rep = mensagem;
        for (i = 1; i < 27; i++) {
            while (mensagem_rep.indexOf(':' + i + ':') != -1) {
                mensagem_rep = mensagem_rep.replace(':' + i + ':', '<img src="smiles/' + i + '.gif">');
            }
        }
        //envia mensagem
        if ((reservado != 0) && (falacom != 'Todos')) {
            classe = 'reservado';
            textoreser = 'reservadamente';
        } else {
            reservado = 0;
        }
        //mostra o texto antes de enviar
//        conteudo += '<div class="' + classe + '"><b><font color="' + cor + '">' + nick + '</font></b> <i><font color="#666666">' + textoreser + ' fala com</font></i> <b>' + falacom + '</b>:<br>' + mensagem_rep + '<br></div>';
          var newdiv = document.createElement("div");
        newdiv.className = classe;
        newdiv.innerHTML = '<b><font color="' + cor + '">' + nick + '</font></b> <i><font color="#666666">' + textoreser + ' fala com</font></i> <b>' + falacom + '</b>:<br>' + mensagem_rep + '<br>';
        document.getElementById("mostrar").appendChild(newdiv);
      
     //   document.getElementById("mostrar").innerHTML = conteudo;
        //envia a mensagem
   
        obj_envia.open("POST","/chat/api/room/ID/message/ID_USER/",true);
        obj_envia.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        obj_envia.send("mensagem=" + mensagem + "&falacom=" + falacom + "&reser=" + reservado + "&cor=" + cor);
        flood = 1;
        setTimeout('liberaflood()', 1300);
    } else {
        var tabela = '';
        tabela += '<table border="0" cellspacing="0" cellpadding="0" class="texto11"><tr>';
        tabela += '<td width="28"><img src="icones/at.jpg"></td>';
        tabela += ' <td width="350">O sistema anti-flood está ativado.</td>';
        tabela += '</tr></table>';
        setTimeout('retornafrase()', 5000);
    }
}

function liberaflood() {
    flood = 0;
}

function retornafrase() {
//    var frase = document.getElementById("salvafrase").value;
    var falacom = document.getElementById("falacom").value;
    if (falacom == 'Todos') {
        //       document.getElementById("exibefrase").innerHTML = frase;
    } else {
        if (frase != '') {
            //         document.getElementById("exibefrase").innerHTML = falacom + ': ' + frase;
        } else {
            document.getElementById("exibefrase").innerHTML = falacom;
        }
    }
}

function reenvia() {
    Ler();
}

obj_mostra = new montaXMLHTTP();
function Ler() {
    obj_mostra.open("GET", "ler.php", true);
    obj_mostra.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    obj_mostra.onreadystatechange = function() {
        if (obj_mostra.readyState == 4) {
//            conteudo += obj_mostra.responseText;
            var newcontent = document.createElement('div');
            newcontent.innerHTML = obj_mostra.responseText;

    while (newcontent.firstChild) {
        document.getElementById("mostrar").appendChild(newcontent.firstChild);
    }
//            document.getElementById("mostrar").innerHTML = conteudo;
            document.getElementById("mostrar").scrollTop = document.getElementById("mostrar").scrollHeight;
            clearTimeout(re);
            setTimeout("Ler()", 1000);
        }

    }
    obj_mostra.send(null);
    var re = setTimeout("reenvia()", 10000);
}

function enviasmile(img) {
    var texto = document.getElementById('mensagem').value;
    img = ' :' + img + ': ';
    document.getElementById('mensagem').value = texto + img;
    document.getElementById('mensagem').focus();
    document.getElementById("botenviar").innerHTML = '<a href="#" onClick="VerificaMsg()"><img src="icones/enviar.jpg" width="50" height="50" border="0"></a>';
}

function aba(id) {
    document.getElementById('int1').style.backgroundImage = "url('icones/branco.jpg')";
    document.getElementById('int2').style.backgroundImage = "url('icones/branco.jpg')";
    document.getElementById('int3').style.backgroundImage = "url('icones/branco.jpg')";
    document.getElementById('int' + id).style.backgroundImage = "url('icones/fundo.jpg')";
    if (id == 1) {
        intsmiles();
    }
    if (id == 2) {
        intimagem();
    }
    if (id == 3) {
        intcor();
    }
    document.getElementById("mensagem").focus();
}

function intsmiles() {
    var exibe = '';
    exibe += '<table width="221" border="0" cellspacing="1" cellpadding="0"><tr>';
    exibe += '<td width="20"><a href="#" onClick="enviasmile(1)"><img src="smiles/1.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td width="23"><a href="#" onClick="enviasmile(2)"><img src="smiles/2.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td width="40"><a href="#" onClick="enviasmile(3)"><img src="smiles/3.gif" width="35" height="20" border="0"></a></td>';
    exibe += '<td width="28"><a href="#" onClick="enviasmile(4)"><img src="smiles/4.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td width="20"><a href="#" onClick="enviasmile(5)"><img src="smiles/5.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td width="26"><a href="#" onClick="enviasmile(6)"><img src="smiles/6.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td width="28"><a href="#" onClick="enviasmile(7)"><img src="smiles/7.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td width="18"><a href="#" onClick="enviasmile(8)"><img src="smiles/8.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td width="18"><a href="#" onClick="enviasmile(9)"><img src="smiles/9.gif" width="20" height="20" border="0"></a></td>';
    exibe += '</tr><tr> ';
    exibe += '<td><a href="#" onClick="enviasmile(10)"><img src="smiles/10.gif" width="20" height="20"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(11)"><img src="smiles/11.gif" width="20" height="20"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(12)"><img src="smiles/12.gif" width="26" height="18"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(13)"><img src="smiles/13.gif" width="20" height="20"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(14)"><img src="smiles/14.gif" width="20" height="20"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(15)"><img src="smiles/15.gif" width="20" height="20"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(16)"><img src="smiles/16.gif" width="20" height="20"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(17)"><img src="smiles/17.gif" width="20" height="20"  border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(18)"><img src="smiles/18.gif" width="23" height="20"  border="0"></a></td>';
    exibe += '</tr><tr> ';
    exibe += '<td height="28"><a href="#" onClick="enviasmile(19)"><img src="smiles/19.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(20)"><img src="smiles/20.gif" width="20" height="20" border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(21)"><img src="smiles/21.gif" width="20" height="27" border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(22)"><img src="smiles/22.gif" width="28" height="28" border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(23)"><img src="smiles/23.gif" width="20" height="26" border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(24)"><img src="smiles/24.gif" width="26" height="23" border="0"></a></td>';
    exibe += '<td colspan="2"><a href="#" onClick="enviasmile(25)"><img src="smiles/25.gif" width="40" height="18" border="0"></a></td>';
    exibe += '<td><a href="#" onClick="enviasmile(26)"><img src="smiles/26.gif" width="20" height="20" border="0"></a></td>';
    exibe += '</tr></table>';
    document.getElementById("interacao").innerHTML = exibe;
}

function intimagem() {
    var reservado = document.getElementById("reservado").value;
    var falacom = document.getElementById("falacom").value;
    var cor = document.getElementById("cor").value;

    var exibe = '';
    exibe += '<form name="form1" enctype="multipart/form-data" method="post" action="envia_imagem.php" target="fupload" onSubmit="desabilita()">';
    exibe += '<table width="160" border="0" cellspacing="0" cellpadding="0"><tr><td height="32">';
    exibe += '<input type="file" name="imagem" class="form">';
    exibe += '</td></tr><tr>';
    exibe += '<td align="center"><input type="submit" name="Submit" value="Enviar" class="form">';
    exibe += '<input name="icor" type="hidden"  value="' + cor + '">';
    exibe += '<input name="ifalacom" type="hidden"  value="' + falacom + '">';
    exibe += '<input name="ireser" type="hidden"  value="' + reservado + '">';
    exibe += '</td>';
    exibe += '</tr></table></form>';
    document.getElementById("interacao").innerHTML = exibe;
}

function desabilita() {
    document.form1.Submit.disabled = true;
    document.form1.Submit.value = 'Enviando...';
}

function avisaimg(erro) {
    var falacom = document.getElementById("falacom").value;
    if (erro == 1) {
        erro = 'Imagem enviada para';
    }
    if (erro == 2) {
        erro = 'Falha ao enviar imagem para';
    }
    //conteudo += '<div class="separador"><i><font color="#666666">' + erro + '</font></i> <b>' + falacom + '</b></div>';
      var newdiv = document.createElement("div");
        newdiv.className = 'separador';
        newdiv.innerHTML = '<i><font color="#666666">' + erro + '</font></i> <b>' + falacom + '</b>';
        fler.document.getElementById("mostrar").appendChild(newdiv);
    //fler.document.getElementById("mostrar").innerHTML = conteudo;
}

function intcor() {
    var exibe = '';
    exibe += '<table width="210" border="0" cellspacing="1" cellpadding="0"><tr> ';
    exibe += '<td width="18" bgcolor="#000000" onClick="alteracor(1)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#00CC00" onClick="alteracor(2)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#330000" onClick="alteracor(3)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#666600" onClick="alteracor(4)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#660000" onClick="alteracor(5)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#CC0000" onClick="alteracor(6)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#CC6600" onClick="alteracor(7)" >&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#FF0000" onClick="alteracor(8)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#FF3300" onClick="alteracor(9)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#0000FF" onClick="alteracor(10)">&nbsp;</td>';
    exibe += '<td width="18" bgcolor="#003300" onClick="alteracor(11)">&nbsp;</td>';
    exibe += '</tr><tr>';
    exibe += '<td bgcolor="#333333" onClick="alteracor(12)">&nbsp;</td>';
    exibe += '<td bgcolor="#00CC33" onClick="alteracor(13)">&nbsp;</td>';
    exibe += '<td bgcolor="#330033" onClick="alteracor(14)">&nbsp;</td>';
    exibe += '<td bgcolor="#666633" onClick="alteracor(15)">&nbsp;</td>';
    exibe += '<td bgcolor="#660033" onClick="alteracor(16)">&nbsp;</td>';
    exibe += '<td bgcolor="#CC0033" onClick="alteracor(17)">&nbsp;</td>';
    exibe += '<td bgcolor="#CC6633" onClick="alteracor(18)">&nbsp;</td>';
    exibe += '<td bgcolor="#FF0033" onClick="alteracor(19)">&nbsp;</td>';
    exibe += '<td bgcolor="#FF6600" onClick="alteracor(20)">&nbsp;</td>';
    exibe += '<td bgcolor="#0033FF" onClick="alteracor(21)">&nbsp;</td>';
    exibe += '<td bgcolor="#006600" onClick="alteracor(22)">&nbsp;</td>';
    exibe += '</tr><tr> ';
    exibe += '<td bgcolor="#666666" onClick="alteracor(23)">&nbsp;</td>';
    exibe += '<td bgcolor="#00CC66" onClick="alteracor(24)">&nbsp;</td>';
    exibe += '<td bgcolor="#330066" onClick="alteracor(25)">&nbsp;</td>';
    exibe += '<td bgcolor="#666666" onClick="alteracor(26)">&nbsp;</td>';
    exibe += '<td bgcolor="#660066" onClick="alteracor(27)">&nbsp;</td>';
    exibe += '<td bgcolor="#CC0066" onClick="alteracor(28)">&nbsp;</td>';
    exibe += '<td bgcolor="#CC6666" onClick="alteracor(29)">&nbsp;</td>';
    exibe += '<td bgcolor="#FF0066" onClick="alteracor(30)">&nbsp;</td>';
    exibe += '<td bgcolor="#FF9900" onClick="alteracor(31)">&nbsp;</td>';
    exibe += '<td bgcolor="#0066FF" onClick="alteracor(32)">&nbsp;</td>';
    exibe += '<td bgcolor="#009900" onClick="alteracor(33)">&nbsp;</td>';
    exibe += '</tr></table>';
    document.getElementById("interacao").innerHTML = exibe;
}

function alteracor(cor) {
    var setacor = '';
    switch (cor) {
        case 1:
            setacor = '#000000';
            break;
        case 2:
            setacor = '#00CC00';
            break;
        case 3:
            setacor = '#330000';
            break;
        case 4:
            setacor = '#666600';
            break;
        case 5:
            setacor = '#660000';
            break;
        case 6:
            setacor = '#CC0000';
            break;
        case 7:
            setacor = '#CC6600';
            break;
        case 8:
            setacor = '#FF0000';
            break;
        case 9:
            setacor = '#FF3300';
            break;
        case 10:
            setacor = '#0000FF';
            break;
        case 11:
            setacor = '#003300';
            break;
        case 12:
            setacor = '#333333';
            break;
        case 13:
            setacor = '#00CC33';
            break;
        case 14:
            setacor = '#330033';
            break;
        case 15:
            setacor = '#666633';
            break;
        case 16:
            setacor = '#660033';
            break;
        case 17:
            setacor = '#CC0033';
            break;
        case 18:
            setacor = '#CC6633';
            break;
        case 19:
            setacor = '#FF0033';
            break;
        case 20:
            setacor = '#FF6600';
            break;
        case 21:
            setacor = '#0033FF';
            break;
        case 22:
            setacor = '#006600';
            break;
        case 23:
            setacor = '#666666';
            break;
        case 24:
            setacor = '#00CC66';
            break;
        case 25:
            setacor = '#330066';
            break;
        case 26:
            setacor = '#666666';
            break;
        case 27:
            setacor = '#660066';
            break;
        case 28:
            setacor = '#CC0066';
            break;
        case 29:
            setacor = '#CC6666';
            break;
        case 30:
            setacor = '#FF0066';
            break;
        case 31:
            setacor = '#FF9900';
            break;
        case 32:
            setacor = '#0066FF';
            break;
        case 33:
            setacor = '#009900';
            break;
    }
    var nick = document.getElementById("nick").value;
    document.getElementById("cor").value = setacor;
    document.getElementById("exibenick").innerHTML = '<strong><font color="' + setacor + '">' + nick + '</font></strong>';
    document.getElementById("mensagem").focus();
}

function frase() {
    var frase = document.getElementById("frase").value;
    if (frase != 'Volto logo, ausente ou frase pessoal') {
        obj_frase = new montaXMLHTTP();
        obj_frase.open("POST", "frase.php", true);
        obj_frase.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        obj_frase.send("frase=" + frase);
    }
}

function sair() {
    document.getElementById("textoload").innerHTML = '<strong>Saindo...</strong><br><br><img src="icones/load.gif" width="100" height="22"><br><br>';
    document.getElementById("load").style.visibility = 'Visible';
    window.top.location.href = 'sair.php';
}