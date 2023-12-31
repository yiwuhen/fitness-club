package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改文章的DTO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticleUpdateDTO implements Serializable {

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
     * 类别id
     */
    @NotNull(message = "添加文章失败，必须选择文章类别！")
    private Long categoryId;

    /**
     * 排序序号
     */
    private Integer sort;

}
