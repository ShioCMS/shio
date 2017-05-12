<?php

namespace Japode\User\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;

/**
 * User entity
 *
 * @Entity(repositoryClass="JPUserRepository")
 */
class JPUser{

    /**
     * @var string
     * @Id
     * @Column(type="string", length=255)
     */
    protected $id;

    /**
     * @var string
     *
     * @Column(type="string", length=20)
     */
    protected $realm;

    /**
     * @var string
     *
     * @Column(type="string", length=100)
     */
    protected $firstName;

    /**
     * @var string
     *
     * @Column(type="string", length=100, nullable=true)
     */
    protected $lastName;

    /**
     * @var string
     *
     * @Column(type="string", length=255, nullable=true)
     */
    protected $email;

    /**
     * @var string
     *
     * @Column(type="string", length=50, nullable=true)
     */
    protected $username;

    /**
     * @var string
     *
     * @Column(type="string", length=50, nullable=true)
     */
    protected $password;

    /**
     * @var \DateTime
     *
     * @Column(type="datetime", nullable=true)
     */
    protected $lastLogin;

    /**
     * @var string
     *
     * @Column(type="string", length=40, nullable=true)
     */
    protected $lastPostType;

    /**
     * @var int
     *
     * @Column(type="integer", nullable=true)
     */
    protected $loginTimes;

    /**
     * @var string
     *
     * @Column(type="string", length=40, nullable=true)
     */
    protected $confirmEmail;

    /**
     * @var string
     *
     * @Column(type="string", length=40, nullable=true)
     */
    protected $recoverPassword;

    /**
     * Set id
     *
     * @param string $id
     * @return JPUser
     */
    public function setId($id) {
        $this->id = $id;

        return $this;
    }

    /**
     * Get id
     *
     * @return string 
     */
    public function getId() {
        return $this->id;
    }

    /**
     * Set realm
     *
     * @param string $realm
     * @return JPUser
     */
    public function setRealm($realm) {
        $this->realm = $realm;

        return $this;
    }

    /**
     * Get realm
     *
     * @return string 
     */
    public function getRealm() {
        return $this->realm;
    }

    /**
     * Set firstName
     *
     * @param string $firstName
     * @return JPUser
     */
    public function setFirstName($firstName) {
        $this->firstName = $firstName;

        return $this;
    }

    /**
     * Get firstName
     *
     * @return string 
     */
    public function getFirstName() {
        return $this->firstName;
    }

    /**
     * Set lastName
     *
     * @param string $lastName
     * @return JPUser
     */
    public function setLastName($lastName) {
        $this->lastName = $lastName;

        return $this;
    }

    /**
     * Get lastName
     *
     * @return string 
     */
    public function getLastName() {
        return $this->lastName;
    }

    /**
     * Set email
     *
     * @param string $email
     * @return JPUser
     */
    public function setEmail($email) {
        $this->email = $email;

        return $this;
    }

    /**
     * Get email
     *
     * @return string 
     */
    public function getEmail() {
        return $this->email;
    }

    /**
     * Set username
     *
     * @param string $username
     * @return JPUser
     */
    public function setUsername($username) {
        $this->username = $username;

        return $this;
    }

    /**
     * Get username
     *
     * @return string 
     */
    public function getUsername() {
        return $this->username;
    }

    /**
     * Set password
     *
     * @param string $password
     * @return JPUser
     */
    public function setPassword($password) {
        $this->password = $password;

        return $this;
    }

    /**
     * Get password
     *
     * @return string 
     */
    public function getPassword() {
        return $this->password;
    }

    /**
     * Set lastLogin
     *
     * @param \DateTime $lastLogin
     * @return JPUser
     */
    public function setLastLogin($lastLogin) {
        $this->lastLogin = $lastLogin;

        return $this;
    }

    /**
     * Get lastLogin
     *
     * @return \DateTime 
     */
    public function getLastLogin() {
        return $this->lastLogin;
    }

    /**
     * Set lastPostType
     *
     * @param string $lastPostType
     * @return JPUser
     */
    public function setLastPostType($lastPostType) {
        $this->lastPostType = $lastPostType;

        return $this;
    }

    /**
     * Get lastPostType
     *
     * @return string 
     */
    public function getLastPostType() {
        return $this->lastPostType;
    }

    /**
     * Set loginTimes
     *
     * @param integer $loginTimes
     * @return JPUser
     */
    public function setLoginTimes($loginTimes) {
        $this->loginTimes = $loginTimes;

        return $this;
    }

    /**
     * Get loginTimes
     *
     * @return integer 
     */
    public function getLoginTimes() {
        return $this->loginTimes;
    }

    /**
     * Set confirmEmail
     *
     * @param string $confirmEmail
     * @return JPUser
     */
    public function setConfirmEmail($confirmEmail) {
        $this->confirmEmail = $confirmEmail;

        return $this;
    }

    /**
     * Get confirmEmail
     *
     * @return string 
     */
    public function getConfirmEmail() {
        return $this->confirmEmail;
    }

    /**
     * Set recoverPassword
     *
     * @param string $recoverPassword
     * @return JPUser
     */
    public function setRecoverPassword($recoverPassword) {
        $this->recoverPassword = $recoverPassword;

        return $this;
    }

    /**
     * Get recoverPassword
     *
     * @return string 
     */
    public function getRecoverPassword() {
        return $this->recoverPassword;
    }

    public function getImageProfile($type = 'large') {
        if ($this->getRealm() == 'default') {
            $imageprofile = "http://www.gravatar.com/avatar/" . md5(strtolower(trim($this->getEmail()))) . ".php";
        } else if ($this->getRealm() == 'facebook') {
            $imageprofile = "http://graph.facebook.com/" . $this->getId() . "/picture?type=" . $type;
        }

        return $imageprofile;
    }
   
}
