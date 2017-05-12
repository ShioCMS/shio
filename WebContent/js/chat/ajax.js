function montaXMLHTTP(){
	try{
		myObj = new XMLHttpRequest()
	}catch(e){
		myObj = new ActiveXObject("Microsoft.XMLHTTP"); 
	}
	return myObj;
}