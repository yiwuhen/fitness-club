package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章图片
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticlePictureDeleteCoverDTO implements Serializable {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章图片url
     */
    private String url;

    /**
     * 是否为封面文章图片，1=是，0=否
     */
    private Integer isCover;

    /**
     * 是否执行删数据库操作，1=是，0=否
     */
    private Integer isDelDB;

    /**
     * 文章图片简介
     */
    private String description;
}
