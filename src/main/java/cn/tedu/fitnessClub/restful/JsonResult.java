package cn.tedu.fitnessClub.restful;

import cn.tedu.fitnessClub.ex.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类型
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class JsonResult<T> implements Serializable {

    /**
     * 操作结果的状态码（状态标识）
     */
    @ApiModelProperty("业务状态码")
    private Integer state;
    /**
     * 操作失败时的提示文本
     */
    @ApiModelProperty("提示文本")
    private String message;
    /**
     * 操作成功时响应的数据
     */
    @ApiModelProperty("数据")
    private T data; // T=Type, K=Key, V=Value, E=Element

    public static JsonResult<Void> ok() {
        // JsonResult jsonResult = new JsonResult();
        // jsonResult.state = ServiceCode.OK.getValue();
        // return jsonResult;
        return ok(null);
    }

    public static <T> JsonResult<T> ok(T data) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.state = ServiceCode.OK.getValue();
        jsonResult.data = data;
        return jsonResult;
    }

    public static JsonResult<Void> fail(ServiceException e) {
        return fail(e.getServiceCode(), e.getMessage());
    }

    public static JsonResult<Void> fail(ServiceCode serviceCode, String message) {
        JsonResult<Void> jsonResult = new JsonResult<>();
        jsonResult.state = serviceCode.getValue();
        jsonResult.message = message;
        return jsonResult;
    }

}