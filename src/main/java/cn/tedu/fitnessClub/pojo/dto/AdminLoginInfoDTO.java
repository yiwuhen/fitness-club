package cn.tedu.fitnessClub.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员登录的DTO类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class AdminLoginInfoDTO implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（原文）
     */
    private String password;

}