<?php

namespace Japode\PostType\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;
use \JsonSerializable;
use \DateTime;

/**
 * PostType entity
 *
 * @Entity(repositoryClass="JPPostTypeRepository")
 */
class JPPostType implements JsonSerializable {

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
     * @Column(type="string", length=50)
     */
    protected $title;

    /**
     * @var string
     *
     * @Column(type="string", length=100)
     */
    protected $description;

    /**
     * @var \DateTime
     *
     * @Column(type="datetime", nullable=true)
     */
    protected $date;

    /**
     * @var JPPostTypeAttr[]
     *
     * @OneToMany(targetEntity="JPPostTypeAttr", mappedBy="postType")
     * @OrderBy({"position" = "ASC"})
     */
    protected $postTypeAttrs;

    /**
     * @var \Japode\Post\Entity\JPPosts[]
     *
     * @OneToMany(targetEntity="\Japode\Post\Entity\JPPost", mappedBy="post_type")
     */
    protected $posts;

    /**
     * @var \Japode\Post\Entity\JPPostAttr[]
     *
     * @OneToMany(targetEntity="\Japode\Post\Entity\JPPostAttr", mappedBy="post_type")
     *
     */
    protected $post_attrs;

    /**
     * @var \Japode\Region\Entity\JPRegion[]
     *
     * @OneToMany(targetEntity="\Japode\Region\Entity\JPRegion", mappedBy="post_type")
     */
    protected $regions;

    public function jsonSerialize() {
        $mdate = new DateTime($this->getDate());
        return array(
            'id' => $this->getId(),
            'name' => $this->getName(),
            'title' => $this->getTitle(),
            'description' => html_entity_decode(strip_tags($this->getDescription())),
            'date' => $mdate->format('d/m/Y')
        );
    }

    /**
     * Constructor
     */
    public function __construct()
    {
        $this->postTypeAttrs = new \Doctrine\Common\Collections\ArrayCollection();
        $this->posts = new \Doctrine\Common\Collections\ArrayCollection();
        $this->post_attrs = new \Doctrine\Common\Collections\ArrayCollection();
        $this->regions = new \Doctrine\Common\Collections\ArrayCollection();
    }

    /**
     * Set id
     *
     * @param string $id
     * @return JPPostType
     */
    public function setId($id)
    {
        $this->id = $id;

        return $this;
    }

    /**
     * Get id
     *
     * @return string 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set name
     *
     * @param string $name
     * @return JPPostType
     */
    public function setName($name)
    {
        $this->name = $name;

        return $this;
    }

    /**
     * Get name
     *
     * @return string 
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * Set title
     *
     * @param string $title
     * @return JPPostType
     */
    public function setTitle($title)
    {
        $this->title = $title;

        return $this;
    }

    /**
     * Get title
     *
     * @return string 
     */
    public function getTitle()
    {
        return $this->title;
    }

    /**
     * Set description
     *
     * @param string $description
     * @return JPPostType
     */
    public function setDescription($description)
    {
        $this->description = $description;

        return $this;
    }

    /**
     * Get description
     *
     * @return string 
     */
    public function getDescription()
    {
        return $this->description;
    }

    /**
     * Set date
     *
     * @param \DateTime $date
     * @return JPPostType
     */
    public function setDate($date)
    {
        $this->date = $date;

        return $this;
    }

    /**
     * Get date
     *
     * @return \DateTime 
     */
    public function getDate()
    {
        return $this->date;
    }

    /**
     * Add postTypeAttrs
     *
     * @param \Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs
     * @return JPPostType
     */
    public function addPostTypeAttr(\Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs)
    {
        $this->postTypeAttrs[] = $postTypeAttrs;

        return $this;
    }

    /**
     * Remove postTypeAttrs
     *
     * @param \Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs
     */
    public function removePostTypeAttr(\Japode\PostType\Entity\JPPostTypeAttr $postTypeAttrs)
    {
        $this->postTypeAttrs->removeElement($postTypeAttrs);
    }

    /**
     * Get postTypeAttrs
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getPostTypeAttrs()
    {
        return $this->postTypeAttrs;
    }

    /**
     * Add posts
     *
     * @param \Japode\Post\Entity\JPPost $posts
     * @return JPPostType
     */
    public function addPost(\Japode\Post\Entity\JPPost $posts)
    {
        $this->posts[] = $posts;

        return $this;
    }

    /**
     * Remove posts
     *
     * @param \Japode\Post\Entity\JPPost $posts
     */
    public function removePost(\Japode\Post\Entity\JPPost $posts)
    {
        $this->posts->removeElement($posts);
    }

    /**
     * Get posts
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getPosts()
    {
        return $this->posts;
    }

    /**
     * Add post_attrs
     *
     * @param \Japode\Post\Entity\JPPostAttr $postAttrs
     * @return JPPostType
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

    /**
     * Add regions
     *
     * @param \Japode\Region\Entity\JPRegion $regions
     * @return JPPostType
     */
    public function addRegion(\Japode\Region\Entity\JPRegion $regions)
    {
        $this->regions[] = $regions;

        return $this;
    }

    /**
     * Remove regions
     *
     * @param \Japode\Region\Entity\JPRegion $regions
     */
    public function removeRegion(\Japode\Region\Entity\JPRegion $regions)
    {
        $this->regions->removeElement($regions);
    }

    /**
     * Get regions
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getRegions()
    {
        return $this->regions;
    }
}
