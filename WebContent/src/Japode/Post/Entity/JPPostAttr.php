<?php

namespace Japode\Post\Entity;

use Doctrine\ORM\Mapping\Entity;
use Doctrine\ORM\Mapping\Table;
use Doctrine\ORM\Mapping\Index;
use Doctrine\ORM\Mapping\Id;
use Doctrine\ORM\Mapping\GeneratedValue;
use Doctrine\ORM\Mapping\Column;

/**
 * PostAttr entity
 *
 * @Entity(repositoryClass="JPPostAttrRepository")
 */
class JPPostAttr {

    /**
     * @var string
     * @Id
     * @Column(type="string", length=40)
     */
    protected $id;

    /**
     * @var \Japode\PostType\Entity\JPPostType
     *
     * @ManyToOne(targetEntity="\Japode\PostType\Entity\JPPostType", inversedBy="post_attrs")
     */
    protected $post_type;

    /**
     * @var \Japode\PostType\Entity\JPPostTypeAttr
     *
     * @ManyToOne(targetEntity="\Japode\PostType\Entity\JPPostTypeAttr", inversedBy="post_attrs")
     */
    protected $post_type_attr;

    /**
     * @var \Japode\Post\Entity\JPPost
     *
     * @ManyToOne(targetEntity="\Japode\Post\Entity\JPPost", inversedBy="post_attrs")
     */
    protected $post;

    /**
     * @var int
     * 
     * @Column(type="integer", nullable=true)
     */
    protected $int_value;

    /**
     * @var string
     * 
     * @Column(type="text", nullable=true)
     */
    protected $str_value;

    /**
     * @var \DateTime
     *
     * @Column(type="datetime", nullable=true)
     */
    protected $date_value;

    /**
     * @var int
     * 
     * @Column(type="integer")
     */
    protected $type;


    /**
     * Set id
     *
     * @param string $id
     * @return JPPostAttr
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
     * Set int_value
     *
     * @param integer $intValue
     * @return JPPostAttr
     */
    public function setIntValue($intValue)
    {
        $this->int_value = $intValue;

        return $this;
    }

    /**
     * Get int_value
     *
     * @return integer 
     */
    public function getIntValue()
    {
        return $this->int_value;
    }

    /**
     * Set str_value
     *
     * @param string $strValue
     * @return JPPostAttr
     */
    public function setStrValue($strValue)
    {
        $this->str_value = $strValue;

        return $this;
    }

    /**
     * Get str_value
     *
     * @return string 
     */
    public function getStrValue()
    {
        return $this->str_value;
    }

    /**
     * Set date_value
     *
     * @param \DateTime $dateValue
     * @return JPPostAttr
     */
    public function setDateValue($dateValue)
    {
        $this->date_value = $dateValue;

        return $this;
    }

    /**
     * Get date_value
     *
     * @return \DateTime 
     */
    public function getDateValue()
    {
        return $this->date_value;
    }

    /**
     * Set type
     *
     * @param integer $type
     * @return JPPostAttr
     */
    public function setType($type)
    {
        $this->type = $type;

        return $this;
    }

    /**
     * Get type
     *
     * @return integer 
     */
    public function getType()
    {
        return $this->type;
    }

    /**
     * Set post_type
     *
     * @param \Japode\PostType\Entity\JPPostType $postType
     * @return \Japode\Post\Entity\JPPostAttr
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
     * Set post_type_attr
     *
     * @param \Japode\PostType\Entity\JPPostTypeAttr $postTypeAttr
     * @return \Japode\Post\Entity\JPPostAttr
     */
    public function setPostTypeAttr(\Japode\PostType\Entity\JPPostTypeAttr $postTypeAttr = null)
    {
        $this->post_type_attr = $postTypeAttr;

        return $this;
    }

    /**
     * Get post_type_attr
     *
     * @return \Japode\PostType\Entity\JPPostTypeAttr 
     */
    public function getPostTypeAttr()
    {
        return $this->post_type_attr;
    }

    /**
     * Set post
     *
     * @param \Japode\Post\Entity\JPPost $post
     * @return \Japode\Post\Entity\JPPostAttr
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
