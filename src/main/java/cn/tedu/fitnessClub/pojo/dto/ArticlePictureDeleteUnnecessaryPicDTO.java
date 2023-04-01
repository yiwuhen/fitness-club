package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 需要删除的冗余图片
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class ArticlePictureDeleteUnnecessaryPicDTO implements Serializable {

    /**
     * 图片的类型
     */
    private String type;

    /**
     * 图片的url
     */
    private String src;

    /**
     * 图片的链接地址
     */
    private String href;

    /**
     * 图片的替代文本
     */
    private String alt;

    /**
     * 图片的样式属性
     */
    private Map<String, String> style;

    /**
     * 图片的子元素列表
     */
    private List<Map<String, String>> children;
}
