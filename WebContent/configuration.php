<?php
function startsWith($haystack, $needle)
{
    return $needle === "" || strpos($haystack, $needle) === 0;
}
function endsWith($haystack, $needle)
{
    return $needle === "" || substr($haystack, -strlen($needle)) === $needle;
}

if (endsWith($_SERVER['HTTP_HOST'], 'japode.prj.viglet.co')) {
    $japode_domain = "japode.prj.viglet.co";
    $japode_context = "";
    $database_hostname = "localhost";
    $database_name = "japode";
    $database_username = "japode";
    $database_password = "japode";
    
    #Facebook
    $facebook_appid = "536233886484089";
    $facebook_appsecret = "84dbf875058142baac5e9f55251cecc8";
    $facebook_redirect = "http://japode.prj.viglet.co/signin/fb/";

    
} else {
    $japode_domain = "www.japode.com";
    $japode_context = "";
    $database_hostname = "localhost";
    $database_name = "japode";
    $database_username = "japode";
    $database_password = "J951061202";
    
    #Facebook
    $facebook_appid = "601877426548698";
    $facebook_appsecret = "7797fe8e3223e52793fb349f8691011a";
    $facebook_redirect = "http://www.japode.com/signin/fb/";
}

define( 'JPWIDGETABLE', 'jp_ctd_widget' );
?>
