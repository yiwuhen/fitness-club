package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.mapper.RoleMapper;
import cn.tedu.fitnessClub.pojo.vo.RoleListItemVO;
import cn.tedu.fitnessClub.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    public RoleServiceImpl() {
        log.debug("创建业务类对象：RoleServiceImpl");
    }

    @Override
    public List<RoleListItemVO> list() {
        log.debug("开始处理【查询角色列表】的业务，参数：无");
        List<RoleListItemVO> list = roleMapper.list();
        Iterator<RoleListItemVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            RoleListItemVO item = iterator.next();
            if (item.getId() == 1) {
                iterator.remove();
                break;
            }
        }
        return list;
    }

}
