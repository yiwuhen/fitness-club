package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改文章图片的DTO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticlePictureUpdateDTO implements Serializable {
    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章图片url
     */
    private String url;

    /**
     * 文章图片简介
     */
    private String description;

    /**
     * 文章图片宽度，单位：px
     */
    private Integer width;

    /**
     * 文章图片高度，单位：px
     */
    private Integer height;

    /**
     * 排序序号
     */
    private Integer sort;
}
