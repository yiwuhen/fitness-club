package cn.tedu.fitnessClub.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章类别数据的列表项VO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticleCategoryListItemVO implements Serializable {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 文章类别名称
     */
    private String name;

    /**
     * 父级文章类别id，如果无父级，则为0
     */
    private Long parentId;

    /**
     * 深度，最顶级文章类别的深度为1，次级为2，以此类推
     */
    private Integer depth;

    /**
     * 排序序号
     */
    private Integer sort;

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;

    /**
     * 是否为父级（是否包含子级），1=是父级，0=不是父级
     */
    private Integer isParent;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    private Integer isDisplay;

    /**
     * 子集列表
     *
     */
    private List<ArticleCategoryListItemVO> children;

    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;

}