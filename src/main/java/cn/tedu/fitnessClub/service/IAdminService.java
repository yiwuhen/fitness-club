package cn.tedu.fitnessClub.service;

import cn.tedu.fitnessClub.pojo.dto.AdminAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.AdminLoginInfoDTO;
import cn.tedu.fitnessClub.pojo.vo.AdminListItemVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IAdminService {

    /**
     * 管理员登录
     * @param adminLoginInfoDTO 封装了用户名、密码等相关信息的对象
     * @return 此管理员登录后得到的JWT数据
     */
    String login(AdminLoginInfoDTO adminLoginInfoDTO);

    /**
     * 添加管理员
     *
     * @param adminAddNewDTO 管理员数据
     */
    void addNew(AdminAddNewDTO adminAddNewDTO);

    /**
     * 删除管理员
     *
     * @param id 管理员id
     */
    void delete(Long id);

    /**
     * 查询管理员列表
     *
     * @return 管理员列表
     */
    List<AdminListItemVO> list();

}
