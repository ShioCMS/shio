<?php

namespace Japode\Post\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;
use Japode\PostType\Entity\JPPostType;
use \JsonSerializable;

/**
 * Post entity
 *
 * @Entity(repositoryClass="JPPostRepository")
 */
class JPPost implements JsonSerializable {

    /**
     * @var string
     * @Id
     * @Column(type="string", length=40)
     */
    protected $id;

    /**
     * @var JPPostType
     *
     * @ManyToOne(targetEntity="\Japode\PostType\Entity\JPPostType", inversedBy="posts")
     */
    protected $post_type;

    /**
     * @var string
     * 
     * @Column(type="string", length=150)
     */
    protected $title;

    /**
     * @var string
     * 
     * @Column(type="string", length=150)
     */
    protected $summary;

    /**
     * @var \DateTime
     *
     * @Column(type="datetime", nullable=true)
     */
    protected $date;

    /**
     * @var \Japode\Post\Entity\JPPostAttr[]
     *
     * @OneToMany(targetEntity="\Japode\Post\Entity\JPPostAttr", mappedBy="post")
     */
    protected $post_attrs;

    /**
     * @var \Japode\Region\Entity\JPRegion[]
     *
     * @OneToMany(targetEntity="\Japode\Region\Entity\JPRegion", mappedBy="post")
     */
    protected $regions;

    /**
     * Constructor
     */
    public function __construct() {
        $this->post_attrs = new \Doctrine\Common\Collections\ArrayCollection();
        $this->regions = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * Set id
     *
     * @param string $id
     * @return JPPost
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
     * Set title
     *
     * @param string $title
     * @return JPPost
     */
    public function setTitle($title) {
        $this->title = $title;

        return $this;
    }

    /**
     * Get title
     *
     * @return string 
     */
    public function getTitle() {
        return $this->title;
    }

    /**
     * Set summary
     *
     * @param string $summary
     * @return JPPost
     */
    public function setSummary($summary) {
        $this->summary = $summary;

        return $this;
    }

    /**
     * Get summary
     *
     * @return string 
     */
    public function getSummary() {
        return $this->summary;
    }

    /**
     * Set date
     *
     * @param \DateTime $date
     * @return JPPost
     */
    public function setDate($date) {
        $this->date = $date;

        return $this;
    }

    /**
     * Get date
     *
     * @return \DateTime 
     */
    public function getDate() {
        return $this->date;
    }

    /**
     * Set post_type
     *
     * @param \Japode\PostType\Entity\JPPostType $postType
     * @return JPPost
     */
    public function setPostType(\Japode\PostType\Entity\JPPostType $postType = null) {
        $this->post_type = $postType;

        return $this;
    }

    /**
     * Get post_type
     *
     * @return \Japode\PostType\Entity\JPPostType 
     */
    public function getPostType() {
        return $this->post_type;
    }

    /**
     * Add post_attrs
     *
     * @param \Japode\Post\Entity\JPPostAttr $postAttrs
     * @return JPPost
     */
    public function addPostAttr(\Japode\Post\Entity\JPPostAttr $postAttrs) {
        $this->post_attrs[] = $postAttrs;

        return $this;
    }

    /**
     * Remove post_attrs
     *
     * @param \Japode\Post\Entity\JPPostAttr $postAttrs
     */
    public function removePostAttr(\Japode\Post\Entity\JPPostAttr $postAttrs) {
        $this->post_attrs->removeElement($postAttrs);
    }

    /**
     * Get post_attrs
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getPostAttrs() {
        return $this->post_attrs;
    }

    /**
     * Add regions
     *
     * @param \Japode\Region\Entity\JPRegion $regions
     * @return JPPost
     */
    public function addRegion(\Japode\Region\Entity\JPRegion $regions) {
        $this->regions[] = $regions;

        return $this;
    }

    /**
     * Remove regions
     *
     * @param \Japode\Region\Entity\JPRegion $regions
     */
    public function removeRegion(\Japode\Region\Entity\JPRegion $regions) {
        $this->regions->removeElement($regions);
    }

    /**
     * Get regions
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getRegions() {
        return $this->regions;
    }

    public function jsonSerialize() {
        $mdate = $this->getDate();
        $postType = $this->getPostType();
        return array(
            'id' => $this->getID(),
            'posttype' => $postType->getId(),
            'posttypeidentifier' => $postType->getName(),
            'posttypename' => $postType->getTitle(),
            'content' => (strlen($this->getTitle()) > 0) ? $this->getTitle() : "...",
            'description' => (strlen($this->getSummary()) > 0) ? html_entity_decode(strip_tags($this->getSummary())) : "...",
            'label' => "SiteA",
            'date' => $mdate->format('Y-m-d\TH:i:s'),
            'date_format' => $mdate->format('d F Y')
        );
    }

}
