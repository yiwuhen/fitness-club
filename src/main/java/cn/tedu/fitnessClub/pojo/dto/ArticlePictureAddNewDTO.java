package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 新增文章图片的DTO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticlePictureAddNewDTO implements Serializable {
    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 图片url
     */
    private String url;

    /**
     * 是否为封面图片，1=是，0=否
     */
    private Integer isCover;

    /**
     * 图片简介
     */
    private String description;

    /**
     * 图片宽度，单位：px
     */
    private Integer width;

    /**
     * 图片高度，单位：px
     */
    private Integer height;

    /**
     * 排序序号
     */
    private Integer sort;
}
