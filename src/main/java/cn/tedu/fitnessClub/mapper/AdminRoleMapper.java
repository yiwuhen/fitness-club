package cn.tedu.fitnessClub.mapper;


import cn.tedu.fitnessClub.pojo.entity.AdminRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理管理员与角色的关联数据的Mapper接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Repository
public interface AdminRoleMapper {

    /**
     * 批量插入管理员与角色的关联数据
     *
     * @param adminRoleList 若干个管理员与角色的关联数据的集合
     * @return 受影响的行数
     */
    int insertBatch(List<AdminRole> adminRoleList);

    /**
     * 根据管理员id删除管理员与角色的关联数据
     *
     * @param adminId 管理员id
     * @return 受影响的行数
     */
    int deleteByAdminId(Long adminId);

}
