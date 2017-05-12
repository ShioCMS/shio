<?php

session_start();
/** Define ABSPATH as this file's directory */
define('ABSPATH', dirname(__FILE__) . '/');
require_once(ABSPATH . '/vendor/autoload.php');
require_once(ABSPATH . 'configuration.php');
require_once(ABSPATH . '/src/bootstrap.php');

$app = new Silex\Application();
$app['debug'] = true;
$app->register(new Silex\Provider\UrlGeneratorServiceProvider());
/*
$app->register(new Silex\Provider\DoctrineServiceProvider(), array(
    'db.options' => array(
        'dbname' => 'japode',
        'user' => 'japode',
        'password' => 'japode',
        'host' => 'localhost',
        'driver' => 'pdo_mysql',
    ),
));
// create session object and start it
$app->register(new Silex\Provider\SessionServiceProvider());

if (!$app['session']->isStarted()) {
    $app['session']->start();
}
*/

$app->register(new Silex\Provider\SwiftmailerServiceProvider());

$app->register(new Silex\Provider\TwigServiceProvider(), array(
    'twig.path' => ABSPATH . '/jp-views',
));
/*
$app->register(new Silex\Provider\SecurityServiceProvider(), array(
    'security.firewalls' => array(
        'login' => array(
            'pattern' => '^/(login).*$',
        ),
        'secure' => array(
            'anonymous' => false,
            'pattern' => '^/(content|post|widget|site|profile|signin).*$',
            'form' => array('login_path' => '/login', 'check_path' => '/signin/default/'),
            'logout' => array('logout_path' => '/signout'),
            'users' => $app->share(function () use ($app) {
                return new Japode\User\UserProvider($app['db']);
            }),
        ),
    )
));
// Boot your application
$app->boot();

$app['security.access_rules'] = array(
    array('^/post.*', 'ROLE_ADMIN'),
    array('^.*$', 'ROLE_USER'),
);
*/
$app['twig']->addGlobal('jp_domain', $GLOBALS['japode_domain']);
$app['swiftmailer.options'] = array(
    'host' => 'localhost',
    'port' => '25',
    'username' => 'alexandre.oliveira@gmail.com',
    'password' => 'geladeira33',
    'encryption' => null,
    'auth_mode' => null
);
$app['db.orm'] = $entityManager;
$app->mount('/signin', new Japode\Login\SignIn());
$app->mount('/signout/', new Japode\Login\SignOut());
$app->mount('/login/', new Japode\Login\Login());
$app->mount('/signup/', new Japode\Login\SignUp());
$app->mount('/content/', new Japode\Content\Content());
$app->mount('/services/', new Japode\Services\Services());
$app->mount('/post/type/', new Japode\PostType\PostType());
$app->mount('/post/', new Japode\Post\Post());
$app->mount('/widget/', new Japode\Widget\Widget());
$app->mount('/site/', new Japode\Site\Site());
$app->mount('/profile/', new Japode\Profile\Profile());
$app->mount('/template/', new Japode\Template\Template());
$app->mount('/lockdin', new OAuth2Demo\Server\Server());
$app->mount('/chat', new Japode\Chat\Chat());
$app->mount('/', new Japode\Social\Social());
// create an http foundation request implementing OAuth2\RequestInterface
$request = OAuth2\HttpFoundationBridge\Request::createFromGlobals();
$app->run($request);

