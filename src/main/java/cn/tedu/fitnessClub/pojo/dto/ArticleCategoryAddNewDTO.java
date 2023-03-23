package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加文章类别的DTO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticleCategoryAddNewDTO implements Serializable {

    /**
     * 文章类别名称
     */
    private String name;

    /**
     * 父级文章类别id，如果无父级，则为0
     */
    private Long parentId;

    /**
     * 排序序号
     */
    private Integer sort;

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    private Integer isDisplay;

}