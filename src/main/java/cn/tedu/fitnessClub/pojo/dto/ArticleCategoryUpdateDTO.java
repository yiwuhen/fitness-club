package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改文章类别的DTO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticleCategoryUpdateDTO implements Serializable {

    /**
     * 文章类别名称
     */
    private String name;

    /**
     * 排序序号
     */
    private Integer sort;

}