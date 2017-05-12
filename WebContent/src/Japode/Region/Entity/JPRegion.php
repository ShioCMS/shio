<?php
namespace Japode\Region\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;

/**
 * Region entity
 *
 * @Entity(repositoryClass="JPRegionRepository")
 */
class JPRegion {
    
     /**
     * @var string
     * @Id
     * @Column(type="string", length=40)
     */
    protected $id;
    
     /**
     * @var string
     * 
     * @Column(type="string", length=100)
     */
    protected $name;
    
    /**
     * @var \Japode\PostType\Entity\JPPostType
     *
     * @ManyToOne(targetEntity="\Japode\PostType\Entity\JPPostType", inversedBy="regions")
     */
    protected $post_type;
    
    /**
     * @var \Japode\Post\Entity\JPPost
     *
     * @ManyToOne(targetEntity="\Japode\Post\Entity\JPPost", inversedBy="regions")
     */
    protected $post;

    /**
     * Set id
     *
     * @param string $id
     * @return JPRegion
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
     * @return JPRegion
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
     * Set post_type
     *
     * @param \Japode\PostType\Entity\JPPostType $postType
     * @return JPRegion
     */
    public function setPostType(\Japode\PostType\Entity\JPPostType $postType = null)
    {
        $this->post_type = $postType;

        return $this;
    }

    /**
     * Get post_type
     *
     * @return \Japode\PostType\Entity\JPPostType 
     */
    public function getPostType()
    {
        return $this->post_type;
    }

    /**
     * Set post
     *
     * @param \Japode\Post\Entity\JPPost $post
     * @return JPRegion
     */
    public function setPost(\Japode\Post\Entity\JPPost $post = null)
    {
        $this->post = $post;

        return $this;
    }

    /**
     * Get post
     *
     * @return \Japode\Post\Entity\JPPost 
     */
    public function getPost()
    {
        return $this->post;
    }
}
