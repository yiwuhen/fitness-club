package cn.tedu.fitnessClub.restful;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JsonPage<T> implements Serializable {

    // 当前类用于代替PageInfo或Page这样的分页操作返回类型,实现项目中分页查询结果类型的统一
    // 这里我们声明基本的分页信息,如果前端需要再添加属性即可
    @ApiModelProperty(value = "总页数",name ="totalPages")
    private Integer totalPages;
    @ApiModelProperty(value = "总条数",name ="totalCount")
    private Long totalCount;
    @ApiModelProperty(value = "页码",name ="page")
    private Integer page;
    @ApiModelProperty(value = "每页条数",name ="pageSize")
    private Integer pageSize;

    // JsonPage类还要保存查询到的数据
    @ApiModelProperty(value = "分页数据",name ="list")
    private List<T> list;

    // 下面编写一个方法,能够实现将PageInfo类型对象转换为JsonPage类型对象的方法
    public static <T> JsonPage<T> restPage(PageInfo<T> pageInfo){

        // 将参数pageInfo对象中意义匹配的属性赋值到JsonPage对象的属性中
        JsonPage<T> jsonPage=new JsonPage<>();
        jsonPage.setTotalCount(pageInfo.getTotal());
        jsonPage.setTotalPages(pageInfo.getPages());
        jsonPage.setPage(pageInfo.getPageNum());
        jsonPage.setPageSize(pageInfo.getPageSize());
        // 分页数据也要赋值过来
        jsonPage.setList(pageInfo.getList());
        // 返回转换完成的对象
        return jsonPage;

    }




}
