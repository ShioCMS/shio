<?php

namespace Japode\Site\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;
use \JsonSerializable;
/**
 * Site entity
 *
 * @Entity
 */
class JPSite implements JsonSerializable {

    /**
     * @var string
     * @Id
     * @Column(type="guid", length=50)
     */
    protected $id;

    /**
     * @var string
     * @Column(type="string", length=100)
     */
    protected $name;

    /**
     * @var string
     * @Id
     * @Column(type="string", length=150)
     */
    protected $description;

    /**
     * @var string
     * @Id
     * @Column(type="string", length=255)
     */
    protected $url;

    /**
     * Set id
     *
     * @param string $id
     * @return JPSite
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
     * Set name
     *
     * @param string $name
     * @return JPSite
     */
    public function setName($name) {
        $this->name = $name;

        return $this;
    }

    /**
     * Get name
     *
     * @return string 
     */
    public function getName() {
        return $this->name;
    }

    /**
     * Set description
     *
     * @param string $description
     * @return JPSite
     */
    public function setDescription($description) {
        $this->description = $description;

        return $this;
    }

    /**
     * Get description
     *
     * @return string 
     */
    public function getDescription() {
        return $this->description;
    }

    /**
     * Set url
     *
     * @param string $url
     * @return JPSite
     */
    public function setUrl($url) {
        $this->url = $url;

        return $this;
    }

    /**
     * Get url
     *
     * @return string 
     */
    public function getUrl() {
        return $this->url;
    }

    public function jsonSerialize() {
        return array(
            'id' => $this->getId(),
            'name' => $this->getName(),
            'description' => $this->getDescription(),
            'url' => $this->getUrl()
        );
    }

}
