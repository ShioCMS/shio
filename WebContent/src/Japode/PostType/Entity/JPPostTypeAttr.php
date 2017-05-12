<?php

namespace Japode\PostType\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;
use Japode\Widget\Entity\JPWidget;
use Japode\Post\Entity\JPPostAttr;

/**
 * PostTypeAttrs entity
 *
 * @Entity(repositoryClass="JPPostTypeAttrRepository")
 */class JPPostTypeAttr {

    /**
     * @var string
     * @Id
     * @Column(type="string", length=40)
     */
    protected $id;

    /**
     * @var JPPostType
     *
     * @ManyToOne(targetEntity="JPPostType", inversedBy="postTypeAttrs")
     */
    protected $postType;

    /**
     * @var JPWidget
     *
     * @ManyToOne(targetEntity="\Japode\Widget\Entity\JPWidget", inversedBy="postTypeAttrs")
     */
    protected $widget;

    /**
     * @var string
     *
     * @Column(type="string", length=50, nullable=true)
     */
    protected $name;

    /**
     * @var string
     *
     * @Column(type="string", length=50)
     */
    protected $label;

    /**
     * @var string
     *
     * @Column(type="string", length=100, nullable=true)
     */
    protected $description;

    /**
     * @var bool
     *
     * @Column(type="boolean")
     */
    protected $isTitle;

    /**
     * @var bool
     *
     * @Column(type="boolean")
     */
    protected $isSummary;

    /**
     * @var int
     *
     * @Column(type="integer")
     */
    protected $position;

    /**
     * @var bool
     *
     * @Column(type="boolean", nullable=true)
     */
    protected $many;

    /**
     * @var bool
     *
     * @Column(type="boolean", nullable=true)
     */
    protected $required;

    /**
     * @var \Japode\Post\Entity\JPPostAttr[]
     *
     * @OneToMany(targetEntity="\Japode\Post\Entity\JPPostAttr", mappedBy="post_type_attr")
     */
    protected $post_attrs;

    /**
     * Set id
     *
     * @param string $id
     * @return JPPostTypeAttr
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
     * @return JPPostTypeAttr
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
     * Set label
     *
     * @param string $label
     * @return JPPostTypeAttr
     */
    public function setLabel($label) {
        $this->label = $label;

        return $this;
    }

    /**
     * Get label
     *
     * @return string 
     */
    public function getLabel() {
        return $this->label;
    }

    /**
     * Set description
     *
     * @param string $description
     * @return JPPostTypeAttr
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
     * Set isTitle
     *
     * @param boolean $isTitle
     * @return JPPostTypeAttr
     */
    public function setIsTitle($isTitle) {
        $this->isTitle = $isTitle;

        return $this;
    }

    /**
     * Get isTitle
     *
     * @return boolean 
     */
    public function getIsTitle() {
        return $this->isTitle;
    }

    /**
     * Set isSummary
     *
     * @param boolean $isSummary
     * @return JPPostTypeAttr
     */
    public function setIsSummary($isSummary) {
        $this->isSummary = $isSummary;

        return $this;
    }

    /**
     * Get isSummary
     *
     * @return boolean 
     */
    public function getIsSummary() {
        return $this->isSummary;
    }

    /**
     * Set position
     *
     * @param integer $position
     * @return JPPostTypeAttr
     */
    public function setPosition($position) {
        $this->position = $position;

        return $this;
    }

    /**
     * Get position
     *
     * @return integer 
     */
    public function getPosition() {
        return $this->position;
    }

    /**
     * Set many
     *
     * @param boolean $many
     * @return JPPostTypeAttr
     */
    public function setMany($many) {
        $this->many = $many;

        return $this;
    }

    /**
     * Get many
     *
     * @return boolean 
     */
    public function getMany() {
        return $this->many;
    }

    /**
     * Set required
     *
     * @param boolean $required
     * @return JPPostTypeAttr
     */
    public function setRequired($required) {
        $this->required = $required;

        return $this;
    }

    /**
     * Get required
     *
     * @return boolean 
     */
    public function getRequired() {
        return $this->required;
    }

    /**
     * Set postType
     *
     * @param \Japode\PostType\Entity\JPPostType $postType
     * @return JPPostTypeAttr
     */
    public function setPostType(\Japode\PostType\Entity\JPPostType $postType = null) {
        $this->postType = $postType;

        return $this;
    }

    /**
     * Get postType
     *
     * @return \Japode\PostType\Entity\JPPostType 
     */
    public function getPostType() {
        return $this->postType;
    }

    /**
     * Set widget
     *
     * @param \Japode\Widget\Entity\JPWidget $widget
     * @return JPPostTypeAttr
     */
    public function setWidget(\Japode\Widget\Entity\JPWidget $widget = null) {
        $this->widget = $widget;

        return $this;
    }

    /**
     * Get widget
     *
     * @return \Japode\Widget\Entity\JPWidget 
     */
    public function getWidget() {
        return $this->widget;
    }

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->post_attrs = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * Add post_attrs
     *
     * @param \Japode\Post\Entity\JPPostAttr $postAttrs
     * @return JPPostTypeAttr
     */
    public function addPostAttr(\Japode\Post\Entity\JPPostAttr $postAttrs)
    {
        $this->post_attrs[] = $postAttrs;

        return $this;
    }

    /**
     * Remove post_attrs
     *
     * @param \Japode\Post\Entity\JPPostAttr $postAttrs
     */
    public function removePostAttr(\Japode\Post\Entity\JPPostAttr $postAttrs)
    {
        $this->post_attrs->removeElement($postAttrs);
    }

    /**
     * Get post_attrs
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getPostAttrs()
    {
        return $this->post_attrs;
    }
    
}
