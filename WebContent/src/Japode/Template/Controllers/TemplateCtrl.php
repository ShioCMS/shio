<?php

namespace Japode\Template\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Response;

class TemplateCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/{filename}', array(new self(), 'template'))->bind('template');
    }

    public function template(Application $app, $filename) {
        $response = new Response($this->get_content($GLOBALS['japode_domain'] . "/jp-templating/" . $filename));
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');

        return $response;
    }

    /*
     * @return string
     * @param string $url
     * @desc Return string content from a remote file
     * @author Luiz Miguel Axcar (lmaxcar@yahoo.com.br)
     */

    function get_content($url) {
        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_HEADER, 0);

        ob_start();

        curl_exec($ch);
        curl_close($ch);
        $string = ob_get_contents();

        ob_end_clean();

        return $string;
    }

}
