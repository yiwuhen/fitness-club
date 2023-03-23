package cn.tedu.fitnessClub.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章列表的VO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticleStandardVO implements Serializable {
    /**
     * 数据id
     */
    private Long id;

    /**
     * 类别id
     */
    private Long categoryId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简介
     */
    private String description;

    /**
     * 正文
     */
    private String content;

    /**
     * 浏览量
     */
    private Long viewCount;

    /**
     * 排序序号
     */
    private Integer sort;

}
