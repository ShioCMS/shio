<?php

namespace Japode\Widget\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;
use Japode\PostType\Entity\JPPostTypeAttr;
use \JsonSerializable;

/**
 * Widget entity
 *
 * @Entity(repositoryClass="JPWidgetRepository")
 */
class JPWidget implements JsonSerializable {

    /**
     * @var string
     * @Id
     * @Column(type="string", length=40)
     */
    protected $id;

    /**
     * @var string
     * 
     * @Column(type="string", length=50)
     */
    protected $name;

    /**
     * @var string
     * 
     * @Column(type="string", length=100)
     */
    protected $description;

    /**
     * @var string
     * 
     * @Column(type="string", length=150)
     */
    protected $implementation_code;

    /**
     * @var string
     * 
     * @Column(type="string", length=50)
     */
    protected $type;

    /**
     * @var string
     * 
     * @Column(type="string", length=100)
     */
    protected $class_name;

    /**
     * @var JPPostTypeAttr[]
     *
     * @OneToMany(targetEntity="\Japode\PostType\Entity\JPPostTypeAttr", mappedBy="widget")
     */
    protected $postTypeAttrs;

    /**
     * Constructor
     */
    public function __construct() {
        $this->postTypeAttrs = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * Set id
     *
     * @param string $id
     * @return JPWidget
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
     * @return JPWidget
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
     * @return JPWidget
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
     * Set implementation_code
     *
     * @param string $implementationCode
     * @return JPWidget
     */
    public function setImplementationCode($implementationCode) {
        $this->implementation_code = $implementationCode;

        return $this;
    }

    /**
     * Get implementation_code
     *
     * @return string 
     */
    public function getImplementationCode() {
        return $this->implementation_code;
    }

    /**
     * Set type
     *
     * @param string $type
     * @return JPWidget
     */
    public function setType($type) {
        $this->type = $type;

        return $this;
    }

    /**
     * Get type
     *
     * @return string 
     */
    public function getType() {
        return $this->type;
    }

    /**
     * Set class_name
     *
     * @param string $className
     * @return JPWidget
     */
    public function setClassName($className) {
        $this->class_name = $className;

        return $this;
    }

    /**
     * Get class_name
     *
     * @return string 
     */
    public function getClassName() {
        return $this->class_name;
    }

    /**
     * Add postTypeAttrs
     *
     * @param \Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs
     * @return JPWidget
     */
    public function addPostTypeAttr(\Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs) {
        $this->postTypeAttrs[] = $postTypeAttrs;

        return $this;
    }

    /**
     * Remove postTypeAttrs
     *
     * @param \Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs
     */
    public function removePostTypeAttr(\Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs) {
        $this->postTypeAttrs->removeElement($postTypeAttrs);
    }

    /**
     * Get postTypeAttrs
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getPostTypeAttrs() {
        return $this->postTypeAttrs;
    }

    public function jsonSerialize() {
        return array(
            'id' => $this->getId(),
            'name' => $this->getName(),
            'description' => html_entity_decode(strip_tags($this->getDescription())),
            'implementation_code' => $this->getImplementationCode(),
            'type' => $this->getType()
        );
    }

}
